package com.example.highschoolmathsolver

import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.extentions.center
import com.example.highschoolmathsolver.extentions.merged
import com.example.highschoolmathsolver.extentions.s
import com.example.highschoolmathsolver.extentions.t
import com.example.highschoolmathsolver.util.ImageUtils
import org.junit.Test
import org.junit.Assert.*
import org.opencv.core.Rect

class ImageUtilTest {

    private val sample = Rect(10, 10, 30, 40)
    @Test
    fun testNorm() {
        val hyp = ImageUtils.setSymbolRegion(sample, sample.center().y, SymbolType.NORM)
        assertEquals(12.0f, hyp.lsup.toFloat())
        assertEquals(12.0f, hyp.lsup.toFloat())
        assertEquals(30.0f, hyp.lcen.toFloat())
        assertEquals(30.0f, hyp.rcen.toFloat())
        assertEquals(48.0f, hyp.lsub.toFloat())
        assertEquals(48.0f, hyp.rsub.toFloat())
    }

    @Test
    fun testDsc() {
        val hyp = ImageUtils.setSymbolRegion(sample, sample.center().y, SymbolType.DESC)
        assertEquals(11.0f, hyp.lsup.toFloat())
        assertEquals(11.0f, hyp.lsup.toFloat())
        assertEquals(20.0f, hyp.lcen.toFloat())
        assertEquals(20.0f, hyp.rcen.toFloat())
        assertEquals(35.0f, hyp.lsub.toFloat())
        assertEquals(35.0f, hyp.rsub.toFloat())
    }

    @Test
    fun testAsc() {
        val hyp = ImageUtils.setSymbolRegion(sample, sample.center().y, SymbolType.ASC)
        assertEquals(25.0f, hyp.lsup.toFloat())
        assertEquals(25.0f, hyp.lsup.toFloat())
        assertEquals(40.0f, hyp.lcen.toFloat())
        assertEquals(40.0f, hyp.rcen.toFloat())
        assertEquals(49.0f, hyp.lsub.toFloat())
        assertEquals(49.0f, hyp.rsub.toFloat())
    }

    @Test
    fun testMiddle() {
        val hyp = ImageUtils.setSymbolRegion(sample, sample.center().y, SymbolType.MIDDLE)
        assertEquals(12.0f, hyp.lsup.toFloat())
        assertEquals(12.0f, hyp.lsup.toFloat())
        assertEquals(30.0f, hyp.lcen.toFloat())
        assertEquals(30.0f, hyp.rcen.toFloat())
        assertEquals(48.0f, hyp.lsub.toFloat())
        assertEquals(48.0f, hyp.rsub.toFloat())
    }

    @Test
    fun testMergeRegion1() {
        val rect1 = Rect(10,10, 30, 20)
        val rect2 = Rect(20, 20, 40,30)
        val merge = rect1.merged(rect2)
        assertEquals(10, merge.x)
        assertEquals(10, merge.y)
        assertEquals(60, merge.s)
        assertEquals(50, merge.t)
    }

    @Test
    fun testMergeRegion2() {
        val rect1 = Rect(10,10, 30, 30)
        val rect2 = Rect(50, 0, 40,30)
        val merge = rect1.merged(rect2)
        assertEquals(10, merge.x)
        assertEquals(0, merge.y)
        assertEquals(90, merge.s)
        assertEquals(40, merge.t)
    }

    @Test
    fun testMergeRegion3() {
        val rect1 = Rect(10,10, 30, 20)
        val rect2 = Rect(0, 40, 20,30)
        val merge = rect1.merged(rect2)
        assertEquals(0, merge.x)
        assertEquals(10, merge.y)
        assertEquals(40, merge.s)
        assertEquals(70, merge.t)
    }

}