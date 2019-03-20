package com.example.highschoolmathsolver.presenter.history

import com.example.highschoolmathsolver.extentions.applySchedulers
import com.example.highschoolmathsolver.model.repository.IDatabaseRepository
import com.example.highschoolmathsolver.presenter.AbstractPresenter
import com.example.highschoolmathsolver.ui.history.HistoryAdapter
import com.example.highschoolmathsolver.ui.history.view.IHistoryView
import com.example.highschoolmathsolver.util.AndroidUtils
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class HistoryPresenter @Inject constructor(val dataRepository: IDatabaseRepository) :
    AbstractPresenter<IHistoryView>() {

    fun save(expression: String) {
        val exp = AndroidUtils.stringToExpression(expression)
        mView?.inSertdata(exp)
        val disposable = dataRepository.save(exp)
            .subscribeOn(Schedulers.io())
            .subscribe({Timber.d("success")}, { Timber.d(it) })
        mSubscription.add(disposable)
    }

    fun fetchData() {
        mView?.showLoading()
        val disposable = dataRepository.getAllExpressions()
            .doFinally { mView?.hideLoading() }
            .take(1)
            .map { it.toMutableList() }
            .applySchedulers()
            .subscribe({ mView?.refreshData(it) }, { Timber.d(it) })
        mSubscription.add(disposable)
    }
}