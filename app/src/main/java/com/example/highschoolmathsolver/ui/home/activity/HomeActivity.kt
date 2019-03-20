package com.example.highschoolmathsolver.ui.home.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.MenuItem
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.presenter.home.HomePresenter
import com.example.highschoolmathsolver.ui.BaseActivity
import com.example.highschoolmathsolver.ui.solution.adapter.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    override fun onNavigationItemSelected(menuItem : MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.calculator -> view_pager.currentItem = MyPagerAdapter.CALCULATOR
            R.id.camera -> view_pager.currentItem = MyPagerAdapter.CAMERA
            R.id.solution -> view_pager.currentItem = MyPagerAdapter.SOLUTION
            R.id.history -> view_pager.currentItem = MyPagerAdapter.HISTORY
            else -> return false
        }
        return true
    }


    @Inject
    internal lateinit var mPresenter : HomePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view_pager.offscreenPageLimit = MyPagerAdapter.NO_TAB
        val myPagerAdapter = MyPagerAdapter(supportFragmentManager)
        bottom_navigation_view.setOnNavigationItemSelectedListener(this)
        view_pager.adapter = myPagerAdapter
    }

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun setupComponent() {
        getUserComponent().inject(this)
    }
}
