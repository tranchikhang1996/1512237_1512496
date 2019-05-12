package com.example.highschoolmathsolver.ui.scan.view

import android.Manifest
import android.view.SurfaceHolder
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.RequiresPermission
import android.view.SurfaceView
import android.view.ViewGroup
import timber.log.Timber
import java.io.IOException
import java.lang.NullPointerException
import java.lang.RuntimeException


class CameraSourcePreview(mContext: Context, attrs: AttributeSet) : ViewGroup(mContext, attrs) {
    private val mSurfaceView: SurfaceView
    private var mStartRequested: Boolean = false
    private var mSurfaceAvailable: Boolean = false
    private var mCameraSource: CameraSource? = null

    init {
        mStartRequested = false
        mSurfaceAvailable = false

        mSurfaceView = SurfaceView(mContext)
        mSurfaceView.holder.addCallback(SurfaceCallback())
        addView(mSurfaceView)
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @Throws(IOException::class, SecurityException::class)
    fun start(cameraSource: CameraSource?) {
        if (cameraSource == null) {
            stop()
        }

        mCameraSource = cameraSource

        mCameraSource?.run {
            mStartRequested = true
            startIfReady()
        }
    }

    private fun stop() {
        mCameraSource?.stop()
    }

    fun release() {
        try {
            mCameraSource?.release()
        } catch (nep: NullPointerException) {
            Timber.d("Camera source is already released or null")
        }
        mCameraSource = null
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @Throws(IOException::class, SecurityException::class)
    private fun startIfReady() {
        if (mStartRequested && mSurfaceAvailable) {
            mCameraSource?.start(mSurfaceView.holder)
            mStartRequested = false
        }
    }

    private inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(surface: SurfaceHolder) {
            mSurfaceAvailable = true
            try {
                startIfReady()
            } catch (se: SecurityException) {
                Timber.e(se, "Do not have permission to start the camera")
            } catch (e: IOException) {
                Timber.e(e, "Could not start camera source.")
            } catch (e : NullPointerException) {
                Timber.d(e,"Could not start camera source and not have permission")
            }

        }

        override fun surfaceDestroyed(surface: SurfaceHolder) {
            mSurfaceAvailable = false
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) = Unit
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val layoutWidth = right - left
        val layoutHeight = bottom - top

        // for fullscreen camera preview
        for (i in 0 until childCount) {
            getChildAt(i).layout(0, 0, layoutWidth, layoutHeight)
        }

        try {
            startIfReady()
        } catch (se: SecurityException) {
            Timber.e(se, "Do not have permission to start the camera")
        } catch (e: IOException) {
            Timber.e(e, "Could not start camera source.")
        } catch (nep : NullPointerException) {
            Timber.d(nep,"Could not start camera source and not have permission")
        } catch (re : RuntimeException) {
            Timber.d(re,"Could not start camera source.")
        }
    }
}