package com.example.highschoolmathsolver.mathengine.expression

import kotlin.math.pow

class MonomialExp(_a: Double, _n: Int) : FormalExpression {
    private var a: Double = _a
    private var n: Int = _n

    companion object {
        public fun creatInstance(_a: Double, _n: Int): FormalExpression {
            if (_a == 0.0 || _n == 0) {
                return ConstExp(_a)
            }
            return MonomialExp(_a, _n)
        }
    }

    override fun evalute(x: Double): Double {
        return this.a * (x.pow(this.n));
    }

    override fun derive(): FormalExpression {
        var aDerive: Double = this.a * this.n
        var nDerive: Int = this.n - 1;
        return MonomialExp.creatInstance(aDerive, nDerive)
    }
    override fun expToString(): String {
        if (this.a == 0.0) {
            return ""
        } else if (this.a == 1.0) {
            return "x^" + this.n
        } else if(this.a==-1.0) {
            return "-x^"+this.n
        }
        else if (this.n == 1) {
            return this.a.toString() + "x"
        } else {
            return this.a.toString() + "x^" + this.n.toString()
        }
    }
}