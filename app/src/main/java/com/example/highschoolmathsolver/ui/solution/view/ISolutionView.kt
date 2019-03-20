package com.example.highschoolmathsolver.ui.solution.view

import com.example.highschoolmathsolver.ui.ILoadDataView

interface ISolutionView : ILoadDataView{
    fun showResult(steps : List<String>)
}