package com.example.highschoolmathsolver.ui.history.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.model.entity.Expression
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.history.adapater.HistoryAdapter
import com.example.highschoolmathsolver.ui.history.listener.HistoryClickListener
import com.example.highschoolmathsolver.ui.solution.adapter.MyPagerAdapter
import com.example.highschoolmathsolver.viewmodel.SharedModel
import kotlinx.android.synthetic.main.custom_dialog_layout.*
import kotlinx.android.synthetic.main.fragment_history.*

/**
 * A simple [BaseFragment] subclass.
 *
 */
class HistoryFragment : BaseFragment() {
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }
    override val requestLayoutID: Int get() = R.layout.fragment_history
    private val viewModel: SharedModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SharedModel::class.java)
    }

    private val listener = object : HistoryClickListener {
        override fun sendData(expression: Expression?) {
            expression?.expression?.let {
                viewModel.solve(it)
                viewModel.changePage(MyPagerAdapter.SOLUTION)
            }
        }
    }

    override fun setupFragmentComponent() {
        getUserComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.listener = listener
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        bindEvent()
        viewModel.loadHistory()
    }

    private fun bindEvent() {
        viewModel.getHistoryData().observe(this, Observer {
            refreshData(it)
        })

        viewModel.getNewHistoryData().observe(this, Observer {
            insertData(it)
        })

        delete.setOnClickListener {
            val msg = getString(R.string.delete_all_history)
            showConfirmDialog(msg, "Yes", "Cancel", {
                viewModel.deleteAll()
            }, {})
        }
    }

    private fun refreshData(expressions: MutableList<Expression>) {
        empty_background.visibility = if(expressions.isEmpty()) View.VISIBLE else View.GONE
        adapter.receiveData(expressions)
    }

    private fun insertData(expressions: Expression) {
        adapter.insertData(expressions)
    }
}
