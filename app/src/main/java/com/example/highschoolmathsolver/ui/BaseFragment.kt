package com.example.highschoolmathsolver.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.viewmodel.SharedModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : androidx.fragment.app.Fragment() {
    val TAG = javaClass.simpleName
    internal val mCompositeDisposable = CompositeDisposable()
    lateinit var model : SharedModel
    internal abstract fun setupFragmentComponent()

    internal abstract fun getResLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getResLayoutId(), container, false)
        setupFragmentComponent()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = activity?.run { ViewModelProviders.of(this).get(SharedModel::class.java) }
            ?: throw throw Exception("Invalid Activity")
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    fun getUserComponent() : UserComponent = AndroidApplication.instance.userComponent
}