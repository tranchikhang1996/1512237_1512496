package com.example.highschoolmathsolver.detector.model

import org.opencv.core.Core.BORDER_ISOLATED
import org.opencv.core.Core.copyMakeBorder
import org.opencv.core.CvType.CV_32F
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.ml.ANN_MLP
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max

class SymbolRecognitionModel @Inject constructor(private val mlp: ANN_MLP, private val labelTable : Map<Int, String>) {

    private val DESIRED_SIZE = Size(40.0, 40.0)
    private val UNKNOWN_DIGIT = "Unknown"
    private val NO_TOP = 1

    fun getPrediction(image: Mat, bi: Rect): List<Pair<String, Double>> {
        val predictions = recognizeDigit(cropImage(image, bi))
        return predictions.sortedByDescending { it.second }.take(NO_TOP)
    }

    fun getBestPredict(image : Mat, rect : Rect) : Pair<String, Double> {
        val predictions = recognizeDigit(cropImage(image, rect))
        return predictions.maxBy { it.second } ?: Pair("", 0.0)
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

    private fun cropImage(src: Mat, rect: Rect): Mat = Mat(src, rect)

}