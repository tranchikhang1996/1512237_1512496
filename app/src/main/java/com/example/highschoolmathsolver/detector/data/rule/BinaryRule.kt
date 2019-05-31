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

//    override fun mergeRegions(region : Rect, b: Hypothesis, c: Hypothesis): Hypothesis {
//        val hypothesis = Hypothesis(region)
//        when(sparel) {
//            SpatialRelationship.HORIZONTAL -> {
//                hypothesis.lsup = (b.lsup + c.lsup) / 2
//                hypothesis.rsup = (b.rsup + c.rsup) / 2
//                hypothesis.lcen = (b.lcen + c.lcen) / 2
//                hypothesis.rcen = (b.rcen + c.rcen) / 2
//                hypothesis.lsub = (b.lsub + c.lsub) / 2
//                hypothesis.rsup = (b.rsup + c.rsup) / 2
//            }
//
//            SpatialRelationship.SUBSCRIPT -> {
//                hypothesis.lsup = b.lsup
//                hypothesis.lcen = b.lcen
//                hypothesis.lsub = b.lsub
//                hypothesis.rsup = b.rsup
//                hypothesis.rcen = b.rcen
//                hypothesis.rsub = c.rsub
//            }
//
//            SpatialRelationship.SUPERSCRIPT -> {
//                hypothesis.lsup = b.lsup
//                hypothesis.lcen = b.lcen
//                hypothesis.lsub = b.lsub
//                hypothesis.rsup = c.rsup
//                hypothesis.rcen = b.rcen
//                hypothesis.rsub = b.rsub
//            }
//
//            SpatialRelationship.SSE -> {
//                hypothesis.lsup = b.lsup
//                hypothesis.lcen = (b.lcen + c.lcen) / 2
//                hypothesis.lsub = c.lsub
//                hypothesis.rsup = b.rsup
//                hypothesis.rcen = (b.rcen + c.rcen) / 2
//                hypothesis.rsub = c.rsub
//            }
//
//            SpatialRelationship.INSIDE -> {
//                hypothesis.lsup = b.lsup
//                hypothesis.lcen = b.lcen
//                hypothesis.lsub = b.lsub
//                hypothesis.rsup = (b.rsup + c.rsup) / 2
//                hypothesis.rcen = (b.rcen + c.rcen) / 2
//                hypothesis.rsub = (b.rsub + c.rsub) / 2
//            }
//
//            SpatialRelationship.VERTICAL, SpatialRelationship.VE -> {
//                when (mergeTag) {
//                    MergeTag.A -> {
//                        hypothesis.lcen = b.lcen
//                        hypothesis.rcen = b.rcen
//                    }
//                    MergeTag.B -> {
//                        hypothesis.lcen = c.lcen
//                        hypothesis.rcen = c.rcen
//                    }
//                    MergeTag.C -> {
//                        hypothesis.lcen = (b.region.y + b.region.t).toDouble() / 2
//                        hypothesis.rcen = (c.region.y + c.region.t).toDouble() / 2
//                    }
//                    MergeTag.M -> {
//                        hypothesis.lcen = (b.lcen + c.lcen) / 2
//                        hypothesis.rcen = (b.rcen + c.rcen) / 2
//                    }
//                }
//
//                hypothesis.lsup = region.y + 0.1 * (hypothesis.lcen - region.y)
//                hypothesis.rsup = region.y + 0.1 * (hypothesis.rcen - region.y)
//                hypothesis.lsub = region.t - 0.1 * (region.t - hypothesis.lcen)
//                hypothesis.rsub = region.t - 0.1 * (region.t - hypothesis.rcen)
//            }
//
//            SpatialRelationship.ROOT -> {
//                hypothesis.lsup = c.lsup
//                hypothesis.lcen = (b.lcen + c.lcen) / 2
//                hypothesis.lsub = b.lsub
//                hypothesis.rsup = b.rsup
//                hypothesis.rcen = b.rcen
//                hypothesis.rsub = b.rsub
//            }
//
//        }
//        return hypothesis
//    }


}