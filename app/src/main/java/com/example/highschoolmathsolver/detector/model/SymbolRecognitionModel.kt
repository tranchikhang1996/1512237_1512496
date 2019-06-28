package com.example.highschoolmathsolver.detector.model

import android.graphics.Bitmap
import com.example.highschoolmathsolver.detector.data.CombineRegion
import com.example.highschoolmathsolver.extentions.isInside
import com.example.highschoolmathsolver.extentions.isOverlapOf
import com.example.highschoolmathsolver.extentions.s
import com.example.highschoolmathsolver.extentions.t
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.core.Core.BORDER_ISOLATED
import org.opencv.core.Core.copyMakeBorder
import org.opencv.core.CvType.CV_32F
import org.opencv.core.CvType.CV_8UC1
import org.opencv.imgproc.Imgproc
import org.opencv.ml.ANN_MLP
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max

class SymbolRecognitionModel @Inject constructor(private val mlp: ANN_MLP, private val labelTable : Map<Int, String>) {

    private val DESIRED_SIZE = Size(28.0, 28.0)
    private val UNKNOWN_DIGIT = "Unknown"
    private val NO_TOP = 1

    fun getPrediction(imageView: MutableList<Bitmap>?, image: Mat, bi: CombineRegion, components: List<CombineRegion>): List<Pair<String, Double>> {
        if(bi.region.first.width / bi.region.first.height > 10 && bi.region.first.height < 10) {
            return arrayListOf(Pair("-", 1.0))
        }
        val predictions = recognizeDigit(getCropImage(imageView, image, bi, components))
        return predictions.sortedByDescending { it.second }.take(NO_TOP)
    }

    private fun getCropImage(shutter : MutableList<Bitmap>?, image : Mat, bi : CombineRegion, components: List<CombineRegion>) : Mat {
        val overlapRect = getOverlapRect(bi, components)
        val thresh =  cropImage(image, bi.region.first, overlapRect)
        val bitmap = Bitmap.createBitmap(thresh.cols(), thresh.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(thresh, bitmap)
        shutter?.add(bitmap)
        return thresh
    }

    private fun getOverlapRect(bi: CombineRegion, components: List<CombineRegion>): List<Rect> {
        val results = arrayListOf<Rect>()
        for (it in components) {
            if (bi === it || !bi.region.first.isOverlapOf(it.region.first) || bi.region.first.isInside(it.region.first)) {
                continue
            }
            val x = if (it.region.first.x in bi.region.first.x..bi.region.first.s)
                it.region.first.x - bi.region.first.x else 0
            val y = if (it.region.first.y in bi.region.first.y..bi.region.first.t)
                it.region.first.y - bi.region.first.y else 0
            val s = if (it.region.first.s in bi.region.first.x..bi.region.first.s)
                it.region.first.s - bi.region.first.x else bi.region.first.width
            val t = if (it.region.first.t in bi.region.first.y..bi.region.first.t)
                it.region.first.t - bi.region.first.y else bi.region.first.height
            val cross = Rect(bi.region.first.x + x, bi.region.first.y + y, s - x, t - y)
            if (shouldSkipClear(bi.region.second, cross)) {
                continue
            }
            results.add(Rect(x, y, s - x, t - y))
        }
        return results
    }

    private fun shouldSkipClear(points : List<Point>, rect: Rect) : Boolean{
        for(point in points) {
            if(point.x.toInt() in rect.x..rect.s && point.y.toInt() in rect.y..rect.t) {
                return true
            }
        }
        return false
    }

    private fun recognizeDigit(roi: Mat): List<Pair<String, Double>> {
        val normalImage = resizeImage(roi)
        val sampleInput = Mat()
        val probabilities = Mat()
        val sampleVector = normalImage.reshape(1, 1)
        sampleVector.convertTo(sampleInput, CV_32F, 1.0 / 255.0)
        mlp.predict(sampleInput, probabilities)
        return labelPrediction(probabilities)
    }

        private fun resizeImage(roi: Mat): Mat {
        val width = roi.width()
        val height = roi.height()
        val length = max(width, height)
        val diff = abs(width - height)
        val padding = (length * 0.2).toInt()
        var top = padding
        var left = padding
        var right = padding
        var bottom = padding
        if (width > height) {
            top += diff / 2
            bottom += diff - (diff / 2)
        } else {
            left += diff / 2
            right += diff - (diff / 2)
        }

        val normalImage = Mat()
        val resizeImage = Mat()
        copyMakeBorder(roi, resizeImage, top, bottom, left, right, BORDER_ISOLATED, Scalar(0.0, 0.0, 0.0, 255.0))
        Imgproc.resize(resizeImage, normalImage, DESIRED_SIZE)
        return normalImage
    }

        private fun labelPrediction(probabilities: Mat): List<Pair<String, Double>> {
        val col = probabilities.cols()
        val prediction = arrayListOf<Pair<String, Double>>()
        for (i in 0 until col) {
            val arrayValue = probabilities.get(0, i)
            if (arrayValue == null || arrayValue.isEmpty()) {
                Timber.d("null in %d", i)
                continue
            }
            val label = labelTable[i] ?: UNKNOWN_DIGIT
            val prob = (arrayValue[0] + 1.7159) / (1.7159 *2)
            prediction.add(Pair(label, prob))
        }
        return prediction
    }

    private fun cropImage(src: Mat, rect: Rect, overlapRect: List<Rect>): Mat {
        val roi = Mat()
        val crop = Mat(src, rect)
        crop.copyTo(roi)
        val mask = Mat.zeros(roi.size(), CV_8UC1)
        for (region in overlapRect) {
            mask.submat(region).setTo(Scalar.all(1.0))
        }
        return roi.setTo(Scalar.all(1.0), mask)
    }

}