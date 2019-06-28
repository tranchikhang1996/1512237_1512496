package com.example.highschoolmathsolver.util

import com.example.highschoolmathsolver.defination.MergeTag
import com.example.highschoolmathsolver.defination.SpatialRelationship
import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.detector.data.Hypothesis
import com.example.highschoolmathsolver.extentions.center
import com.example.highschoolmathsolver.extentions.merged
import com.example.highschoolmathsolver.extentions.t
import com.google.android.gms.vision.Frame
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import org.opencv.core.CvType
import android.widget.ImageView
import org.opencv.android.Utils
import org.opencv.core.Mat
import timber.log.Timber


class ImageUtils {
    companion object {
        private const val CONTOUR_MODE = Imgproc.RETR_EXTERNAL
        private const val CONTOUR_METHOD = Imgproc.CHAIN_APPROX_TC89_KCOS

        fun toMat(frame: Frame, rect: Rect): Mat {
            val height = frame.metadata.height * 3 / 2
            val width = frame.metadata.width
            val data = frame.grayscaleImageData
            val nv21Image = Mat(height, width, CvType.CV_8UC1)
            nv21Image.put(0,0, data.array())
            val grayScale = Mat()
            Imgproc.cvtColor(nv21Image, grayScale, Imgproc.COLOR_YUV2GRAY_NV21)
            val roi = Mat(grayScale, rect)
            val invertRoi = Mat()
            Core.rotate(roi, invertRoi, Core.ROTATE_90_CLOCKWISE)
            return invertRoi
        }

        fun toMat(data : ByteArray, rect: Rect): Mat {
            val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
            val myBitmap32 = bmp.copy(Bitmap.Config.ARGB_8888, true)
            val nv21Image = Mat(bmp.height, bmp.width, CvType.CV_8UC3)
            Utils.bitmapToMat(myBitmap32, nv21Image)
            val grayScale = Mat()
            Imgproc.cvtColor(nv21Image, grayScale, Imgproc.COLOR_RGB2GRAY)
            val wRatio = myBitmap32.width / 1280f
            val hRatio = myBitmap32.height / 720f
            val frame = Rect(
                (rect.x * wRatio).toInt(),
                (rect.y * hRatio).toInt(),
                (rect.width * wRatio).toInt(),
                (rect.height * hRatio).toInt()
            )
            val roi = Mat(grayScale, frame)
            val invertRoi = Mat()
            Core.rotate(roi, invertRoi, Core.ROTATE_90_CLOCKWISE)
            return invertRoi
        }

        fun preProcessing(src: Mat): Mat {
            val size = Size(3.0, 3.0)
            val thresh = Mat()
            val gaussianBlur = Mat()
            Imgproc.GaussianBlur(src, gaussianBlur, size, 0.0)
            Imgproc.adaptiveThreshold(
                gaussianBlur,
                thresh,
                255.0,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY,
                75,
                10.0
            )
            val bitwise = Mat()
            Core.bitwise_not(thresh, bitwise)
            val morphologyOpen = Mat()
            val kernel = Mat(Size(3.0, 3.0), CvType.CV_8UC1, Scalar(255.0))
            Imgproc.morphologyEx(bitwise, morphologyOpen, Imgproc.MORPH_OPEN, kernel)
            return morphologyOpen
//            val morphologyClose = Mat()
//            Imgproc.morphologyEx(morphologyOpen, morphologyClose, Imgproc.MORPH_CLOSE, kernel)
//            return morphologyOpen
        }

        fun segment(thresh: Mat): List<Pair<Rect, List<Point>>> {
            val contours = arrayListOf<MatOfPoint>()
            val hierarchy = Mat()
            Imgproc.findContours(thresh, contours, hierarchy, CONTOUR_MODE, CONTOUR_METHOD, Point(0.0, 0.0))
            return contours.map { Pair(Imgproc.boundingRect(it), it.toList()) }
        }

        fun mergeRegion(regions: List<Rect>): Rect {
            var rect = regions[0]
            for (i in 0 until regions.size - 1) {
                rect = rect.merged(regions[i + 1])
            }
            return rect
        }

        fun toSpatialRelationship(tag: String): SpatialRelationship = when (tag) {
            "H" -> SpatialRelationship.HORIZONTAL
            "V" -> SpatialRelationship.VERTICAL
            "Sub" -> SpatialRelationship.SUBSCRIPT
            "Sup" -> SpatialRelationship.SUPERSCRIPT
            "Ins" -> SpatialRelationship.INSIDE
            "Mrt" -> SpatialRelationship.ROOT
            "Ve" -> SpatialRelationship.VE
            "SSE" -> SpatialRelationship.SSE
            else -> SpatialRelationship.UNKNOWN
        }

        fun toMergeTag(tag: String): MergeTag = when (tag) {
            "A" -> MergeTag.A
            "B" -> MergeTag.B
            "M" -> MergeTag.M
            else -> MergeTag.C
        }

        fun toSymbolType(tag: String): SymbolType = when (tag) {
            "a" -> SymbolType.ASC
            "d" -> SymbolType.DESC
            "m" -> SymbolType.MIDDLE
            else -> SymbolType.NORM
        }

        fun setSymbolRegion(region: Rect, centroid: Double, symbolType: SymbolType): Hypothesis {
            val hypothesis = Hypothesis(region)
            when (symbolType) {
                SymbolType.ASC -> {
                    hypothesis.lcen = (centroid + region.t) / 2
                    hypothesis.rcen = (centroid + region.t) / 2
                }
                SymbolType.DESC -> {
                    hypothesis.lcen = (centroid + region.y) / 2
                    hypothesis.rcen = (centroid + region.y) / 2
                }
                SymbolType.NORM -> {
                    hypothesis.lcen = centroid
                    hypothesis.rcen = centroid
                }
                else -> {
                    hypothesis.lcen = region.center().y
                    hypothesis.rcen = region.center().y
                }
            }
            return hypothesis
        }

        fun drawToImageView(thresh : Mat, shutter : ImageView?) {
            val contours = segment(thresh)
            for (pair in contours) {
                val rect = pair.first
                Imgproc.rectangle(
                    thresh,
                    Point(rect.x.toDouble(), rect.y.toDouble()),
                    Point(rect.x.toDouble() + rect.width, rect.y.toDouble() + rect.height),
                    Scalar(255.0, 0.0, 0.0, 255.0),
                    2
                )
            }
            Timber.d("SCFGDetector number of contours = %d", contours.size)
            val bitmap = Bitmap.createBitmap(thresh.cols(), thresh.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(thresh, bitmap)
            AndroidUtils.runOnUIThreadWithRxjava { shutter?.setImageBitmap(bitmap) }
        }

    }
}