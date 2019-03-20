package com.example.highschoolmathsolver.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.model.entity.Expression
import io.github.kexanie.library.MathView

class HistoryAdapter(var mDataSet : MutableList<Expression> = arrayListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: HistoryClickListener? = null

    override fun getItemCount(): Int = mDataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is HistoryViewHolder) {
            return
        }
        val item = mDataSet[position]
        holder.title.text = item.date
        holder.mathView.text = item.expression
        holder.cardView.setOnClickListener {
            listener?.sendData(mDataSet[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cardView =
            LayoutInflater.from(parent.context).inflate(R.layout.history_holder_layout, parent, false) as CardView
        return HistoryViewHolder(cardView)
    }

    fun receiveData(expressions : MutableList<Expression>) {
        mDataSet = expressions
        notifyDataSetChanged()
    }

    fun insertData(expressions: Expression) {
        mDataSet.add(expressions)
        notifyItemInserted(mDataSet.size-1)
    }

    class HistoryViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        val title: TextView = cardView.findViewById(R.id.title)
        val mathView : MathView = cardView.findViewById(R.id.math_view)
    }
}
