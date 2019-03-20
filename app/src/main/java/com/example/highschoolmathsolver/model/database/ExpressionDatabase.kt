package com.example.highschoolmathsolver.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.highschoolmathsolver.model.dao.ExpressionDAO
import com.example.highschoolmathsolver.model.entity.Expression

@Database(entities = [Expression::class], version = 1)
abstract class ExpressionDatabase : RoomDatabase(){
    abstract fun expressionDao(): ExpressionDAO
}