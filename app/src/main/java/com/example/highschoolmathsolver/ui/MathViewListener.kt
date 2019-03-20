package com.example.highschoolmathsolver.ui

import android.view.View
import com.example.highschoolmathsolver.R
import io.github.kexanie.library.MathView

class MathViewListener(private val content: String) : View.OnClickListener {
    override fun onClick(v: View) {
        val mathView: MathView = v.findViewById(R.id.math_view)
        mathView.text = content
    }
}