package com.example.highschoolmathsolver.mathengine.expression

class ConstExp(_value: Double): FormalExpression {
    private var value: Double=_value
    override fun evalute(x: Double): Double {
        return this.value
    }

    override fun derive(): FormalExpression{
        return ConstExp(0.0)
    }

    override fun expToString(): String = if (this.value == 0.0) "" else this.value.toString()
}