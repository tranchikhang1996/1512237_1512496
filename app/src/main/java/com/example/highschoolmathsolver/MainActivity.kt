package com.example.highschoolmathsolver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.highschoolmathsolver.ui.home.activity.HomeActivity
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {

    init {
        if (!OpenCVLoader.initDebug())
            Timber.d("Unable to load OpenCV")
        else
            Timber.d("OpenCV loaded")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    try {
                        AndroidApplication.instance.loadLabelTable()
                        AndroidApplication.instance.loadMlp()
                        val intent = Intent(mAppContext, HomeActivity::class.java)
                        startActivity(intent)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}
