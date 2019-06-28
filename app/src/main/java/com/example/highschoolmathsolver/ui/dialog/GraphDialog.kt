package com.example.highschoolmathsolver.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.util.MathUtils
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.solution_graph_layout.*
import kotlinx.android.synthetic.main.solution_input_layout.math_view
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar


class GraphDialog(private val expression: String) : BaseDialogFragment() {

    override val layoutId: Int get() = com.example.highschoolmathsolver.R.layout.solution_graph_layout

    override fun setupFragmentComponent(userComponent: UserComponent) = userComponent.inject(this)

    var rootSeries:PointsGraphSeries<DataPoint> = PointsGraphSeries()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        math_view.text = "<h3>" +MathUtils.trimToKaTeX(expression)+"</h3>"
        showRootGraph()
        m_seekbar.setOnProgressChangeListener(onProgressChangeListener)
    }
    private fun showRootGraph(){
        graph.viewport.apply {
            isYAxisBoundsManual = true
            isXAxisBoundsManual = true
            setMinY(-7.0)
            setMaxY(7.0)
            setMinX(-7.0)
            setMaxX(7.0)
        }
        graph.viewport.apply {
            isScrollable = true
            //setScalableY(true)
            //isScalable = true
            setScrollableY(true)
        }
        rootSeries.appendData(DataPoint(0.0,0.0),true,50)
        rootSeries.shape = PointsGraphSeries.Shape.POINT
        rootSeries.size = 10.0f
        rootSeries.color = Color.WHITE
        graph.addSeries(rootSeries)
    }
    private fun showGraphWithM(m : Double) {
        val lineSeries: LineGraphSeries<DataPoint> = LineGraphSeries()
        val pointSeries: PointsGraphSeries<DataPoint> = PointsGraphSeries()
        val seriesMaxX: LineGraphSeries<DataPoint> = LineGraphSeries()
        val seriesMaxY: LineGraphSeries<DataPoint> = LineGraphSeries()
        val seriesMinX: LineGraphSeries<DataPoint> = LineGraphSeries()
        val seriesMinY: LineGraphSeries<DataPoint> = LineGraphSeries()
        val nonMLatexExp = MathUtils.removeM(m, expression)
        graph.viewport.apply {
            isYAxisBoundsManual = true
            isXAxisBoundsManual = true
            setMinY(-7.0)
            setMaxY(7.0)
            setMinX(-7.0)
            setMaxX(7.0)
        }
        graph.viewport.apply {
            isScrollable = true
            //setScalableY(true)
            //isScalable = true
            setScrollableY(true)
        }
        var x = -25.1
        var y: Double
        for (i in 1..500) {
            x += 0.1
            y = MathUtils.calculateY(x,nonMLatexExp)
            lineSeries.appendData(DataPoint(x, y), true, 1000)
        }
        lineSeries.color = Color.WHITE
        pointSeries.appendData(DataPoint(0.0,0.0),true,50)
//        var listPoints=MathUtils.getListPointDerive(expression)
//        for(point in listPoints){
//            val pointx=point;
//            val pointy=MathUtils.calculateY(pointx,nonMLatexExp)
//            pointSeries.appendData(DataPoint(pointx,pointy),true,50)
//        }
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
    private val onProgressChangeListener = object : DiscreteSeekBar.OnProgressChangeListener {
        override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
            val m = (seekBar.progress - 100).toDouble() / 10
            cur_m.text = String.format("m = %.2f", m)
            graph.removeAllSeries()
            showGraphWithM(m)
            val format = m.toString()
            seekBar.setIndicatorFormatter(format)
        }
        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
            val format = ((seekBar.progress - 100).toDouble() / 10).toString()
            seekBar.setIndicatorFormatter(format)
        }
        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) = Unit
    }
}