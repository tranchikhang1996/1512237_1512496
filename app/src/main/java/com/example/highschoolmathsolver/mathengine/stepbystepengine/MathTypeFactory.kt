package com.example.highschoolmathsolver.mathengine.stepbystepengine

class MathTypeFactory {
    fun getMathType(latexExpression: String): MathType {
        val regex_constan = "^y{1}".toRegex()
        val regex_equation = "={1}".toRegex()
        val regex_inequation = "<{1}|>{1}|leqslant{1}|geqslant{1}".toRegex()
        val regex_lim = "lim{1}".toRegex()
        val regex_integral = "dx$".toRegex()

        return when {
            regex_constan.containsMatchIn(latexExpression) -> Constan(latexExpression)
            regex_equation.containsMatchIn(latexExpression) -> {
                val regex_trigo = "sin|cos|tan|cot".toRegex()
                if (regex_trigo.containsMatchIn(latexExpression)) {
                    Trigonometric(latexExpression)
                } else {
                    Equation(latexExpression)
                }
            }
            regex_inequation.containsMatchIn(latexExpression) -> InEquation(latexExpression)
            regex_lim.containsMatchIn(latexExpression) -> Limit(latexExpression)
            regex_integral.containsMatchIn(latexExpression) -> Integral(latexExpression)
            else -> UnSupportedMathType(latexExpression)
        }
    }
}