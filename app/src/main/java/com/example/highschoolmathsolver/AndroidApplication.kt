package com.example.highschoolmathsolver

import android.app.Application
import androidx.room.Room
import com.example.highschoolmathsolver.di.component.DaggerUserComponent
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.model.database.ExpressionDatabase
import timber.log.Timber



class AndroidApplication : Application() {

    companion object {
        const val DATABASE_NAME = "expression_history_database"
        lateinit var instance : AndroidApplication
    }

    lateinit var userComponent : UserComponent
    lateinit var database : ExpressionDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this
        userComponent = DaggerUserComponent.builder().build()
        database = Room.databaseBuilder(applicationContext, ExpressionDatabase::class.java, DATABASE_NAME).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}