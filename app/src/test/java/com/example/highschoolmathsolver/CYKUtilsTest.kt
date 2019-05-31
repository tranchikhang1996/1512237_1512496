package com.example.highschoolmathsolver

import com.example.highschoolmathsolver.defination.MergeTag
import com.example.highschoolmathsolver.defination.SpatialRelationship
import com.example.highschoolmathsolver.detector.data.CYKCell
import com.example.highschoolmathsolver.detector.data.rule.BinaryRule
import com.example.highschoolmathsolver.util.CykUtils
import org.junit.Test
import org.opencv.core.Rect
import org.junit.Assert.*

class CYKUtilsTest {
    private val binaryRule = BinaryRule( "Term", "LetterCap" ,"TermSub" ,
        0.00060063, SpatialRelationship.HORIZONTAL,"`$1@$2" , MergeTag.M)
    @Test
    fun testGetH1() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(170, 120, 40, 30)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH2() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(170, 100, 20, 20)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH3() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(170, 80, 40, 40)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH4() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(130, 60, 15, 20)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH5() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(110, 60, 40, 20)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH6() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(110, 80, 50, 30)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH7() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(110, 100, 40, 20)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH8() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(110, 120, 40, 30)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH9() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(110, 140, 30, 20)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH10() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(122, 134, 30, 20)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH11() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(122, 134, 22, 20)
        assertTest(rect1, rect2, expected = 0)
    }

    @Test
    fun testGetH12() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(140, 100, 40, 20)
        assertTest(rect1, rect2, expected = 1)
    }

    @Test
    fun testGetH13() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(140, 80, 40, 30)
        assertTest(rect1, rect2, expected = 1)
    }

    @Test
    fun testGetH14() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(140, 120, 40, 30)
        assertTest(rect1, rect2, expected = 1)
    }

    @Test
    fun testGetH15() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(122, 124, 20, 20)
        assertTest(rect1, rect2, expected = 1)
    }

    @Test
    fun testGetH16() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(124, 94, 20, 30)
        assertTest(rect1, rect2, expected = 1)
    }

    @Test
    fun testGetH17() {
        val rect1 = Rect(100,100, 30, 20)
        val rect2 = Rect(124, 80, 20, 30)
        assertTest(rect1, rect2, expected = 1)
    }

    private fun assertTest(rectC : Rect, rect2 : Rect, rx : Int = 10, ry : Int = 10, expected : Int) {
        val c = CYKCell(HashMap(), null, null,-1,-1, rectC )
        val h = CYKCell(HashMap(), null, null, -1,-1, rect2)
        val results = CykUtils.getH(c, arrayListOf(h), rx, ry)
        assertEquals(expected, results.size)
    }

    @Test
    fun testBinarySearch() {
        val list = arrayListOf(1,2,4,4,4,4,4,5,6,7)
        val m = CykUtils.binarySearchInt(5, list)
        assertEquals(7, m)
    }

    @Test
    fun testShouldSkip() {
        val listC = arrayListOf(1L, 2L, 3L, 4L)
        val listCi = arrayListOf(2L, 5L, 6L)
        val result = CykUtils.shouldSkip(listC, listCi)
        assertEquals(true, result)
    }

    @Test
    fun testShouldSkip1() {
        val listC = arrayListOf(1L, 2L, 4L)
        val listCi = arrayListOf(3L, 5L, 6L)
        val result = CykUtils.shouldSkip(listC, listCi)
        assertEquals(false, result)
    }

    @Test
    fun testShouldSkip2() {
        val listC = arrayListOf<Long>()
        val listCi = arrayListOf(2L, 5L, 6L)
        val result = CykUtils.shouldSkip(listC, listCi)
        assertEquals(false, result)
    }
}