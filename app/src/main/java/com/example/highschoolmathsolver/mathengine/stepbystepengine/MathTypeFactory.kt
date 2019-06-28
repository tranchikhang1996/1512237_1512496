package com.example.highschoolmathsolver.mathengine.stepbystepengine

class MathTypeFactory() {
    fun getMathType(latexExpression: String): MathType {
        var input = latexExpression
        val regex_constan = "^y{1}".toRegex()
        val regex_equation = "={1}".toRegex()
        val regex_inequation = "lt{1}|gt{1}|leq{1}|geq{1}".toRegex()
        val regex_lim = "lim{1}".toRegex()
        val regex_integral = "dx$".toRegex()
        if (regex_constan.containsMatchIn(input = input)) {
            return Constan(latexExpression)
        } else if (regex_equation.containsMatchIn(input = input) &&
            !regex_integral.containsMatchIn(input = input)
        ) {
            val regex_trigo = "sin|cos|tan|cot".toRegex()
            if (regex_trigo.containsMatchIn(input = input)) {
                return Trigonometric(latexExpression)
            } else {
                return Equation(latexExpression)
            }
        } else if (regex_inequation.containsMatchIn(input = input)) {
            return Inequation(latexExpression)
        } else if (regex_lim.containsMatchIn(input = input)) {
            return Limit(latexExpression)
        } else if (regex_integral.containsMatchIn(input = input)) {
            return Integral(latexExpression)
        } else {
            return UnSupportedMathType(latexExpression)
        }
    }
}