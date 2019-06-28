package com.example.highschoolmathsolver.ui.dialog

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.solution.adapter.SolutionAdapter
import kotlinx.android.synthetic.main.solution_input_layout.*
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.util.MathUtils


class SolutionDialogFragment(private val expression: String) : BaseDialogFragment() {

    override val layoutId: Int get() = R.layout.solution_input_layout

    override fun setupFragmentComponent(userComponent: UserComponent) = userComponent.inject(this)

    private var adapter = SolutionAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        math_view.text = "<h3>" +MathUtils.trimToKaTeX(expression)+"</h3>"
        viewModel.clearSolutionDetail()
        bindEvent()
    }

    private fun bindEvent() {
        input_m.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val m = input_m_layout.editText?.text?.toString()
                m?.let{ solveWithM(it) }
                input_m_layout.clearFocus()
                hideSoftKeyboard(view)
                true
            } else {
                false
            }
        }
        viewModel.getSolutionDetailData().observe(this, Observer {
            adapter.setData(it)
        })
    }

    private fun solveWithM(m : String) {
        var valueOfM=m.toDouble()
        val nonMExpression = MathUtils.removeM(valueOfM,expression)
        viewModel.solveDetail(nonMExpression)
    }
}