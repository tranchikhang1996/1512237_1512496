package com.example.highschoolmathsolver.ui.dialog

import android.os.Bundle
import android.view.View
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.util.GraphUtils
import com.example.highschoolmathsolver.util.GraphUtils.Companion.showRootGraph
import com.example.highschoolmathsolver.util.MathUtils
import kotlinx.android.synthetic.main.solution_graph_layout.*
import kotlinx.android.synthetic.main.solution_input_layout.math_view
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar


class GraphDialog(private val expression: String, private val shouldShowSeekBar : Boolean = true) : BaseDialogFragment() {

    override val layoutId: Int get() = R.layout.solution_graph_layout

    override fun setupFragmentComponent(userComponent: UserComponent) = userComponent.inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!shouldShowSeekBar) {
            cur_m.visibility = View.GONE
            m_seekbar.visibility = View.GONE
        }
        math_view.text = "<h3>" + MathUtils.trimToKaTeX(expression) + "</h3>"
        showRootGraph(graph, true)
        if(!shouldShowSeekBar) {
            showGraph(expression)
        }
        m_seekbar.setOnProgressChangeListener(onProgressChangeListener)
    }

    private fun showGraphWithM(m: Double) {
        val nonMLatexExp = if (shouldShowSeekBar) MathUtils.removeM(m, expression) else expression
        showGraph(nonMLatexExp)
    }

    private fun showGraph(exp: String) {
        GraphUtils.drawGraph(graph, exp)
    }

    private val onProgressChangeListener = object : DiscreteSeekBar.OnProgressChangeListener {
        override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
            val m = (seekBar.progress - 100).toDouble() / 10
            cur_m.text = String.format("m = %.2f", m)
            graph.removeAllSeries()
            showGraphWithM(m)
            val format = m.toString()
            seekBar.setIndicatorFormatter(format)
        }

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
            val format = ((seekBar.progress - 100).toDouble() / 10).toString()
            seekBar.setIndicatorFormatter(format)
        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) = Unit
    }
}