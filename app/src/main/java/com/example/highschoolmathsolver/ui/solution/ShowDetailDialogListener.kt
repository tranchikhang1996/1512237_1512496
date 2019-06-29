package com.example.highschoolmathsolver.ui.solution

interface ShowDetailDialogListener {
    fun onShowDetailStep(expression : String)
    fun onShowDetailGraph(expression : String, shouldShowSeekBar : Boolean)
}