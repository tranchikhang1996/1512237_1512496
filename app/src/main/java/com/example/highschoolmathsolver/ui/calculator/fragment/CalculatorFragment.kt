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
            val input="y=3x^{3}+2mx^{2}-5x+2"
            viewModel.solve(input)
            viewModel.save(input)
        }
    }
}
