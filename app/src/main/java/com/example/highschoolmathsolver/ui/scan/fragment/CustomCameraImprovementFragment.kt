package com.example.highschoolmathsolver.ui.scan.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.customui.OverlayView
import com.example.highschoolmathsolver.ui.scan.view.AutoFitTextureView
import com.example.highschoolmathsolver.util.AndroidUtils
import com.example.highschoolmathsolver.util.Camera2Utils
import kotlinx.android.synthetic.main.fragment_kyc_improvement_camera.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CustomCameraImprovementFragment : BaseCustomCameraFragment() {
    private lateinit var mCameraManager: CameraManager

    private lateinit var mCameraIDs: Array<String>
    private lateinit var mCameraID: String
    private var mCurrentCamera: Int = 0
    private var mCamera: CameraDevice? = null
    private lateinit var mBackgroundThread: HandlerThread
    private lateinit var mBackgroundHandler: Handler
    private var mImageReader: ImageReader? = null
    private lateinit var mPreviewSize: Size
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mSensorOrientation: Int? = DEFAULT_SENSOR_ORIENTATION
    private var surface: Surface? = null
    private lateinit var overlayView : OverlayView
    private lateinit var autoFitView : AutoFitTextureView
    @Volatile
    private var requestNext = true

    companion object {

        const val AUTO_FIT_PREVIEW_DELAY_TIME = 100L

        const val MAX_IMAGE = 1

        const val DEFAULT_SENSOR_ORIENTATION = 90

        const val CAMERA_THREAD_NAME = "custom_camera"
        const val MAX_PREVIEW_WIDTH = 1920
        const val MAX_PREVIEW_HEIGHT = 1080
        const val MAX_CAMERA_ACCESS_TIME = 5000L
    }

    override fun setupFragmentComponent() {
        getUserComponent().inject(this)
    }

    override fun getResLayoutId(): Int = R.layout.fragment_kyc_improvement_camera

    override fun initCameraCharacteristics(preferFront: Boolean) {
        mCameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        mCameraIDs = Camera2Utils.getListCameraIDs(mCameraManager)
        val (cameraId, index) = Camera2Utils.getCameraId(mCameraManager, preferFront)
        mCameraID = cameraId
        mCurrentCamera = index
        overlayView = overlay_view
        autoFitView = camera_texture_view
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        startCamera()
    }

    private fun startCamera() {
        when {
            mCameraID.isEmpty() -> showError(R.string.kyc_camera_number_zero)
            camera_texture_view.isAvailable -> openCamera(camera_texture_view.width, camera_texture_view.height)
            else -> camera_texture_view.surfaceTextureListener = mSurfaceTextureListener
        }
    }

    override fun requestPermission() = startCamera()

    override fun freeCamera() {
        closeCamera()
        stopBackgroundThread()
    }

    private val mSurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            openCamera(width, height)
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) = Unit

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) = Unit

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = true
    }

    private val mCameraDeviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mCamera = camera
            createCameraPreviewSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            mCameraOpenCloseLock.release()
            camera.close()
            mCamera = null
            Timber.d("[KYC-camera improvement] camera disconnected")
        }

        override fun onError(camera: CameraDevice, error: Int) {
            mCameraOpenCloseLock.release()
            camera.close()
            mCamera = null
            showError(R.string.exception_open_camera_fail)
            Timber.w("[KYC-camera improvement] open camera error with type [%d]", error)
        }
    }

    private val mCaptureSessionCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            mCameraCaptureSession = session
            mPreviewRequestBuilder?.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            val previewCaptureRequest = mPreviewRequestBuilder?.build()
            try {
                previewCaptureRequest?.let {
                    mCameraCaptureSession?.setRepeatingRequest(it, null, mBackgroundHandler)
                }
            } catch (e: CameraAccessException) {
                Timber.d("[KYC-camera improvement] fail to set Repeating request")
            } finally {
                mCameraOpenCloseLock.release()
            }
            mCompositeDisposable.add(AndroidUtils.runOnUIThreadWithRxjava { setControllability(true) })
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            mCameraOpenCloseLock.release()
            Timber.d("[KYC-camera improvement] fail to configure camera")
            showError(R.string.exception_open_camera_fail)
        }
    }

    private fun createCameraPreviewSession() {
        val surfaceTexture = camera_texture_view.surfaceTexture
        surfaceTexture.setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)
        surface = Surface(surfaceTexture)

        mImageReader = ImageReader.newInstance(mPreviewSize.width, mPreviewSize.height, ImageFormat.YUV_420_888, MAX_IMAGE)
        mImageReader?.setOnImageAvailableListener(onImageAvailableListener, mBackgroundHandler)

        try {
            mPreviewRequestBuilder = mCamera?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

            surface?.let { mPreviewRequestBuilder?.addTarget(it) }

            mImageReader?.surface?.let{mPreviewRequestBuilder?.addTarget(it)}

            mCamera?.createCaptureSession(
                arrayListOf(surface, mImageReader?.surface),
                mCaptureSessionCallback,
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            mCameraOpenCloseLock.release()
            showError(R.string.exception_open_camera_fail)
            Timber.w(e, "[KYC-camera improvement] fail to create camera session")
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(width: Int, height: Int) {
        if (hasRequestedCameraPermission && !isPermissionGranted(Manifest.permission.CAMERA)) {
            return
        }

        if (!isPermissionGrantedAndRequest(Manifest.permission.CAMERA, CAMERA)) {
            return
        }

        try {
            setUpCameraOutputs(width, height)
            if (!mCameraOpenCloseLock.tryAcquire(MAX_CAMERA_ACCESS_TIME, TimeUnit.MILLISECONDS)) {
                return
            }
            mCameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, mBackgroundHandler)
        } catch (e: CameraAccessException) {
            mCameraOpenCloseLock.release()
            showError(R.string.exception_open_camera_fail)
            Timber.w(e, "[Kyc-camera improvement] Open Camera Error with cameraID [%s]", mCameraID)
        } catch (e: InterruptedException) {
            Timber.d(e, "[Kyc-camera improvement] Interrupted while trying to lock camera opening")
        }

    }

    private fun setUpCameraOutputs(width: Int, height: Int) {
        val maxWidth = if (height > MAX_PREVIEW_WIDTH) MAX_PREVIEW_WIDTH else height
        val maxHeight = if (width > MAX_PREVIEW_HEIGHT) MAX_PREVIEW_HEIGHT else width
        val cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraID)
        val map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
        map ?: throw CameraAccessException(CameraAccessException.CAMERA_ERROR)
        val largest =
            map.getOutputSizes(ImageFormat.JPEG).maxWith(Camera2Utils.comparator) ?: throw CameraAccessException(
                CameraAccessException.CAMERA_ERROR
            )
        mPreviewSize = Camera2Utils.chooseOptimalSize(
            map.getOutputSizes(SurfaceTexture::class.java), height, width,
            maxWidth, maxHeight, largest
        )
        mCompositeDisposable.add(
            AndroidUtils.runWithDelay(
                { camera_texture_view.setAspectRatio(mPreviewSize.height, mPreviewSize.width) },
                AUTO_FIT_PREVIEW_DELAY_TIME
            )
        )
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread(CAMERA_THREAD_NAME)
        mBackgroundThread.start()
        mBackgroundHandler = Handler(mBackgroundThread.looper)
    }

    private fun stopBackgroundThread() {
        mBackgroundThread.quitSafely()
        try {
            mBackgroundThread.join()
        } catch (e: InterruptedException) {
            Timber.d(e, "[KYC-camera improvement] Interrupted while quiting thread")
        }
    }

    private fun closeCamera() {
        try {
            mCameraOpenCloseLock.acquire()
            mCameraCaptureSession?.close()
            mCamera?.close()
            mImageReader?.close()
            mCamera = null
            mCameraCaptureSession = null
            surface?.release()
        } catch (e: InterruptedException) {
            Timber.d(e, "[KYC-camera improvement] Interrupted while acquiring close camera")
        } finally {
            mCameraOpenCloseLock.release()
        }
    }

    override fun requestNextImage() {
        requestNext = true
    }

    private val onImageAvailableListener = ImageReader.OnImageAvailableListener { ImageReader ->
        val mImage = ImageReader?.acquireNextImage()
        mImage?.let {
            if (requestNext) {
                requestNext = false
                mPresenter.detect(
                    it,
                    camera_texture_view.width,
                    camera_texture_view.height,
                    overlay_view.getRectWidth().toInt(),
                    overlay_view.getRectHeight().toInt()
                )
            }
            it.close()
        }
    }
}


