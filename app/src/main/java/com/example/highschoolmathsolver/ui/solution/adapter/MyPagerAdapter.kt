package com.example.highschoolmathsolver.ui.solution.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.highschoolmathsolver.ui.calculator.fragment.CalculatorFragment
import com.example.highschoolmathsolver.ui.scan.fragment.CustomCameraImprovementFragment
import com.example.highschoolmathsolver.ui.history.fragment.HistoryFragment
import com.example.highschoolmathsolver.ui.solution.fragment.SolutionFragment

class MyPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {

    companion object {
        const val NO_TAB = 4
        const val CALCULATOR = 0
        const val CAMERA = 1
        const val SOLUTION = 2
        const val HISTORY = 3
    }
    override fun getItem(position: Int): androidx.fragment.app.Fragment = when(position) {
        CALCULATOR -> CalculatorFragment()
        CAMERA -> CustomCameraImprovementFragment()
        SOLUTION -> SolutionFragment()
        HISTORY -> HistoryFragment()
        else -> androidx.fragment.app.Fragment()
    }

    override fun getCount(): Int = NO_TAB
}