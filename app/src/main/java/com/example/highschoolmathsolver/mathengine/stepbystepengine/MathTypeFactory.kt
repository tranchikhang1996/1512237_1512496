package com.example.highschoolmathsolver.mathengine.stepbystepengine

class MathTypeFactory() {
    public fun getMathType(latexExpression: String): MathType {
        if (latexExpression == "") {
            return Constan("")
        } else {
            return Constan(latexExpression)
        }
    }
}