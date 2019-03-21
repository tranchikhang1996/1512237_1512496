package com.example.highschoolmathsolver.ui.calculator.fragment


import android.os.Bundle
import android.view.View
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.presenter.calculator.CalculatorPresenter
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.calculator.view.ICalculatorView
import com.example.highschoolmathsolver.util.DialogHelper
import kotlinx.android.synthetic.main.fragment_calculator.*
import javax.inject.Inject

/**
 * A simple [BaseFragment] subclass.
 *
 */
class CalculatorFragment : BaseFragment(), ICalculatorView {

    @Inject
    internal lateinit var presenter: CalculatorPresenter

    override fun getResLayoutId(): Int = R.layout.fragment_calculator

    override fun setupFragmentComponent() {
        getUserComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        execute()
    }

    private fun execute() {
        button.setOnClickListener {
            val  text = "This come from string. You can insert inline formula:" +
                    " \\(ax^2 + bx + c = 0\\) " +
                    "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$"
            model.execute("test")
            model.saveData(text)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }
}
