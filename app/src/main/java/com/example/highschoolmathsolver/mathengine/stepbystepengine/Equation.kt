package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.util.MathUtils.Companion.solveEquationMxParser
import com.example.highschoolmathsolver.util.MathUtils.Companion.trimToKaTeX
import com.example.highschoolmathsolver.util.MathUtils.Companion.trimToMxParserFx
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function

class Equation(_latexExpression : String) : MathType(_latexExpression){
    override  fun solution(): List<String>{
        val rs= arrayListOf<String>()
        val katexExpression= trimToKaTeX(latexExpression)
        val b0= "<h3>- Theo đề ta có phương trình: $katexExpression<br>"//+ trimToMxParserFx(katexExpression)+"<br>"
        var b1="<h3>- Giải phương trình ta có :"
        val mxParserFx= trimToMxParserFx(katexExpression)
        val listRs= solveEquationMxParser(mxParserFx)
        if(listRs.size==0){
            b1+="<br>Phương trình vô nghiệm"
        }else{
            b1+="<br>Phương trình có các nghiệm:"
            for(item in listRs){
                b1+= "$$(x=$item)$$"
            }
        }
        rs.add("$b0</h3>")
        rs.add("$b1</h3>")
        val b2= "STATIC_GRAPH_FOR_EQUATION$katexExpression"
        rs.add(b2)
        return rs
    }
}