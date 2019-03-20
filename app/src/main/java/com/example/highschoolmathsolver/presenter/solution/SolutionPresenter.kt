package com.example.highschoolmathsolver.presenter.solution

import com.example.highschoolmathsolver.extentions.applySchedulers
import com.example.highschoolmathsolver.mathengine.ISolveEngine
import com.example.highschoolmathsolver.presenter.AbstractPresenter
import com.example.highschoolmathsolver.ui.solution.adapter.SolutionAdapter
import com.example.highschoolmathsolver.ui.solution.view.ISolutionView
import timber.log.Timber
import javax.inject.Inject

class SolutionPresenter @Inject constructor(private val solveEngine: ISolveEngine) :
    AbstractPresenter<ISolutionView>() {

    fun solve(expression: String?) {
        expression?.let {
            val disposable = solveEngine.solve(it)
                .applySchedulers()
                .subscribe({ data -> mView?.showResult(data) }, { throwable -> Timber.d(throwable) })
            mSubscription.add(disposable)
        }
    }

}