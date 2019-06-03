package com.example.highschoolmathsolver.ui.history.adapater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.model.entity.Expression
import com.example.highschoolmathsolver.ui.ChoosingListener
import com.example.highschoolmathsolver.ui.MathViewListener
import com.example.highschoolmathsolver.ui.history.listener.HistoryClickListener
import com.example.highschoolmathsolver.util.MathUtils
import com.google.android.material.button.MaterialButton
import io.github.kexanie.library.MathView

class HistoryAdapter(private var mDataSet : MutableList<Expression> = arrayListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ChoosingListener {

    private var choosingItem = -1
    override fun choose(index: Int) {
        choosingItem = if( choosingItem == index ) -1 else index
        notifyItemChanged(index)
    }

    companion object {
        const val HISTORY_TYPE = 0
        const val HISTORY_EXPAND_TYPE = 1
    }

    var listener: HistoryClickListener? = null

    override fun getItemCount(): Int = mDataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mDataSet[position]
        when(holder) {
            is HistoryExpandViewHolder -> {
//                holder.title.text = item.date
                holder.mathView.text = "<h3>"+MathUtils.trimToKaTeX(item.expression)+"</h3>"
                holder.collapseView.setOnClickListener { choose(position) }
                holder.seeSolution.setOnClickListener {
                    listener?.sendData(item)
                }
            }

            is HistoryViewHolder -> {
                holder.title.text = item.date
                holder.expand.setOnClickListener(MathViewListener(this, position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = when (viewType) {
            HISTORY_EXPAND_TYPE -> R.layout.history_expand_holder_layout
            else -> R.layout.history_holder_layout
        }
        val cardView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false) as CardView
        return when (viewType) {
            HISTORY_EXPAND_TYPE -> HistoryExpandViewHolder(cardView)
            else -> HistoryViewHolder(cardView)
        }
    }

    fun receiveData(expressions : MutableList<Expression>) {
        mDataSet = expressions
        choosingItem = -1
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if(choosingItem == position) HISTORY_EXPAND_TYPE else HISTORY_TYPE
    }

    fun insertData(expressions: Expression) {
        mDataSet.add(expressions)
        notifyItemInserted(mDataSet.size-1)
    }

    class HistoryExpandViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
//        val title: TextView = cardView.findViewById(R.id.title)
        val mathView : MathView = cardView.findViewById(R.id.math_view)
        val collapseView : ImageView = cardView.findViewById(R.id.collapse)
        val seeSolution : MaterialButton = cardView.findViewById(R.id.see_solution)
    }

    class HistoryViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        val title: TextView = cardView.findViewById(R.id.title)
        val expand : View = cardView.findViewById<View>(R.id.frameImage)
    }
}
