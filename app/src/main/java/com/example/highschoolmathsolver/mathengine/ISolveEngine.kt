package com.example.highschoolmathsolver.mathengine

import io.reactivex.Observable

interface ISolveEngine {
    fun solve(expression : String) : Observable<List<String>>
}