package com.example.highschoolmathsolver.detector

import android.media.Image
import com.example.highschoolmathsolver.model.entity.Expression
import io.reactivex.Observable

interface IDetector {
    fun detect(image : Image, previewWidth : Int, previewHeight : Int, rectWidth : Int, rectHeight : Int) : Observable<String>
}