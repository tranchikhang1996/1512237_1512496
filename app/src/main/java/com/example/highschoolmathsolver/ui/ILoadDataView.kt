package com.example.highschoolmathsolver.ui

interface ILoadDataView {

    fun showLoading()

    fun hideLoading()

    fun showError(message : String)
}