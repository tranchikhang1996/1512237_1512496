package com.example.highschoolmathsolver.di.module;

import com.example.highschoolmathsolver.AndroidApplication;
import com.example.highschoolmathsolver.detector.GrammarDetector;
import com.example.highschoolmathsolver.detector.IDetector;
import com.example.highschoolmathsolver.mathengine.ISolveEngine;
import com.example.highschoolmathsolver.mathengine.stepbystepengine.SolveEngine;
import com.example.highschoolmathsolver.model.database.ExpressionDatabase;
import com.example.highschoolmathsolver.model.repository.IDatabaseRepository;
import com.example.highschoolmathsolver.model.repository.RoomRepository;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule {

    @Singleton
    @Provides
    public ISolveEngine provideSolveEngine() {
        return new SolveEngine();
    }

    @Singleton
    @Provides
    public ExpressionDatabase provideExpressionDatabase() {
        return AndroidApplication.instance.database;
    }

    @Singleton
    @Provides
    public IDatabaseRepository provideRoomDatabaseRepository(ExpressionDatabase database) {
        return new RoomRepository(database);
    }

    @Singleton
    @Provides
    public IDetector provideGramarDetector() {
        return new GrammarDetector();
    }
}
