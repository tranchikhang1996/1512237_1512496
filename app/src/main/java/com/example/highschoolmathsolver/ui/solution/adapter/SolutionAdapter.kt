package com.example.highschoolmathsolver.ui.solution.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.ChoosingListener
import com.example.highschoolmathsolver.ui.MathViewListener
import io.github.kexanie.library.MathView

class SolutionAdapter(private var mDataSet: List<String> = arrayListOf()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() , ChoosingListener{

    override fun choose(index: Int) {
        choosingItem = if(index == choosingItem) -1 else index
        notifyItemChanged(index)
    }

    companion object {
        const val STEP = "Bước"
        const val GRAPH = "DRAW_GRAPH"
        const val SOLUTION_TYPE = 0
        const val SOLUTION_EXPAND_TYPE = 1
        const val GRAPH_TYPE = 2
    }

    private var choosingItem = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutId = when(viewType) {
            SOLUTION_EXPAND_TYPE -> R.layout.solution_expand_holder_layout
            GRAPH_TYPE -> R.layout.graph_holder_layout
            else -> R.layout.solution_holder_layout
        }

        val cardView = LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false) as CardView

        return when (viewType) {
            SOLUTION_EXPAND_TYPE -> SolutionExpandViewHolder(cardView)
            GRAPH_TYPE -> GraphViewHolder(cardView)
            else -> SolutionViewHolder(cardView)
        }
    }

    override fun getItemCount(): Int =  mDataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SolutionExpandViewHolder -> {
                holder.mathView.text = mDataSet[position]
                holder.title.text = ("$STEP $position")
                holder.collapseView.setOnClickListener { choose(position) }
            }
            is GraphViewHolder -> {
                holder.collapseView.setOnClickListener {
                    showGraph(mDataSet[position])
                }
            }
            is SolutionViewHolder -> {
                holder.title.text = ("$STEP $position")
                holder.expand.setOnClickListener(MathViewListener(this, position))
            }
        }
    }

    private fun showGraph(expression : String) {
        expression.apply {
            removePrefix(GRAPH)
            trim()
            // draw graph here
        }
    }

    fun setData(dataSet: List<String>?) {
        dataSet ?: return
        mDataSet = dataSet
        choosingItem = -1
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            mDataSet[position].startsWith(GRAPH) -> GRAPH_TYPE
            position != choosingItem -> SOLUTION_TYPE
            else -> SOLUTION_EXPAND_TYPE
        }
    }

    class SolutionExpandViewHolder(val view: CardView) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val mathView : MathView = view.findViewById(R.id.math_view)
        val collapseView : ImageView = view.findViewById(R.id.collapse)
    }

    class SolutionViewHolder(val view: CardView) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val expand : View = view.findViewById(R.id.frameImage)
    }

    class GraphViewHolder(val view: CardView) : RecyclerView.ViewHolder(view) {
        val collapseView : View = view.findViewById(R.id.frameImage)
    }
}