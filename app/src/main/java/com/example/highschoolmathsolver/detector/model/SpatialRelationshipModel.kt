package com.example.highschoolmathsolver.detector.model

import com.example.highschoolmathsolver.defination.SpatialRelationship
import com.example.highschoolmathsolver.detector.data.Gmm
import com.example.highschoolmathsolver.detector.data.Hypothesis
import com.example.highschoolmathsolver.extentions.isInside
import com.example.highschoolmathsolver.extentions.s
import com.example.highschoolmathsolver.extentions.t

import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SpatialRelationshipModel @Inject constructor(val model: Gmm) {

//    fun getProb(h1: Hypothesis, h2: Hypothesis, relationship: SpatialRelationship, rx: Double, ry: Double): Double =
//        when (relationship) {
//
//            SpatialRelationship.INSIDE -> {
//                val dx = abs(h2.region.s - h1.region.s)
//                val dy = abs(h2.region.t - h1.region.t)
//                1 - (dx * dx + dy * dy) / (rx * rx + ry * ry)
//            }
//
//            SpatialRelationship.ROOT -> {
//                val dx = abs(h2.region.s - h1.region.x)
//                val dy = abs(h2.region.s - h1.region.y)
//
//                1 - (dx * dx + dy * dy) / (rx * rx + ry * ry)
//            }
//
//            SpatialRelationship.UNKNOWN -> 0.0
//
//            else -> {
//                val (p1, p2) = computeP1P2(h1, h2, relationship, rx, ry)
//                (p1 + p2) / 2
//            }
//        }
//
//    fun computeP1P2(h1 : Hypothesis, h2 : Hypothesis, relationship : SpatialRelationship, rx : Double, ry : Double) : Pair<Double, Double> {
//        val dx = abs(h2.region.x - h1.region.s)
//        return when (relationship) {
//            SpatialRelationship.HORIZONTAL -> {
//                val dvc = abs(h2.lcen - h1.rcen)
//                val p1 = 1 - dx / (3 * rx)
//                val p2 = 1 - dvc / max(ry, h1.region.height.toDouble())
//                Pair(p1, p2)
//            }
//            SpatialRelationship.SUBSCRIPT -> {
//                val dy = h2.lcen - h1.rsub
//                if(dy < 0) Pair(0.0,0.0) else {
//                    val p1 = 1 - dx / (3 * rx)
//                    val p2 = 1 - dy / max(ry, h1.region.height.toDouble())
//                    Pair(p1,p2)
//                }
//            }
//            SpatialRelationship.SUPERSCRIPT -> {
//                val dy = h1.rsup - h2.lcen
//                if(dy < 0) Pair(0.0,0.0) else {
//                    val p1 = 1 - dx / (3 * rx)
//                    val p2 = 1 - dy / max(ry, h1.region.height.toDouble())
//                    Pair(p1,p2)
//                }
//            }
//
//            SpatialRelationship.VERTICAL, SpatialRelationship.VE -> {
//                val dhc = abs(h2.region.center().x - h1.region.center().x)
//                val dy = h2.region.y - h1.region.t
//                if(dy < 0) Pair(0.0,0.0) else {
//                    val p1 = 1 - dy / (3 * ry)
//                    val p2 = 1 - (dhc) / (3 * rx)
//                    Pair(p1, p2)
//                }
//            }
//
//            SpatialRelationship.SSE -> {
//                val dy = h2.region.y - h1.region.t
//                val dl = abs(h2.region.x - h1.region.x)
//                val p1 = 1 - dy / (3 * ry)
//                val p2 = 1 - dl / rx
//                Pair(p1, p2)
//            }
//
//            else -> Pair(0.0,0.0)
//        }
//    }

    fun getProb(h1: Hypothesis, h2: Hypothesis, relationship: SpatialRelationship, rx: Double, ry: Double): Double {

        if (relationship == SpatialRelationship.SSE) {
            val dy = h2.region.y - h1.region.t
            val dl = abs(h2.region.x - h1.region.x)
            val p1 = 1 - dy / (3 * ry)
            val p2 = 1 - dl / rx
            return (p1 + p2) / 2.0
        }

        val k = getIndex(relationship)
        if (k <= 2) {
            if (k < 0 || h2.region.x < h1.region.x || h2.region.s <= h1.region.s) return 0.0

            if(h1.region.isInside(h2.region) || h2.region.isInside(h1.region)) {
                return 0.0
            }
        }

        val sample = getFeatures(h1, h2)

        val probs = model.posterior(sample)

        smooth(probs)

        return when (relationship) {
            SpatialRelationship.VERTICAL -> {
                if( h2.region.y < (h1.region.y + h1.region.t) / 2
                || abs((h1.region.x + h1.region.s) / 2  - (h2.region.x + h2.region.s) / 2) > 2.5 * rx
                || (h2.region.x > h1.region.s || h2.region.s < h1.region.x) ) 0.0 else probs[k]
            }

            SpatialRelationship.VE -> {
                if (h2.region.y < (h1.region.y + h1.region.t) / 2
                    || abs((h1.region.x + h1.region.s) / 2 - (h2.region.x + h2.region.s) / 2) > 2.5 * rx
                    || (h2.region.x > h1.region.s || h2.region.s < h1.region.x)
                )
                    0.0 else {
                    var penalty =
                        abs(h1.region.x - h2.region.x) / (3.0 * rx) + abs(h1.region.s - h2.region.s) / (3.0 * rx)
                    if (penalty > 0.95) penalty = 0.95
                    (1.0 - penalty) * probs[k]
                }
            }

            SpatialRelationship.INSIDE -> if (h2.region.x < h1.region.x || h2.region.y < h1.region.y) 0.0 else probs[k]

            else -> probs[k]
        }
    }


    private fun smooth(post: Array<Double>) {
            for (i in 0 until 6)
                post[i] = (post[i] + 0.02) / (1.00 + 6 * 0.02)
        }

        private fun getIndex(sprl: SpatialRelationship): Int = when (sprl) {
            SpatialRelationship.HORIZONTAL -> 0
            SpatialRelationship.SUBSCRIPT -> 1
            SpatialRelationship.SUPERSCRIPT -> 2
            SpatialRelationship.VERTICAL -> 3
            SpatialRelationship.INSIDE -> 4
            SpatialRelationship.ROOT -> 5
            SpatialRelationship.VE -> 3
            SpatialRelationship.SSE -> 6
            else -> -1
        }

        private fun getFeatures(a: Hypothesis, b: Hypothesis): Array<Double> {
            val sample = Array(9) { 0.0 }

            val f = (max(a.region.t, b.region.t) - min(a.region.y, b.region.y) + 1).toDouble()
            sample[0] = (b.region.t - b.region.y + 1) / f
            sample[1] = (a.rcen - b.lcen) / f
            sample[2] = ((a.region.s + a.region.x) / 2.0 - (b.region.s + b.region.x) / 2.0) / f
            sample[3] = (b.region.x - a.region.s) / f
            sample[4] = (b.region.x - a.region.x) / f
            sample[5] = (b.region.s - a.region.s) / f
            sample[6] = (b.region.y - a.region.t) / f
            sample[7] = (b.region.y - a.region.y) / f
            sample[8] = (b.region.t - a.region.t) / f
            return sample
        }
}