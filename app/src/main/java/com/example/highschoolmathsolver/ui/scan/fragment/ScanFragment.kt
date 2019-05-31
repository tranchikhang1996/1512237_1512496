package com.example.highschoolmathsolver.ui.scan.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.detector.SCFGDetector
import com.example.highschoolmathsolver.detector.listener.FrameSizeChangeListener
import com.example.highschoolmathsolver.ui.RuntimePermissionFragment
import com.example.highschoolmathsolver.ui.scan.view.CameraSource
import com.example.highschoolmathsolver.util.AndroidUtils
import com.google.android.gms.vision.Detector
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.scan_fragment_layout.*
import org.opencv.core.Rect
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ScanFragment : RuntimePermissionFragment(), FrameSizeChangeListener {

    companion object {
        const val CAMERA_PREVIEW_DEFAULT_WIDTH = 1280
        const val CAMERA_PREVIEW_DEFAULT_HEIGHT = 720

        @JvmStatic
        fun newInstance(args: Bundle? = null): ScanFragment = ScanFragment().apply { arguments = args }
    }

    override val requestLayoutID: Int get() = R.layout.scan_fragment_layout
    @Inject lateinit var detector: SCFGDetector

    private var hasRequestedCameraPermission: Boolean = false
    private var cameraSource: CameraSource? = null
    private val subject = PublishSubject.create<ByteArray>()

    override fun setupFragmentComponent() {
        getUserComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overlay_view.frameSizeChangeListener = this
        btn_allow_cam.setOnClickListener {
            hasRequestedCameraPermission = false
            startScan()
        }
        close.setOnClickListener {
            if (activity?.isFinishing == false) {
                activity?.finish()
            }
        }

        viewModel.getFrameSize().observe(this, Observer {
            detector.onFrameSizeChange(it)
        })

        initSubject()
    }

    private fun showScanLoading() {
        mSubscriptions.add(AndroidUtils.runOnUIThreadWithRxjava {
            loading_view.visibility = View.VISIBLE
            overlay_view.stopIndicate()
        })
    }

    private fun hideScanLoading() {
        mSubscriptions.add(AndroidUtils.runOnUIThreadWithRxjava {
            loading_view.visibility = View.GONE
            overlay_view.startIndicator()
        })
    }

    private fun initSubject() {

        val disposable = subject.toFlowable(BackpressureStrategy.LATEST)
            .observeOn(Schedulers.computation())
            .doOnNext {
                showScanLoading()
            }
            .map { detector.detectFromByteArray(it) }
            .onErrorReturnItem("")
            .subscribe(
                {
                    handleResult(it)
                    hideScanLoading()
                },
                { throwable ->
                    hideScanLoading()
                    Timber.d(throwable, "[scanME] onError!")
                })
        mSubscriptions.add(disposable)

    }

    override fun onResume() {
        super.onResume()
        startScan()
    }

    override fun onPause() {
        cameraSourcePreview.release()
        super.onPause()
    }

    override fun onDestroyView() {
        detector.imageView = null
        super.onDestroyView()
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        if (hasRequestedCameraPermission && !isPermissionGranted(Manifest.permission.CAMERA)) {
            return
        }

        if (!isPermissionGrantedAndRequest(Manifest.permission.CAMERA, CAMERA)) {
            return
        }
        createCameraSource()
    }

    private fun createCameraSource() {
        if (activity?.isFinishing != false) {
            return
        }

        detector.setProcessor(object : Detector.Processor<String?> {
            // use for auto scanning in the future
            override fun release() = Unit

            override fun receiveDetections(detections: Detector.Detections<String?>) = Unit

        })

        cameraSource = CameraSource.Builder(detector)
            .setRequestedPreviewSize(CAMERA_PREVIEW_DEFAULT_WIDTH, CAMERA_PREVIEW_DEFAULT_HEIGHT)
            .build()

        shutter_btn.setOnClickListener {
            cameraSource?.takePicture({}, { bytes -> subject.onNext(bytes) })
        }
        scan()
    }

    private fun handleResult(expression: String) {
        if (expression.isEmpty()) {
            return
        }
        viewModel.solve(expression)
        viewModel.save(expression)
    }

    @SuppressLint("MissingPermission")
    private fun scan() {
        try {
            cameraSourcePreview.start(cameraSource)
            cameraSourcePreview.visibility = View.VISIBLE
            overlay_view.startIndicator()
            detector.imageView = image_dung_de_test
            detector.onFrameSizeChange(viewModel.getFrameSize().value ?: Rect())
        } catch (e: IOException) {
            Timber.d("[scanME] Unable to start camera source: %s", e.message)
            showError(R.string.exception_open_camera_fail)
            cameraSourcePreview.release()
        } catch (e: RuntimeException) {
            Timber.d("[scanME] Unable to start camera source: %s", e.message)
            showError(R.string.exception_open_camera_fail)
            cameraSourcePreview.release()
        }

    }

    override fun permissionGranted(permissionRequestCode: Int, isGranted: Boolean) {
        hasRequestedCameraPermission = true
        when (permissionRequestCode) {
            CAMERA -> {
                if (!isGranted) {
                    overlay_view.stopIndicate()
                }
                permission_denied_panel.visibility = if (isGranted) View.GONE else View.VISIBLE
            }
            else -> Unit
        }
    }

    override fun onFrameSizeChange(rect: Rect) {
        viewModel.changeFrameSize(rect)
    }
}

/**
            val src = Mat()
            val assetManager = activity!!.assets
            val link = assetManager.open("example.png")
            val myBitmap = BitmapFactory.decodeStream(link)
            Utils.bitmapToMat(myBitmap, src)
            val gray = Mat()
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY)
            subject.onNext(gray)
**/
