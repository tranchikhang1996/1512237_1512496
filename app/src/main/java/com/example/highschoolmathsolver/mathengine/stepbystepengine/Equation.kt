package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.util.MathUtils.Companion.solveEquationMxParser
import com.example.highschoolmathsolver.util.MathUtils.Companion.trimToKaTeX
import com.example.highschoolmathsolver.util.MathUtils.Companion.trimToMxParserFx
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function

class Equation(_latexExpression: String) : MathType(_latexExpression) {
    override fun solution(): List<String> {
        var rs = arrayListOf<String>()
        var katexExpression = trimToKaTeX(latexExpression)
        var b0 =
            "<h3>- Theo đề ta có phương trình: $katexExpression<br>"//+ trimToMxParserFx(katexExpression)+"<br>"
        var b1 = "<h3>- Giải phương trình ta có :"
        var mxParserFx = trimToMxParserFx(katexExpression)
        var listRs = solveEquationMxParser(mxParserFx)
        if (listRs.size == 0) {
            b1 += "<br>Phương trình vô nghiệm"
        } else {
            b1 += "<br>Phương trình có các nghiệm:"
            for (item in listRs) {
                b1 += "$$(x=$item)$$"
            }
        }
        rs.add("$b0</h3>")
        rs.add("$b1</h3>")
        var b2 = "STATIC_GRAPH_FOR_EQUATION$latexExpression"
        rs.add(b2)
        return rs
    }
}