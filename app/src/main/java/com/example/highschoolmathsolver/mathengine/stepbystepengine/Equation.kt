package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.util.MathUtils.Companion.trimToKaTeX

class Equation(_latexExpression : String) : MathType(_latexExpression){
    override fun solution(): List<String>{
        val rs= arrayListOf<String>()
        val b0="<h3>- Theo đề ta có phương trình: "+ trimToKaTeX(latexExpression)
        rs.add("$b0</h3>")
        val b1="<h3>- Giải phương trình ta có các nghiệm sau : </h3>"
        rs.add(b1)
        return rs
    }
}