package com.example.highschoolmathsolver.ui.dialog

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.di.component.UserComponent
import kotlinx.android.synthetic.main.solution_input_layout.input_m
import kotlinx.android.synthetic.main.solution_input_layout.input_m_layout
import kotlinx.android.synthetic.main.solution_input_layout.math_view

class GraphDialog(private val expression: String) : BaseDialogFragment() {
    override val layoutId: Int get() = R.layout.solution_graph_layout

    override fun setupFragmentComponent(userComponent: UserComponent) = userComponent.inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        math_view.text = expression
        bindEvent()
    }

    private fun bindEvent() {
        input_m.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val m = input_m_layout.editText?.text?.toString()
                m?.let { showGraphWithM(it) }
                input_m_layout.clearFocus()
                hideSoftKeyboard(view)
                true
            } else {
                false
            }
        }
    }

    private fun showGraphWithM(m: String) {

    }

}