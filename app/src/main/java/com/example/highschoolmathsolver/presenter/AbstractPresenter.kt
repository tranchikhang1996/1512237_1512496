package com.example.highschoolmathsolver.presenter

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class AbstractPresenter<View> : IPresenter<View>  {
    internal var mSubscription = CompositeDisposable()
    var mView : View? = null

    @CallSuper
    override fun attachView(view: View) {
        Timber.d("attachView: %s", view)
        mView = view
    }

    @CallSuper
    override fun detachView() {
        Timber.d("detachView: %s", mView)
        mSubscription.clear()
        mView = null
    }

    @CallSuper
    override fun destroy() {
        Timber.d("destroy is called")
        detachView()
    }

    override fun resume() {}

    override fun pause() {}
}