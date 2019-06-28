package com.example.highschoolmathsolver.detector.data.rule

import com.example.highschoolmathsolver.defination.MergeTag
import com.example.highschoolmathsolver.defination.SpatialRelationship
import com.example.highschoolmathsolver.detector.data.Hypothesis
import com.example.highschoolmathsolver.extentions.t
import org.opencv.core.Rect

class BinaryRule(
    val A: String,
    val B: String,
    val C: String,
    val prob: Double,
    val sparel: SpatialRelationship,
    val template: String,
    private val mergeTag: MergeTag
) : Rule {
    override fun mergeRegions(region : Rect, b: Hypothesis, c: Hypothesis): Hypothesis {
        val hypothesis = Hypothesis(region)
        when (mergeTag) {
            MergeTag.A -> {
                hypothesis.lcen = b.lcen
                hypothesis.rcen = b.rcen
            }
            MergeTag.B -> {
                hypothesis.lcen = c.lcen
                hypothesis.rcen = c.rcen
            }
            MergeTag.C -> {
                hypothesis.lcen = (b.region.y + b.region.t).toDouble() / 2
                hypothesis.rcen = (c.region.y + c.region.t).toDouble() / 2
            }
            MergeTag.M -> {
                hypothesis.lcen = (b.lcen + c.lcen) / 2
                hypothesis.rcen = (b.rcen + c.rcen) / 2
            }
        }
        return hypothesis
    }
}