package com.example.highschoolmathsolver.di.component;

import com.example.highschoolmathsolver.di.module.AppModule;
import com.example.highschoolmathsolver.ui.home.activity.HomeActivity;
import com.example.highschoolmathsolver.ui.calculator.fragment.CalculatorFragment;
import com.example.highschoolmathsolver.ui.scan.fragment.CustomCameraImprovementFragment;
import com.example.highschoolmathsolver.ui.history.fragment.HistoryFragment;
import com.example.highschoolmathsolver.ui.solution.fragment.SolutionFragment;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component (modules = AppModule.class)
public interface UserComponent {
    void inject(CustomCameraImprovementFragment fragment);
    void inject(HomeActivity activity);
    void inject(SolutionFragment fragment);
    void inject(HistoryFragment fragment);
    void inject(CalculatorFragment fragment);
}
