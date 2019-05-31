package com.example.highschoolmathsolver.di.module

import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.detector.config.ConfigurationProvider
import com.example.highschoolmathsolver.detector.data.Gmm
import dagger.Module
import dagger.Provides
import org.opencv.ml.ANN_MLP
import javax.inject.Singleton

@Module
class DetectorModule {
    @Singleton
    @Provides
    fun provideMLP(): ANN_MLP = AndroidApplication.instance.mlp

    @Singleton
    @Provides
    fun provideLable(): Map<Int, String> = AndroidApplication.instance.labelTable

    @Singleton
    @Provides
    fun provideGmm(): Gmm = AndroidApplication.instance.gmm

    @Singleton
    @Provides
    fun provideConfigurationProvider(): ConfigurationProvider = ConfigurationProvider()

}