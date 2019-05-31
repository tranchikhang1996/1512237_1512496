package com.example.highschoolmathsolver.ui.solution.fragment


import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.solution.adapter.MyPagerAdapter
import com.example.highschoolmathsolver.ui.solution.adapter.SolutionAdapter
import kotlinx.android.synthetic.main.fragment_solution.*

/**
 * A simple [BaseFragment] subclass.
 *
 */
class SolutionFragment : BaseFragment() {

    private val mSolutionAdapter = SolutionAdapter()

    override val requestLayoutID: Int get() = R.layout.fragment_solution
    override fun setupFragmentComponent() = getUserComponent().inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = mSolutionAdapter
        bindEvent()
    }

    private fun bindEvent() {
        viewModel.getSolutionData().observe(this, Observer {
            empty_background.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            showResult(it)
        })

        scanToSeeMore.setOnClickListener {
            viewModel.changePage(MyPagerAdapter.CAMERA)
        }
    }

    private fun showResult(steps: List<String>) {
        mSolutionAdapter.setData(steps)
    }
}

/**
// examples with mathview

//        math_view.setEngine(MathView.Engine.KATEX)
//        val tex = "This come from string. You can insert inline formula:" +
//                " \\(ax^2 + bx + c = 0\\) " +
//                "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$"
//        math_view.text = tex

 **/