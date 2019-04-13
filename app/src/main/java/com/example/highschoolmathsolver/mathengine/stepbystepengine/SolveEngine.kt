package com.example.highschoolmathsolver.mathengine.stepbystepengine

import com.example.highschoolmathsolver.mathengine.ISolveEngine
import io.reactivex.Observable

class SolveEngine : ISolveEngine {
    override fun solve(expression: String): Observable<List<String>> {

        // Đoạn code này chỉ dùng để test
        val  text = "This come from string. You can insert inline formula:" +
                " \\(ax^2 + bx + c = 0\\) " +
                "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$"

        return Observable.just(arrayListOf(text, text, text, text, "DRAW_GRAPH", text, text))

        // làm xong phần giải thì mở đoạn code này ra rồi xóa đoạn code phía trên nha

//        return Observable.just(expression).map { stepByStep(it) }
    }

    fun stepByStep(expression : String) : String {

        // phần giải code tại đây, cần trả về list<String> các bước giải
        return ""
    }

}