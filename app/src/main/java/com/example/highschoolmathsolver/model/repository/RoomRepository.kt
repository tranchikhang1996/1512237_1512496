package com.example.highschoolmathsolver.model.repository

import com.example.highschoolmathsolver.model.database.ExpressionDatabase
import com.example.highschoolmathsolver.model.entity.Expression
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RoomRepository constructor(val database : ExpressionDatabase) : IDatabaseRepository {

    override fun getAllExpressions(): Observable<List<Expression>> {
        val dao = database.expressionDao()
        return dao.getAllExpression()
    }

    override fun save(expression: Expression) : Completable {
        val dao = database.expressionDao()
        return dao.insertAll(expression)
    }
}