package com.example.highschoolmathsolver.ui.history.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.model.entity.Expression
import com.example.highschoolmathsolver.presenter.history.HistoryPresenter
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.history.HistoryAdapter
import com.example.highschoolmathsolver.ui.history.HistoryClickListener
import com.example.highschoolmathsolver.ui.history.view.IHistoryView
import com.example.highschoolmathsolver.ui.home.activity.HomeActivity
import com.example.highschoolmathsolver.ui.solution.adapter.MyPagerAdapter
import com.example.highschoolmathsolver.util.AndroidUtils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_history.*
import javax.inject.Inject

/**
 * A simple [BaseFragment] subclass.
 *
 */
class HistoryFragment : BaseFragment(), IHistoryView {

    @Inject
    internal lateinit var presenter: HistoryPresenter
    private val adapter = HistoryAdapter()
    override fun getResLayoutId(): Int = R.layout.fragment_history

    private val listener = object : HistoryClickListener {
        override fun sendData(expression: Expression?) {
            expression?.expression?.let {
                model.execute(it)
                if (activity is HomeActivity) {
                    (activity as HomeActivity).changePage(MyPagerAdapter.SOLUTION)
                }
            }
        }
    }

    override fun setupFragmentComponent() {
        getUserComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnDataUpdate()
        adapter.listener = listener
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        presenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }

    private fun setOnDataUpdate() {
        model.mSavedData.observe(this, Observer { presenter.save(it) })
    }

    override fun refreshData(expressions: MutableList<Expression>) {
        adapter.receiveData(expressions)
    }

    override fun inSertdata(expressions: Expression) {
        adapter.insertData(expressions)
    }

    override fun showLoading() {
        mCompositeDisposable.add(AndroidUtils.runOnUIThreadWithRxjava { history_loading.visibility = View.VISIBLE })
    }

    override fun hideLoading() {
        mCompositeDisposable.add(AndroidUtils.runOnUIThreadWithRxjava { history_loading.visibility = View.GONE })
    }

    override fun showError(message: String) {
        showErrorDialog(message)
    }
}
