package com.example.highschoolmathsolver

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.highschoolmathsolver.ui.home.activity.HomeActivity
import com.example.highschoolmathsolver.util.DialogHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val mSubscription = CompositeDisposable()

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


    private fun initModel(context : Context) {
        val disposable = Single.create<Boolean> {
            try {
                AndroidApplication.instance.loadConfig()
                it.onSuccess(true)
            } catch (e: IOException) {
                it.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            }, { t ->
                Timber.d(t)
                showErrorAndQuick()
            })
        mSubscription.add(disposable)
    }

    private fun showErrorAndQuick() {
        DialogHelper.showError(activity = this, message = getString(R.string.main_error_message))
    }

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    initModel(mAppContext)
                }
                else -> {
                    super.onManagerConnected(status)
                    showErrorAndQuick()
                }
            }
        }
    }
}
