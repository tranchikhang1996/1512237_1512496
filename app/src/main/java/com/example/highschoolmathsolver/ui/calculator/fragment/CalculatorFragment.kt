package com.example.highschoolmathsolver.ui.calculator.fragment


import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.viewmodel.SharedModel
import kotlinx.android.synthetic.main.fragment_calculator.*

/**
 * A simple [BaseFragment] subclass.
 *
 */
class CalculatorFragment : BaseFragment() {

    override val requestLayoutID: Int get() = R.layout.fragment_calculator

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(SharedModel::class.java) }

    override fun setupFragmentComponent() = getUserComponent().inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        execute()
    }
    private fun execute() {
        button.setOnClickListener {
            val constan="y=3x^{3}+2mx^{2}-5x+2"
            val equation="x^{4}-2x^{2}=0"
            val inequation="x^{4}-2x^{2}\\leqslant0"
            val trigonometric="2\\sin^{2}x+\\sin2x+2\\cos^{2}x=0"
            val limit="\\lim_{x\\to\\infty}\\frac{2x^{2}-x+3}{x^{2}+1}"
            val integral="\\int_{0}^{2}\\frac{x^{3}}{ (x+1)^{2}+1}dx"
            viewModel.solve(integral)
            viewModel.save(integral)
        }
    }
}
