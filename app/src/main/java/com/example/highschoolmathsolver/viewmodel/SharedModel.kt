package com.example.highschoolmathsolver.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedModel : ViewModel() {
    val mExecutedData = MutableLiveData<String>()
    val mSavedData = MutableLiveData<String>()
    var currentPage : Int = 0

    fun execute(expression : String) {
        mExecutedData.value = expression
    }

    fun saveData(savedData : String) {
        mSavedData.value = savedData
    }

}