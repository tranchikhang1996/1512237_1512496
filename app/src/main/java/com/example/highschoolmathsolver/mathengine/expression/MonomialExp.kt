package com.example.highschoolmathsolver.mathengine.expression

import kotlin.math.pow

class MonomialExp(_a: Double, _n: Int) : FormalExpression {
    private var a: Double = _a
    private var n: Int = _n

    companion object {
        fun creatInstance(_a: Double, _n: Int): FormalExpression =
            if (_a == 0.0 || _n == 0) ConstExp(_a) else MonomialExp(_a, _n)
    }

    override fun evalute(x: Double): Double {
        return this.a * (x.pow(this.n))
    }

    override fun derive(): FormalExpression {
        val aDerive: Double = this.a * this.n
        val nDerive: Int = this.n - 1
        return creatInstance(aDerive, nDerive)
    }

    override fun expToString(): String = when {
        this.a == 0.0 -> ""
        this.a == 1.0 -> "x^" + this.n
        this.a == -1.0 -> "-x^" + this.n
        this.n == 1 -> this.a.toString() + "x"
        else -> this.a.toString() + "x^" + this.n.toString()
    }
}