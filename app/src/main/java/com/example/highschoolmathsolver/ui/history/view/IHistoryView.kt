package com.example.highschoolmathsolver.ui.history.view

import com.example.highschoolmathsolver.model.entity.Expression
import com.example.highschoolmathsolver.ui.ILoadDataView

interface IHistoryView : ILoadDataView{
    fun refreshData(expressions : MutableList<Expression>)

    fun inSertdata(expressions : Expression)
}