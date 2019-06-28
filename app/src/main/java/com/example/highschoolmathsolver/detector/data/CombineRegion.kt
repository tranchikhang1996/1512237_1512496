package com.example.highschoolmathsolver.detector.data

import org.opencv.core.Point
import org.opencv.core.Rect

class CombineRegion(
    val key : String,
    val region: Pair<Rect, List<Point>>,
    val childs : MutableList<Long>,
    val childRegions : MutableList<Rect>
)