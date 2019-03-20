package com.example.highschoolmathsolver.detector

import android.media.Image
import io.reactivex.Observable

class GrammarDetector : IDetector {
    override fun detect(
        image: Image,
        previewWidth: Int,
        previewHeight: Int,
        rectWidth: Int,
        rectHeight: Int
    ): Observable<String> {
        val data = WrapImage(image, previewWidth, previewHeight, rectWidth, rectHeight)
        return Observable.just(data).map {
            return@map ""
        }
    }

    data class WrapImage(
        val image: Image,
        val previewWidth: Int,
        val previewHeight: Int,
        val rectWidth: Int,
        val rectHeight: Int
    )
}
