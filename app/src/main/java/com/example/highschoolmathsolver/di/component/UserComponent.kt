package com.example.highschoolmathsolver.di.component

import com.example.highschoolmathsolver.detector.SCFGDetector
import com.example.highschoolmathsolver.di.module.AppModule
import com.example.highschoolmathsolver.di.module.ViewModelModule
import com.example.highschoolmathsolver.ui.history.fragment.HistoryFragment
import com.example.highschoolmathsolver.ui.home.activity.HomeActivity
import com.example.highschoolmathsolver.ui.scan.fragment.ScanFragment
import com.example.highschoolmathsolver.ui.solution.fragment.SolutionFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface UserComponent {
    fun inject(fragment: ScanFragment)
    fun inject(activity: HomeActivity)
    fun inject(fragment: SolutionFragment)
    fun inject(fragment: HistoryFragment)
    fun getDetector() : SCFGDetector
    fun inject(fragment: SolutionDialogFragment)
    fun inject(fragment: GraphDialog)
}