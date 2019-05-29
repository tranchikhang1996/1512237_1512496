package com.example.highschoolmathsolver.mathengine.stepbystepengine

class MathTypeFactory() {
    companion object {
        public fun getMathType(latexExpression: String): MathType {
            if (latexExpression == "") {
                return Constan("",0.0)
            } else {
                return Constan(latexExpression,0.0)
            }
        }
    }
}