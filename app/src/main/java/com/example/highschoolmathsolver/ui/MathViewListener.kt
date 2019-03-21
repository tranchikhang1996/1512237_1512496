package com.example.highschoolmathsolver.ui

import android.view.View
import com.example.highschoolmathsolver.R
import io.github.kexanie.library.MathView

class MathViewListener(private val choosingListener: ChoosingListener?, private val index : Int) : View.OnClickListener {
    override fun onClick(v: View) {
        choosingListener?.choose(index)
    }
}