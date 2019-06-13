package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.mathengine.ISolveEngine
import io.reactivex.Observable
import javax.inject.Inject

class SolveEngine @Inject constructor(val factory : MathTypeFactory) : ISolveEngine {
    override fun solve(expression: String): Observable<List<String>> {
        return Observable.just(expression).map {
            stepByStep(it.replace(" ",""))
        }
    }

    private fun stepByStep(expression : String) : List<String> {
        val myMathType=factory.getMathType(expression)
        return myMathType.solution()
    }

}