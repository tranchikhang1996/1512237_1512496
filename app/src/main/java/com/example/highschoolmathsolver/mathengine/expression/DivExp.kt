package com.example.highschoolmathsolver.mathengine.expression

class DivExp(_exp1: FormalExpression,_exp2: FormalExpression): BinaryExp(_exp1,_exp2) {
    override fun evalute(x: Double): Double {
        return this.exp1.evalute(x)+this.exp2.evalute(x)
    }

    override fun derive(): FormalExpression {
        return DivExp(AddExp(MultiExp(this.exp1.derive(),this.exp2),MultiExp(this.exp1,this.exp2.derive())),MultiExp(exp2,exp2))
    }

    override fun getOperator(): String {
        return "/"
    }
}