package com.example.highschoolmathsolver.util

import com.example.highschoolmathsolver.detector.data.CYKCell
import com.example.highschoolmathsolver.extentions.s
import com.example.highschoolmathsolver.extentions.t
import org.opencv.core.Point
import org.opencv.core.Rect
import kotlin.math.max
import kotlin.math.min

class CykUtils {
    companion object {

        fun getRX(boxes: List<Pair<Rect, List<Point>>>): Double {
            if (boxes.isEmpty()) {
                return 0.0
            }
            if(boxes.size < 2) {
                return boxes[0].first.width.toDouble()
            }
            val sorted = boxes.sortedBy { it.first.width }
            val median =
                if (sorted.size % 2 == 0) (sorted[sorted.size / 2].first.width + sorted[sorted.size / 2 - 1].first.width) / 2.0 else
                    sorted[sorted.size / 2 - 1].first.width.toDouble()

            val sum = sorted.sumBy { it.first.width }.toDouble()
            return max(sum / sorted.size, median)
        }

        fun getRY(boxes: List<Pair<Rect, List<Point>>>): Double {
            if (boxes.isEmpty()) {
                return 0.0
            }
            if(boxes.size < 2) {
                return boxes[0].first.height.toDouble()
            }
            val sorted = boxes.sortedBy { it.first.height }
            val median =
                if (sorted.size % 2 == 0) (sorted[sorted.size / 2].first.height + sorted[sorted.size / 2 - 1].first.height) / 2.0 else
                    sorted[sorted.size / 2 - 1].first.height.toDouble()

            val sum = sorted.sumBy { it.first.height }.toDouble()
            return max(sum / sorted.size, median)
        }

        fun shouldSkip(listC: List<Long>, listCi: List<Long>): Boolean {
            for (ci in listCi) {
                if (listC.contains(ci)) {
                    return true
                }
            }

            for (c in listC) {
                if (listCi.contains(c)) {
                    return true
                }
            }
            return false
        }

        fun getH(c: CYKCell, neighbors: List<CYKCell>, rx: Int, ry: Int): List<CYKCell> {
            val sx = c.region.s - (0.8 * c.region.width).toInt()
            val ss = c.region.s + (rx)
            val sy = c.region.y - 0.5 * ry
            val st = c.region.t + 0.5 * ry
            val start = binarySearch(sx, neighbors)
            val end = binarySearch(ss + 1, neighbors)
            val validCells = arrayListOf<CYKCell>()
            if(neighbors.isEmpty()) {
                return validCells
            }
            for (i in start..end) {
                val ci = neighbors[i]
                if (shouldSkip(c.childIds, ci.childIds)) {
                    continue
                }

                if (ci.region.x in sx..ss && ci.region.y < st && ci.region.t > sy) {
                    validCells.add(ci)
                }
            }
            return validCells
        }

        fun getV(c: CYKCell, neighbors: List<CYKCell>, rx: Int, ry: Int): List<CYKCell> {
            val sx = c.region.x - 2 * rx
            val ss = c.region.s + 2 * rx
            val sy = c.region.t + 1
            val st = c.region.t + ry * 3
            return neighbors.filter { !shouldSkip(c.childIds, it.childIds) }
                .filter { it.region.x >= sx && it.region.s <= ss && it.region.y in sy..st}
        }

        fun getI(c: CYKCell, neighbors: List<CYKCell>, rx: Int, ry: Int): List<CYKCell> {
            val sx = c.region.x + 1
            val ss = c.region.s + 1
            val sy = c.region.y + 1
            val st = c.region.t + 1
            val start = binarySearch(sx, neighbors)
            val end = binarySearch(ss + 1, neighbors)
            val validCells = arrayListOf<CYKCell>()
            if(neighbors.isEmpty()) {
                return validCells
            }
            for (i in start..end) {
                val ci = neighbors[i]
                if (shouldSkip(c.childIds, ci.childIds)) {
                    continue
                }

                if (ci.region.x in sx..ss && ci.region.s in sx..ss && ci.region.y <= st && ci.region.t >= sy) {
                    validCells.add(ci)
                }
            }
            return validCells
        }

        fun getM(c: CYKCell, neighbors: List<CYKCell>, rx: Int, ry: Int): List<CYKCell> {
            val sx = c.region.x - rx
            val ss = min(c.region.x + 2 * rx, c.region.s) - 1
            val sy = c.region.y
            val st = min(c.region.y + 2 * ry, c.region.t) - 1
            val start = binarySearch(sx, neighbors)
            val end = binarySearch(ss + 1, neighbors)
            val validCells = arrayListOf<CYKCell>()
            if(neighbors.isEmpty()) {
                return validCells
            }
            for (i in start..end) {
                val ci = neighbors[i]
                if (shouldSkip(c.childIds, ci.childIds)) {
                    continue
                }

                if (ci.region.s in sx..ss && ci.region.t in sy..st) {
                    validCells.add(ci)
                }
            }
            return validCells
        }

        fun getS(c: CYKCell, neighbors: List<CYKCell>, rx: Int, ry: Int): List<CYKCell> {
            val sx = c.region.x - 5
            val ss = c.region.x + 5
            val sy = c.region.t
            val st = c.region.t + ry
            val start = binarySearch(sx, neighbors)
            val end = binarySearch(ss + 1, neighbors)
            val validCells = arrayListOf<CYKCell>()
            if(neighbors.isEmpty()) {
                return validCells
            }
            for (i in start..end) {
                val ci = neighbors[i]
                if (shouldSkip(c.childIds, ci.childIds)) {
                    continue
                }

                if (ci.region.x in sx..ss && ci.region.y in sy..st) {
                    validCells.add(ci)
                }
            }
            return validCells
        }

        private fun binarySearch(sx : Int, neighbors : List<CYKCell>) : Int {
            var l = 0
            var r = neighbors.size - 1

            while(l < r) {
                val c = (l + r) / 2
                if(sx <= neighbors[c].region.x) {
                    r = c
                }
                else {
                    l = c + 1
                }
            }
            return l
        }

        fun binarySearchInt(sx : Int, neighbors : List<Int>) : Int {
            var l = 0
            var r = neighbors.size - 1

            while(l < r) {
                val c = (l + r) / 2
                if(sx <= neighbors[c]) {
                    r = c
                }
                else {
                    l = c + 1
                }
            }
            return l
        }
    }
}