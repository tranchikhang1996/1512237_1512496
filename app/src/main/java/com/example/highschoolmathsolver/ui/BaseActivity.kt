package com.example.highschoolmathsolver.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.viewmodel.ViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import android.view.WindowManager
import android.os.Build
import android.annotation.TargetApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.viewmodel.SharedModel


abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutId: Int
    abstract fun setupComponent()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    internal val mSubscription = CompositeDisposable()

    val viewModel: SharedModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SharedModel::class.java)
    }

    fun getUserComponent(): UserComponent = AndroidApplication.instance.userComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarBackground()
        setContentView(layoutId)
        setupComponent()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            window.setBackgroundDrawableResource(R.drawable.primary_background)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mSubscription.isDisposed) {
            mSubscription.dispose()
        }
    }
}