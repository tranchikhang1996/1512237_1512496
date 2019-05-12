package com.example.highschoolmathsolver.ui.scan.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.RuntimePermissionFragment
import com.example.highschoolmathsolver.ui.scan.view.CameraSource
import com.example.highschoolmathsolver.viewmodel.SharedModel
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.scan_fragment_layout.*
import timber.log.Timber
import java.io.IOException

class ScanFragment : RuntimePermissionFragment() {

    companion object {
        const val CAMERA_PREVIEW_DEFAULT_WIDTH = 1280
        const val CAMERA_PREVIEW_DEFAULT_HEIGHT = 720

        @JvmStatic
        fun newInstance(args: Bundle? = null): ScanFragment =
            ScanFragment().apply { arguments = args }
    }

    override val requestLayoutID: Int get() = R.layout.scan_fragment_layout
    private var hasRequestedCameraPermission: Boolean = false
    private var cameraSource: CameraSource? = null
    private val viewModel: SharedModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SharedModel::class.java)
    }

    private var isExpressionDetected = false

    override fun setupFragmentComponent() {
        getUserComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_allow_cam.setOnClickListener {
            hasRequestedCameraPermission = false
            startScan()
        }
        close.setOnClickListener {
            if(activity?.isFinishing == false) {
                activity?.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startScan()
    }

    override fun onPause() {
        cameraSourcePreview.release()
        super.onPause()
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        if (hasRequestedCameraPermission && !isPermissionGranted(Manifest.permission.CAMERA)) {
            return
        }

        if (!isPermissionGrantedAndRequest(Manifest.permission.CAMERA, CAMERA)) {
            return
        }
        overlay_view.startIndicator()
        isExpressionDetected = false
        createCameraSource()
    }

    private fun createCameraSource() {
        if (activity?.isFinishing != false) {
            return
        }

        val barcodeDetector = BarcodeDetector.Builder(activity)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barCodes = detections.detectedItems
                if (barCodes.size() != 0 && !isExpressionDetected) {
                    isExpressionDetected = true
                    handleResult(barCodes.valueAt(0).displayValue)
                }
            }
        })
        cameraSource = CameraSource.Builder(activity, barcodeDetector)
            .setRequestedPreviewSize(CAMERA_PREVIEW_DEFAULT_WIDTH, CAMERA_PREVIEW_DEFAULT_HEIGHT)
            .build()
        scan()
    }

    private fun handleResult(expression : String) {
        viewModel.solve(expression)
    }

    @SuppressLint("MissingPermission")
    private fun scan() {
        try {
            cameraSourcePreview.start(cameraSource)
            cameraSourcePreview.visibility = View.VISIBLE
        } catch (e: IOException) {
            Timber.d("[ScanQR] Unable to start camera source: %s", e.message)
            showError(R.string.exception_open_camera_fail)
            cameraSourcePreview.release()
        } catch (e: RuntimeException) {
            Timber.d("[ScanQR] Unable to start camera source: %s", e.message)
            showError(R.string.exception_open_camera_fail)
            cameraSourcePreview.release()
        }

    }

    override fun permissionGranted(permissionRequestCode: Int, isGranted: Boolean) {
        hasRequestedCameraPermission = true
        when (permissionRequestCode) {
            CAMERA -> {
                if(!isGranted) {
                    overlay_view.stopIndicate()
                }
                permission_denied_panel.visibility = if (isGranted) View.GONE else View.VISIBLE
            }
            else -> Unit
        }
    }

}


