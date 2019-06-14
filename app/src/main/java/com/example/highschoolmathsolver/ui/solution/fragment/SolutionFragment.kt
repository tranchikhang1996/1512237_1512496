package com.example.highschoolmathsolver.ui.solution.fragment


import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.dialog.GraphDialog
import com.example.highschoolmathsolver.ui.dialog.SolutionDialogFragment
import com.example.highschoolmathsolver.ui.solution.ShowDetailDialogListener
import com.example.highschoolmathsolver.ui.solution.adapter.MyPagerAdapter
import com.example.highschoolmathsolver.ui.solution.adapter.SolutionAdapter
import kotlinx.android.synthetic.main.fragment_solution.*

/**
 * A simple [BaseFragment] subclass.
 *
 */
class SolutionFragment : BaseFragment(), ShowDetailDialogListener {

    override fun onShowDetailGraph(expression: String) {
        activity?.supportFragmentManager?.let {
            detailGraphDialog = GraphDialog(expression)
            detailGraphDialog?.show(it, "Graph_Dialog")
        }
    }

    override fun onShowDetailStep(expression: String) {
        activity?.supportFragmentManager?.let {
            detailDialog = SolutionDialogFragment(expression)
            detailDialog?.show(it, "Dialog")
        }
    }

    private val mSolutionAdapter = SolutionAdapter()
    private var detailDialog: DialogFragment? = null
    private var detailGraphDialog : DialogFragment? = null

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
        mSolutionAdapter.setShowDetailDialogListener(this)
        bindEvent()
    }

    private fun bindEvent() {
        viewModel.getSolutionData().observe(this, Observer {
            empty_background.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            if(it.isEmpty()) {
                showSolutionFailed()
            } else {
                showResult(it)
            }
        })

        scanToSeeMore.setOnClickListener {
            viewModel.changePage(MyPagerAdapter.CAMERA)
        }
    }

    private fun showSolutionFailed() {
        activity?.let {
            Toast.makeText(it, "Không thể giải bài toán vui lòng thử lại", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showResult(steps: List<String>) {
        mSolutionAdapter.setData(steps)
    }

    override fun onDestroyView() {
        detailDialog?.dismiss()
        detailGraphDialog?.dismiss()
        super.onDestroyView()
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