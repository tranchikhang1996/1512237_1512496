package com.example.highschoolmathsolver.di.module

import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.mathengine.ISolveEngine
import com.example.highschoolmathsolver.mathengine.stepbystepengine.MathTypeFactory
import com.example.highschoolmathsolver.mathengine.stepbystepengine.SolveEngine
import com.example.highschoolmathsolver.model.database.ExpressionDatabase
import com.example.highschoolmathsolver.model.repository.IDatabaseRepository
import com.example.highschoolmathsolver.model.repository.RoomRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideSolveEngine(factory: MathTypeFactory): ISolveEngine {
        return SolveEngine(factory)
    }

    @Singleton
    @Provides
    fun provideExpressionDatabase(): ExpressionDatabase {
        return AndroidApplication.instance.database
    }

    @Singleton
    @Provides
    fun provideRoomDatabaseRepository(database: ExpressionDatabase): IDatabaseRepository {
        return RoomRepository(database)
    }

    @Singleton
    @Provides
    fun provideMathTypeFactory() : MathTypeFactory{
        return MathTypeFactory()
    }
}