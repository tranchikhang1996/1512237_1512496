package com.example.highschoolmathsolver.detector.data

import org.opencv.core.Rect

class CombineRegion(
    val id: Long,
    val region: Rect,
    val parent: Pair<CombineRegion, CombineRegion>?,
    val childIds : List<Long>,
    val childRegions : List<Rect>
)