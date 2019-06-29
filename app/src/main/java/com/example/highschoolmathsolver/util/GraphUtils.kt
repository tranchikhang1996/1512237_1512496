package com.example.highschoolmathsolver.util

import android.graphics.Color
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries

class GraphUtils {

    companion object {

        fun showRootGraph(graph: GraphView, scrollable : Boolean) {
            val rootSeries: PointsGraphSeries<DataPoint> = PointsGraphSeries()
            graph.viewport.apply {
                isYAxisBoundsManual = true
                isXAxisBoundsManual = true
                setMinY(-7.0)
                setMaxY(7.0)
                setMinX(-7.0)
                setMaxX(7.0)
            }
            graph.viewport.apply {
                isScrollable = scrollable
                //setScalableY(true)
                //isScalable = true
                setScrollableY(scrollable)
            }
            rootSeries.appendData(DataPoint(0.0, 0.0), true, 50)
            rootSeries.shape = PointsGraphSeries.Shape.POINT
            rootSeries.size = 10.0f
            rootSeries.color = Color.WHITE
            graph.addSeries(rootSeries)
        }

        fun drawGraph(graph: GraphView, expression: String) {
            val lineSeries: LineGraphSeries<DataPoint> = LineGraphSeries()
            val pointSeries: PointsGraphSeries<DataPoint> = PointsGraphSeries()
            val seriesMaxX: LineGraphSeries<DataPoint> = LineGraphSeries()
            val seriesMaxY: LineGraphSeries<DataPoint> = LineGraphSeries()
            val seriesMinX: LineGraphSeries<DataPoint> = LineGraphSeries()
            val seriesMinY: LineGraphSeries<DataPoint> = LineGraphSeries()
            var x = -25.1
            var y: Double
            for (i in 1..500) {
                x += 0.1
                y = MathUtils.calculateY(x, expression)
                lineSeries.appendData(DataPoint(x, y), true, 1000)
            }
            lineSeries.color = Color.WHITE
            pointSeries.appendData(DataPoint(0.0, 0.0), true, 50)
            pointSeries.shape = PointsGraphSeries.Shape.POINT
            pointSeries.size = 10.0f
            pointSeries.color = Color.WHITE
            graph.addSeries(pointSeries)
            graph.addSeries(lineSeries)
            var xMiny = -30.0
            var yMiny: Double
            for (i in 1..60) {
                xMiny += 1
                yMiny = -100.0
                seriesMinY.appendData(DataPoint(xMiny, yMiny), false, 1000)
            }
            graph.addSeries(seriesMinY)
            var xMaxy = -30.0
            var yMaxy: Double
            for (i in 1..60) {
                xMaxy += 1
                yMaxy = 100.0
                seriesMaxY.appendData(DataPoint(xMaxy, yMaxy), false, 1000)
            }
            graph.addSeries(seriesMaxY)
            var xMinx: Double
            var yMinx = -30.0
            for (i in 1..60) {
                xMinx = -100.0
                yMinx += 1
                seriesMinX.appendData(DataPoint(xMinx, yMinx), false, 1000)
            }
            graph.addSeries(seriesMinX)

            var xMaxX = 0.0
            var yMaxX = -30.0
            for (i in 1..60) {
                xMaxX += 100.0
                yMaxX += 1
                seriesMaxX.appendData(DataPoint(xMaxX, yMaxX), false, 1000)
            }
            graph.addSeries(seriesMaxX)
        }

    }
}