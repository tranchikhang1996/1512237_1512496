package com.example.highschoolmathsolver

import com.example.highschoolmathsolver.defination.SpatialRelationship
import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.detector.data.Hypothesis
import com.example.highschoolmathsolver.detector.model.SpatialRelationshipModel
import com.example.highschoolmathsolver.extentions.center
import com.example.highschoolmathsolver.util.ImageUtils
import org.junit.Test
import org.junit.Assert.*
import org.opencv.core.Rect

class RelationshipTest {

    private val rx = 4.0
    private val ry = 4.0
    private val model = SpatialRelationshipModel()

    @Test
    fun testHorizontalProb1() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(140, 104, 40, 22)
        val hyp1 = Hypothesis(rect1)
        val hyp2 = Hypothesis(rect2)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.HORIZONTAL, rx, ry)
        assertEquals(1.0f / 6, p1.toFloat())
        assertEquals(3.0f / 4, p2.toFloat())
    }

    @Test
    fun testHorizontalProb2() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 90, 40, 30)
        val hyp1 = Hypothesis(rect1)
        val hyp2 = Hypothesis(rect2)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.HORIZONTAL, rx, ry)
        assertEquals(1.0f / 2, p1.toFloat())
        assertEquals(3.0f / 4, p2.toFloat())
    }

    @Test
    fun testHorizontalProb3() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 84, 30, 20)
        val hyp1 = Hypothesis(rect1)
        val hyp2 = Hypothesis(rect2)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.HORIZONTAL, rx, ry)
        assertEquals(1.0f / 2, p1.toFloat())
        assertEquals(1.0f / 5, p2.toFloat())
    }

    @Test
    fun testHorizontalProb4() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 114, 30, 20)
        val hyp1 = Hypothesis(rect1)
        val hyp2 = Hypothesis(rect2)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.HORIZONTAL, rx, ry)
        assertEquals(1.0f / 2, p1.toFloat())
        assertEquals(3.0f / 10, p2.toFloat())
    }

    @Test
    fun testSubProb1() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 114, 30, 20)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.SUBSCRIPT, rx, ry)
        assertEquals(1.0f / 2, p1.toFloat())
        assertEquals(3.0f / 4, p2.toFloat())
    }

    @Test
    fun testSubProb2() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 90, 30, 20)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.SUBSCRIPT, rx, ry)
        assertEquals(0f, p1.toFloat())
        assertEquals(0f, p2.toFloat())
    }

    @Test
    fun testSubProb3() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 110, 30, 20)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.SUBSCRIPT, rx, ry)
        assertEquals(1.0f / 2, p1.toFloat())
        assertEquals(19.0f / 20, p2.toFloat())
    }

    @Test
    fun testSupProb1() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 84, 30, 20)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.SUPERSCRIPT, rx, ry)
        assertEquals(1.0f / 2, p1.toFloat())
        assertEquals(13.0f / 20, p2.toFloat())
    }

    @Test
    fun testSupProb2() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 96, 30, 20)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.SUPERSCRIPT, rx, ry)
        assertEquals(0f, p1.toFloat())
        assertEquals(0f, p2.toFloat())
    }

    @Test
    fun testSupProb3() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(136, 90, 30, 20)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.SUPERSCRIPT, rx, ry)
        assertEquals(1f / 2, p1.toFloat())
        assertEquals(19f / 20, p2.toFloat())
    }

    @Test
    fun testVerProb1() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(90, 130, 50, 30)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.VERTICAL, rx, ry)
        assertEquals(1f / 6, p1.toFloat())
        assertEquals(1f, p2.toFloat())
    }

    @Test
    fun testVerProb2() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(100, 130, 50, 30)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.VERTICAL, rx, ry)
        assertEquals(1f / 6, p1.toFloat())
        assertEquals(1f /6, p2.toFloat())
    }

    @Test
    fun testVerProb3() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(84, 130, 50, 30)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.VERTICAL, rx, ry)
        assertEquals(1f / 6, p1.toFloat())
        assertEquals(1f /2, p2.toFloat())
    }

    @Test
    fun testSSEProb1() {
        val rect1 = Rect(100, 100, 30, 20)
        val rect2 = Rect(100, 130, 50, 30)
        val hyp1 = ImageUtils.setSymbolRegion(rect1, rect1.center().y, SymbolType.NORM)
        val hyp2 = ImageUtils.setSymbolRegion(rect2, rect2.center().y, SymbolType.NORM)
        val (p1, p2) = model.computeP1P2(hyp1, hyp2, SpatialRelationship.SSE, rx, ry)
        assertEquals(1f / 6, p1.toFloat())
        assertEquals(1f, p2.toFloat())
    }
}