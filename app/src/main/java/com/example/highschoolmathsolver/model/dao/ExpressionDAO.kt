package com.example.highschoolmathsolver.model.dao

import androidx.room.*
import com.example.highschoolmathsolver.model.entity.Expression
import io.reactivex.Completable

@Dao
interface ExpressionDAO {
    @Query("SELECT * FROM Expression ORDER BY id DESC LIMIT 10")
    fun getAllExpression() : MutableList<Expression>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg expression: Expression) : Completable

    @Delete
    fun delete(expression: Expression)

    @Query("DELETE FROM Expression")
    fun deleteAllExpression() : Completable
}