package com.example.highschoolmathsolver.model.repository

import com.example.highschoolmathsolver.model.entity.Expression
import io.reactivex.Completable
import io.reactivex.Single

interface IDatabaseRepository {
    fun save(expression : Expression) : Completable
    fun getAllExpressions() : Single<MutableList<Expression>>
    fun deleteAll() : Completable
    fun deleteItem(expression: Expression) : Completable
}