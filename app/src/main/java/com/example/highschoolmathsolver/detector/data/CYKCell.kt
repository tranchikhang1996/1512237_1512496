package com.example.highschoolmathsolver.detector.data

import org.opencv.core.Rect


class CYKCell(
    val candidates : HashMap<String, Candidate>,
    val B : CYKCell?,
    val C : CYKCell?,
    val bId : Long,
    val cId : Long,
    val region : Rect,
    var id : Long = 0,
    val childIds : MutableList<Long> = arrayListOf()) {

    companion object {
        var currentId: Long = 0L
            get() {
                return field++
            }
    }
}