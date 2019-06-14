package com.example.highschoolmathsolver.extentions

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.opencv.core.Point
import org.opencv.core.Rect
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun <T> Observable<T>.applySchedulers(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applySchedulers(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Rect.isOverlapOf(rect: Rect): Boolean {
    val c1 = this.center()
    val c2 = rect.center()
    val cdx = abs(c1.x - c2.x)
    val cdy = abs(c1.y - c2.y)
    val sumWidth = this.width / 2.0 + rect.width / 2.0
    val sumHeight = this.height / 2.0 + rect.height / 2.0
    return !(cdx > sumWidth || cdy > sumHeight)
}

fun Rect.getSearchRegion(rx: Int, ry: Int): Rect {
    return Rect(this.x - rx, this.y - ry, this.width + 2 * rx, this.height + 2 * ry)
}

fun Rect.center(): Point {
    return Point(this.x + this.width / 2.0, this.y + this.height / 2.0)
}

fun Rect.isInside(rect : Rect) : Boolean {
    return this.x >= rect.x && this.s <= rect.s && this.y >= rect.y && this.t <= rect.t
}

fun Rect.merged(rect: Rect): Rect {
    val tlx = min(this.x, rect.x)
    val tly = min(this.y, rect.y)
    val brx = max(this.s, rect.s)
    val bry = max(this.t, rect.t)
    return Rect(tlx, tly, brx - tlx, bry - tly)
}

val Rect.s : Int get() = this.x + this.width

val Rect.t : Int get() = this.y + this.height
