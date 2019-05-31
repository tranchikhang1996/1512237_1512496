package com.example.highschoolmathsolver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.highschoolmathsolver.extentions.applySchedulers
import com.example.highschoolmathsolver.mathengine.ISolveEngine
import com.example.highschoolmathsolver.model.entity.Expression
import com.example.highschoolmathsolver.model.repository.IDatabaseRepository
import com.example.highschoolmathsolver.ui.solution.adapter.MyPagerAdapter
import com.example.highschoolmathsolver.util.AndroidUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.opencv.core.Rect
import timber.log.Timber
import javax.inject.Inject

class SharedModel @Inject constructor(
    private val roomRepository: IDatabaseRepository,
    private val solver: ISolveEngine
) :
    ViewModel() {
    private val solutionData: MutableLiveData<List<String>> = MutableLiveData()
    private val historyData: MutableLiveData<MutableList<Expression>> = MutableLiveData()
    private val newHistoryData: MutableLiveData<Expression> = MutableLiveData()
    private val currentPage: MutableLiveData<Int> = MutableLiveData()
    private val subscriptions = CompositeDisposable()
    private val frameSize : MutableLiveData<Rect> = MutableLiveData(Rect())

    fun changePage(page: Int) {
        currentPage.value = page
    }

    fun changeFrameSize(rect : Rect) {
        frameSize.value = rect
    }

    fun getFrameSize() : LiveData<Rect> = frameSize

    fun getCurrentPage(): LiveData<Int> = currentPage

    fun getSolutionData(): LiveData<List<String>> = solutionData

    fun getHistoryData(): LiveData<MutableList<Expression>> = historyData

    fun getNewHistoryData(): LiveData<Expression> = newHistoryData

    fun save(expression: String) {
        val data = AndroidUtils.stringToExpression(expression)
        historyData.value?.add(data)
        newHistoryData.postValue(data)
        val disposable = roomRepository.save(data)
            .subscribeOn(Schedulers.io())
            .subscribe()
        subscriptions.add(disposable)
    }

    fun solve(expression: String) {
        val disposable = solver.solve(expression)
            .applySchedulers()
            .subscribe({
                solutionData.value = it
                changePage(MyPagerAdapter.SOLUTION)
            }, Timber::d)
        subscriptions.add(disposable)
    }

    fun deleteAll() {
        val disposable = roomRepository.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ historyData.value = arrayListOf() }, Timber::d)
        subscriptions.add(disposable)
    }

    fun loadHistory() {
        val disposable = roomRepository.getAllExpressions()
            .applySchedulers()
            .subscribe(historyData::setValue, Timber::d)
        subscriptions.add(disposable)
    }

    private fun clearTasks() {
        subscriptions.clear()
    }

    override fun onCleared() {
        clearTasks()
        super.onCleared()
    }
}