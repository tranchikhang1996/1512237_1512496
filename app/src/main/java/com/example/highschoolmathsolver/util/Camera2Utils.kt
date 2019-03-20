package com.example.highschoolmathsolver.util

import android.annotation.TargetApi
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Size
import android.view.Surface
import timber.log.Timber
import java.lang.Long

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Camera2Utils {
    companion object {
        const val DEGREE_90 = 90
        const val DEGREE_180 = 180
        const val DEGREE_0 = 0
        const val DEGREE_270 = 270
        const val DEGREE_360 = 360
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        val comparator = Comparator<Size>()
        { lhs, rhs -> Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height) }

        fun getListCameraIDs(cameraManager: CameraManager?): Array<String> = when (cameraManager) {
            null -> emptyArray()
            else -> try {
                cameraManager.cameraIdList
            } catch (e: CameraAccessException) {
                Timber.d(e)
                emptyArray<String>()
            }
        }

        fun getOrientation(rotation: Int): Int = when (rotation) {
            Surface.ROTATION_0 -> DEGREE_90
            Surface.ROTATION_90 -> DEGREE_0
            Surface.ROTATION_180 -> DEGREE_270
            Surface.ROTATION_270 -> DEGREE_180
            else -> DEGREE_0
        }

        fun chooseOptimalSize(
            choices: Array<Size>, textureViewWidth: Int,
            textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size
        ): Size {
            val (bigEnough, notBigEnough) = choices.asSequence()
                .filter { it.width < maxWidth && it.height < maxHeight && it.height == it.width * aspectRatio.height / aspectRatio.width }
                .partition { it.width >= textureViewWidth && it.height >= textureViewHeight }
            return when {
                bigEnough.isNotEmpty() -> bigEnough.minWith(comparator)!!
                notBigEnough.isNotEmpty() -> notBigEnough.maxWith(comparator)!!
                else -> Size(textureViewWidth, textureViewHeight)
            }
        }

        fun getCameraId(cameraManager: CameraManager?, preferFront: Boolean): Pair<String, Int> {
            cameraManager ?: return Pair("", 0)
            try {
                val cameraIDs = cameraManager.cameraIdList
                for ((index, cameraId) in cameraIDs.withIndex()) {
                    val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                    val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                    if (preferFront && facing == CameraCharacteristics.LENS_FACING_FRONT || !preferFront && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        return Pair(cameraId, index)
                    }
                }
            } catch (e: CameraAccessException) {
                Timber.d(e)
            }
            return Pair("", 0)
        }
    }
}