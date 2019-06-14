package com.example.highschoolmathsolver.mathengine.expression

interface FormalExpression {
    fun evalute(x: Double): Double
    fun derive(): FormalExpression
    fun expToString(): String
}