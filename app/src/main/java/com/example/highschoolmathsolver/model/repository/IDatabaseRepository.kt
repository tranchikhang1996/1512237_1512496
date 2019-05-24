package com.example.highschoolmathsolver.model.repository

import com.example.highschoolmathsolver.model.entity.Expression
import io.reactivex.Completable
import io.reactivex.Observable

interface IDatabaseRepository {
    fun save(expression : Expression) : Completable
    fun getAllExpressions() : Observable<MutableList<Expression>>
    fun deleteAll() : Completable
}