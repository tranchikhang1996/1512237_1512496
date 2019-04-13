package com.example.highschoolmathsolver.mathengine.expression

class ConstExp(_value: Double): FormalExpression {
    private var value: Double=_value
    override public fun evalute(x: Double): Double {
        return this.value
    }

    override fun derive(): FormalExpression{
        return ConstExp(0.0)
    }

    override fun expToString(): String{
        return this.value.toString()
    }
}