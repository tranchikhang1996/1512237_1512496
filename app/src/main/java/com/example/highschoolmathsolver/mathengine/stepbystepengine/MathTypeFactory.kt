package com.example.highschoolmathsolver.mathengine.stepbystepengine

class MathTypeFactory {
    public fun getMathType(latexExpression: String): MathType?{
        if(latexExpression==""){
            return null
        }
        else{
            return Constan(latexExpression)
        }
        return null
    }
}