package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.util.MathUtils
import com.example.highschoolmathsolver.util.MathUtils.Companion.trimToKaTeX
import org.mariuszgromada.math.mxparser.Expression

class Equation(_latexExpression : String) : MathType(_latexExpression){
    override  public fun solution(): List<String>{
        var rs= arrayListOf<String>()
        var b0="<h3>- Theo đề ta có phương trình: "+ trimToKaTeX(latexExpression)
        rs.add(b0+"</h3>")
        var b1="<h3>- Giải phương trình ta có các nghiệm sau : </h3>"
        rs.add(b1)
        return rs
    }
}