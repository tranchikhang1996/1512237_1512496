package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.util.MathUtils

class Trigonometric(_latexExpression: String) : MathType(_latexExpression) {
    override fun solution(): List<String> {
        val rs = arrayListOf<String>()
        var b0 = "<h3>- Theo đề ta có phương trình lượng giác: " + MathUtils.trimToKaTeX(latexExpression) + "<br>"
        b0 += "Hiện ứng dụng chưa hỗ trợ dạng toán này, vui lòng thử các dạng toán khác."
        rs.add("$b0</h3>")
        return rs
    }
}