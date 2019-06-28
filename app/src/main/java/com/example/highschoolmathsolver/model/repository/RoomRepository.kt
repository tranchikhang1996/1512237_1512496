package com.example.highschoolmathsolver.model.repository

import com.example.highschoolmathsolver.model.database.ExpressionDatabase
import com.example.highschoolmathsolver.model.entity.Expression
import io.reactivex.Completable
import io.reactivex.Single

class RoomRepository constructor(private val database : ExpressionDatabase) : IDatabaseRepository {
    override fun deleteItem(expression: Expression) : Completable {
        val dao = database.expressionDao()
        return Completable.create {
            dao.delete(expression)
            it.onComplete()
        }
    }

    override fun getAllExpressions(): Single<MutableList<Expression>> {
        val dao = database.expressionDao()
        return Single.create {
            it.onSuccess(dao.getAllExpression())
        }
    }

    override fun save(expression: Expression) : Completable {
        val dao = database.expressionDao()
        return dao.insertAll(expression)
    }

    override fun deleteAll(): Completable {
        val dao = database.expressionDao()
        return dao.deleteAllExpression()
    }

}