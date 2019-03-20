package com.example.highschoolmathsolver.ui.solution.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.MathViewListener
import com.jjoe64.graphview.GraphView
import io.github.kexanie.library.MathView

class SolutionAdapter(private var mDataSet: List<String> = arrayListOf()) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    companion object {
        const val STEP = "Bước"
        const val GRAPH = "DRAW_GRAPH"
        const val SOLUTION_TYPE = 1
        const val GRAPH_TYPE = 2
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val layoutId = if (viewType == SOLUTION_TYPE) R.layout.solution_holder_layout else R.layout.graph_holder_layout
        val cardView = LayoutInflater.from(viewGroup.context).inflate(
            layoutId,
            viewGroup,
            false
        ) as androidx.cardview.widget.CardView
        return if (viewType == SOLUTION_TYPE) SolutionViewHolder(
            cardView
        ) else GraphViewHolder(cardView)
    }

    override fun getItemCount(): Int =  mDataSet.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (holder is SolutionViewHolder) {
            val content = mDataSet[position]
            holder.title.text = ("$STEP $position")
            holder.mathView.text = ""
            holder.view.setOnClickListener(MathViewListener(content))
        }

        if (holder is GraphViewHolder) {
            drawGraph(holder.graphView, mDataSet[position])
        }

    }

    private fun drawGraph(graphView: GraphView, expression : String) {
        expression.apply {
            removePrefix(GRAPH)
            trim()
            // draw graph here
        }
    }

    fun setData(dataSet: List<String>?) {
        dataSet ?: return
        mDataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mDataSet[position].startsWith(GRAPH)) GRAPH_TYPE else SOLUTION_TYPE
    }

    class SolutionViewHolder(val view: androidx.cardview.widget.CardView) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val mathView : MathView = view.findViewById(R.id.math_view)
    }

    class GraphViewHolder(val view: androidx.cardview.widget.CardView) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val graphView: GraphView = view.findViewById(R.id.graph_view)
    }
}