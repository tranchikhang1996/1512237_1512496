package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.mathengine.ISolveEngine
import io.reactivex.Observable
import javax.inject.Inject

class SolveEngine @Inject constructor(val factory : MathTypeFactory) : ISolveEngine {
    override fun solve(expression: String): Observable<List<String>> {
        //return Observable.just(arrayListOf(text,text, text, text, "DRAW_GRAPH", text, text))

        // làm xong phần giải thì mở đoạn code này ra rồi xóa đoạn code phía trên nha

        return Observable.just(expression).map { stepByStep(it) }
    }

    fun stepByStep(expression : String) : List<String> {
        // phần giải code tại đây, cần trả về list<String> các bước giải
        var myMathType=factory.getMathType(expression)
        return myMathType.solution()
    }

}