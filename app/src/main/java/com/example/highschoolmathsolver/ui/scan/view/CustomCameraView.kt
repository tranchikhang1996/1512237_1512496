package com.example.highschoolmathsolver.ui.scan.view

import android.os.Bundle
import androidx.annotation.StringRes
import com.example.highschoolmathsolver.ui.ILoadDataView

interface CustomCameraView : ILoadDataView {
    fun getData(): Bundle?
    fun initView()
    fun showError(@StringRes resourceId: Int)
    fun initCameraCharacteristics(preferFront: Boolean)
    fun requestNextImage()
    fun solve(expression : String)
    fun saveExpression(expression: String)
}