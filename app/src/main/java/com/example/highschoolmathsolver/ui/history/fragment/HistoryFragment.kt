package com.example.highschoolmathsolver.ui.history.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.model.entity.Expression
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.history.RecyclerItemTouchHelper
import com.example.highschoolmathsolver.ui.history.adapater.HistoryAdapter
import com.example.highschoolmathsolver.ui.history.listener.HistoryClickListener
import kotlinx.android.synthetic.main.fragment_history.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import timber.log.Timber


/**
 * A simple [BaseFragment] subclass.
 *
 */
class HistoryFragment : BaseFragment(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }
    override val requestLayoutID: Int get() = R.layout.fragment_history

    private val listener = object : HistoryClickListener {
        override fun sendData(expression: Expression?) {
            expression?.expression?.let {
                viewModel.gotoSolution(it)
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
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler_view.adapter = adapter
        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view)
        bindEvent()
        viewModel.loadHistory()
    }

    private fun bindEvent() {
        viewModel.getHistoryData().observe(this, Observer {
            refreshData(it)
        })

        viewModel.getNewHistoryData().observe(this, Observer {
            it ?: return@Observer
            insertData(it)
        })

        delete.setOnClickListener {
            viewModel.getHistoryData().value?.run {
                if (this.isEmpty()) {
                    return@run
                }
                val msg = getString(R.string.delete_all_history)
                showConfirmDialog(msg, "Yes", "Cancel", {
                    viewModel.deleteAll()
                }, {})
            }
        }
    }

    private fun refreshData(expressions: MutableList<Expression>) {
        empty_background.visibility = if (expressions.isEmpty()) View.VISIBLE else View.GONE
        adapter.receiveData(expressions)
    }

    private fun insertData(expressions: Expression) {
        empty_background.visibility = View.GONE
        adapter.insertData(expressions)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val shouldCloseExpanded = viewHolder is HistoryAdapter.HistoryExpandViewHolder
        val expression = adapter.getDataAt(position)
        val disposable = viewModel.deleteItem(expression).subscribe(
            {
                adapter.removeItem(position, shouldCloseExpanded)
                if (viewModel.getHistoryData().value.isNullOrEmpty()) {
                    empty_background.visibility = View.VISIBLE
                }
            },
            {
                Timber.d("delete error!")
            })
        mSubscriptions.add(disposable)
    }
}
