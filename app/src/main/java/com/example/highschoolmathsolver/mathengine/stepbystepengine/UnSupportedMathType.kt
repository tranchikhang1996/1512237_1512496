package com.example.highschoolmathsolver.mathengine.stepbystepengine

class UnSupportedMathType(_latexExpression: String) : MathType(_latexExpression) {
    override fun solution(): List<String> {
        return arrayListOf()
    }
}