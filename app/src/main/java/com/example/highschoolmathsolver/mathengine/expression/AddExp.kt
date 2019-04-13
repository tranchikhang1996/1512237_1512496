package com.example.highschoolmathsolver.mathengine.expression

class AddExp(_exp1: FormalExpression,_exp2: FormalExpression): BinaryExp(_exp1,_exp2) {
    override fun evalute(x: Double): Double {
        return this.exp1.evalute(x)+this.exp2.evalute(x)
    }

    override fun derive(): FormalExpression {
        return AddExp(this.exp1.derive(),this.exp2.derive())
    }

    override fun getOperator(): String {
        return "+"
    }
}