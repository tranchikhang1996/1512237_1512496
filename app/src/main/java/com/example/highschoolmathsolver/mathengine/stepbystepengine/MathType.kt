package com.example.highschoolmathsolver.mathengine.stepbystepengine

abstract class MathType(_latexExpression: String) {
    protected var latexExpression: String=_latexExpression
    abstract fun solution(): List<String>
}