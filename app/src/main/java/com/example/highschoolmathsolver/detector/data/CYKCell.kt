package com.example.highschoolmathsolver.detector.data

import org.opencv.core.Rect


class CYKCell(
    val candidates : HashMap<String, Candidate>,
    val B : CYKCell?,
    val C : CYKCell?,
    val region : Rect,
    val key : String = "",
    val childIds : MutableList<Long> = arrayListOf())