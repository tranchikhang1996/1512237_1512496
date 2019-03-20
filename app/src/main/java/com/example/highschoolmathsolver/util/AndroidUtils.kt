package com.example.highschoolmathsolver.util

import com.example.highschoolmathsolver.model.entity.Expression
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AndroidUtils {
    companion object {
        fun runOnUIThreadWithRxjava(runnable: () -> Unit): Disposable = Observable.just(runnable)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it() }, { Timber.d(it, "[Utils] runOnUIThreadWithRxjava error") })

        fun runWithDelay(runnable: () -> Unit, delay: Long): Disposable =
            Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ runnable() }, { Timber.d(it, "[Utils] runOnUIThreadWithRxjavaDelay error") })

        fun stringToExpression(exp : String) : Expression{
            val sDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            return Expression(expression = exp, date = sDate)
        }
    }

}