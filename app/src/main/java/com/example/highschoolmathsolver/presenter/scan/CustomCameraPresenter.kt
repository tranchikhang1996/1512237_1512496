package com.example.highschoolmathsolver.presenter.scan

import android.media.Image
import com.example.highschoolmathsolver.detector.IDetector
import com.example.highschoolmathsolver.extentions.applySchedulers
import com.example.highschoolmathsolver.presenter.AbstractPresenter
import com.example.highschoolmathsolver.ui.scan.view.CustomCameraView
import timber.log.Timber
import javax.inject.Inject

class CustomCameraPresenter @Inject constructor(val detector : IDetector): AbstractPresenter<CustomCameraView>() {

    override fun attachView(view: CustomCameraView) {
        super.attachView(view)
        mView?.initCameraCharacteristics(false)
    }

    fun detect(image : Image, previewWidth : Int, previewHeight : Int, rectWidth : Int, rectHeight : Int) {
        val disposable = detector.detect(image, previewWidth, previewHeight, rectWidth, rectHeight)
            .applySchedulers()
            .subscribe({exp -> handleResult(exp)},{handleError()})
        mSubscription.add(disposable)
    }

    private fun handleResult(expression: String) {
        mView?.let {
            if(expression.isEmpty()) {
                it.requestNextImage()
                return
            }
            it.saveExpression(expression)
            it.solve(expression)
        }
    }

    private fun handleError() {
        Timber.d("error")
    }
}