package com.example.highschoolmathsolver.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.util.MathUtils
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.solution_graph_layout.*
import kotlinx.android.synthetic.main.solution_input_layout.input_m
import kotlinx.android.synthetic.main.solution_input_layout.input_m_layout
import kotlinx.android.synthetic.main.solution_input_layout.math_view

class GraphDialog(private val expression: String) : BaseDialogFragment() {

    override val layoutId: Int get() = R.layout.solution_graph_layout

    override fun setupFragmentComponent(userComponent: UserComponent) = userComponent.inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        math_view.text = "<h3>" +MathUtils.trimToKaTeX(expression)+"</h3>"
        bindEvent()
    }

    private fun bindEvent() {
        input_m.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val m = input_m_layout.editText?.text?.toString()
                graph.removeAllSeries()
                m?.let { showGraphWithM(it) }
                input_m_layout.clearFocus()
                hideSoftKeyboard(view)
                true
            } else {
                false
            }
        }
    }

    private fun showGraphWithM(m: String) {
        var series: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesUtil: PointsGraphSeries<DataPoint> = PointsGraphSeries()
        var seriesmaxX: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesmaxY: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesminX: LineGraphSeries<DataPoint> = LineGraphSeries()
        var seriesminY: LineGraphSeries<DataPoint> = LineGraphSeries()
        var valueOfM=m.toDouble()
        var nonMLatexExp=MathUtils.removeM(valueOfM,expression)
        graph.getViewport().setYAxisBoundsManual(true)
        graph.getViewport().setXAxisBoundsManual(true)
        graph.getViewport().setMinY(-7.0)
        graph.getViewport().setMaxY(7.0)
        graph.getViewport().setMinX(-7.0)
        graph.getViewport().setMaxX(7.0)

        // Set up các thông số cho graph
        graph.getViewport().setScrollable(true)
        //graph.getViewport().setScrollableY(true)
        graph.getViewport().setScrollableY(true);

        // ------------------------------

        // Vẽ đồ thị
        var x = -25.1;
        var y = 0.0;
        for (i in 1..500) {
            x += 0.1;
            // đây là biểu thức sau khi khử m và chuyển sang dạng FormalExpression
            y = MathUtils.calculateY(x,nonMLatexExp)
            if (x==-1.0) {
                seriesUtil.appendData(DataPoint(x, y), true, 500)
            }
            series.appendData(DataPoint(x, y), true, 1000)
        }
        //-------------

        seriesUtil.appendData(DataPoint(0.0,0.0),true,500)
        seriesUtil.setShape(PointsGraphSeries.Shape.POINT)
        seriesUtil.setSize(10.0f)
        seriesUtil.setColor(Color.RED)
        series.setColor(Color.RED)
        // đồ thị
        graph.addSeries(series)
        // các giao điểm
        graph.addSeries(seriesUtil)
        //----------

        // Thêm 4 series max X, min X, max Y, min Y để tạo khung, fix lỗi bị lệch về với các hàm hướng về 1 phía
        var xminy = -30.0;
        var yminy = 0.0;
        for (i in 1..60) {
            xminy += 1;
            yminy = -100.0;
            seriesminY.appendData(DataPoint(xminy, yminy), false, 1000)
        }
        graph.addSeries(seriesminY)

        var xmaxy = -30.0;
        var ymaxy = 0.0;
        for (i in 1..60) {
            xmaxy += 1;
            ymaxy = 100.0;
            seriesmaxY.appendData(DataPoint(xmaxy, ymaxy), false, 1000)
        }
        graph.addSeries(seriesmaxY)

        var xminx = 0.0;
        var yminx = -30.0;
        for (i in 1..60) {
            xminx = -100.0;
            yminx += 1;
            seriesminX.appendData(DataPoint(xminx, yminx), false, 1000)
        }
        graph.addSeries(seriesminX)

        var xmaxx = 0.0;
        var ymaxx = -30.0;
        for (i in 1..60) {
            xmaxx += 100.0;
            ymaxx += 1;
            seriesmaxX.appendData(DataPoint(xmaxx, ymaxx), false, 1000)
        }
        graph.addSeries(seriesmaxX)
        //-------------------------------------------

    }
}