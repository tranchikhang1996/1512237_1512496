package com.example.highschoolmathsolver.mathengine.expression

abstract class BinaryExp(_exp1: FormalExpression,_exp2: FormalExpression):FormalExpression {
    protected var exp1: FormalExpression=_exp1
    protected var exp2: FormalExpression=_exp2
    override fun expToString(): String {
        var op: String=getOperator()
        var opr1:String=exp1.expToString()
        var opr2:String=exp2.expToString()
        return opr1+op+opr2
    }
    //protected abstract fun doEvaluate(val1: Double, val2: Double): Double
    protected abstract fun getOperator(): String
}