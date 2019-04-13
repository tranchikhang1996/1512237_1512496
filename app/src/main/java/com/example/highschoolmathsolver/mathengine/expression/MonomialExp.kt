package com.example.highschoolmathsolver.mathengine.expression

class MonomialExp(_a: Double,_n: Double) :FormalExpression{
    private var a: Double=_a
    private var n: Double=_a
    companion object {
        public fun creatInstance(_a: Double, _n: Double): FormalExpression{
            if(_a==0.0 || _n==0.0){
                return ConstExp(_a)
            }
            return MonomialExp(_a,_n)
        }
    }
    override fun evalute(x: Double): Double {
        return this.a*Math.pow(x,this.n)
    }

    override fun derive(): FormalExpression {
        var aDerive: Double=this.a*this.n
        var nDerive: Double=this.n-1;
        return MonomialExp.creatInstance(aDerive,nDerive)
    }

    override fun expToString(): String {
        return this.a.toString()+"x^"+this.n.toString()
    }
}