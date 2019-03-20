package com.example.highschoolmathsolver.ui.solution.fragment


import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.presenter.solution.SolutionPresenter
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.solution.adapter.SolutionAdapter
import com.example.highschoolmathsolver.ui.solution.view.ISolutionView
import com.example.highschoolmathsolver.util.DialogHelper
import kotlinx.android.synthetic.main.fragment_solution.*
import javax.inject.Inject

// examples with mathview

//        math_view.setEngine(MathView.Engine.KATEX)
//        val tex = "This come from string. You can insert inline formula:" +
//                " \\(ax^2 + bx + c = 0\\) " +
//                "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$"
//        math_view.text = tex

/**
 * A simple [BaseFragment] subclass.
 *
 */
class SolutionFragment : BaseFragment(), ISolutionView {

    @Inject
    lateinit var mPresenter: SolutionPresenter
    private val mSolutionAdapter = SolutionAdapter()

    override fun setupFragmentComponent() {
        getUserComponent().inject(this)
    }

    override fun getResLayoutId(): Int = R.layout.fragment_solution

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        setOnDataUpdate()
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = mSolutionAdapter
    }

    private fun setOnDataUpdate() {
        model.mExecutedData.observe(this, Observer { mPresenter.solve(it) })
    }

    override fun onResume() {
        super.onResume()
        mPresenter.resume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.destroy()
    }

    override fun showResult(steps: List<String>) {
        mSolutionAdapter.setData(steps)
    }

    override fun showLoading() {
        DialogHelper.showLoading(activity)
    }

    override fun hideLoading() {
        DialogHelper.hideLoading()
    }

    override fun showError(message: String) {
        DialogHelper.showError(activity, message)
    }
}
