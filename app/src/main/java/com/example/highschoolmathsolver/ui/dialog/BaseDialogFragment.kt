package com.example.highschoolmathsolver.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.viewmodel.SharedModel
import com.example.highschoolmathsolver.viewmodel.ViewModelFactory
import javax.inject.Inject

abstract class BaseDialogFragment : DialogFragment() {
    @Inject
    lateinit var factory: ViewModelFactory
    internal val viewModel: SharedModel by lazy { ViewModelProviders.of(this, factory).get(SharedModel::class.java) }
    abstract val layoutId : Int
    abstract fun setupFragmentComponent(userComponent: UserComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentComponent(AndroidApplication.instance.userComponent)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    internal fun hideSoftKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}