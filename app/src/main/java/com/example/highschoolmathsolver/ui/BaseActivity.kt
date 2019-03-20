package com.example.highschoolmathsolver.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.di.component.UserComponent
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {
    abstract fun getLayoutId() : Int
    abstract fun setupComponent()

    val mSubscription = CompositeDisposable()

    fun getUserComponent() : UserComponent = AndroidApplication.instance.userComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setupComponent()
    }
    override fun onDestroy() {
        super.onDestroy()
        if(!mSubscription.isDisposed) {
            mSubscription.dispose()
        }
    }
}