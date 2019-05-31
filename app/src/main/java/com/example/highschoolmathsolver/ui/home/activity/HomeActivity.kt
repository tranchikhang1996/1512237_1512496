package com.example.highschoolmathsolver.ui.home.activity

import android.os.Bundle
import android.view.GestureDetector
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.highschoolmathsolver.R
import com.example.highschoolmathsolver.ui.BaseActivity
import com.example.highschoolmathsolver.ui.BaseFragment
import com.example.highschoolmathsolver.ui.solution.adapter.MyPagerAdapter
import com.example.highschoolmathsolver.viewmodel.SharedModel
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel : SharedModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(SharedModel::class.java) }
    private lateinit var myPagerAdapter : MyPagerAdapter
    private var currentPage = 0
    private val viewPager by lazy { view_pager }
    private lateinit var gestureDetector : GestureDetectorCompat

    private val listener = object : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_HIDE_THRESHOLD = 20
        private val SWIPE_HIDE_VELOCITY_THRESHOLD = 10
        private val SWIPE_SHOW_THRESHOLD = 400
        private val SWIPE_SHOW_VELOCITY_THRESHOLD = 50

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            val visibility = bottom_navigation_view.visibility
            bottom_navigation_view.visibility = if(visibility == View.GONE) View.VISIBLE else View.GONE
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffY = e2.y - e1.y
                if (diffY > 0) {
                    if (Math.abs(diffY) > SWIPE_HIDE_THRESHOLD && Math.abs(velocityY) > SWIPE_HIDE_VELOCITY_THRESHOLD) {
                        bottom_navigation_view.visibility = View.GONE
                    }
                }
                else {
                    if (Math.abs(diffY) > SWIPE_SHOW_THRESHOLD && Math.abs(velocityY) > SWIPE_SHOW_VELOCITY_THRESHOLD) {
                        bottom_navigation_view.visibility = View.VISIBLE
                    }
                }
            return true
        }
    }

    override fun onNavigationItemSelected(menuItem : MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.camera -> view_pager.currentItem = MyPagerAdapter.CAMERA
            R.id.solution -> view_pager.currentItem = MyPagerAdapter.SOLUTION
            R.id.history -> view_pager.currentItem = MyPagerAdapter.HISTORY
            else -> return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gestureDetector = GestureDetectorCompat(this, listener)
        view_pager.offscreenPageLimit = MyPagerAdapter.NO_TAB - 1
        myPagerAdapter = MyPagerAdapter(supportFragmentManager)
        bottom_navigation_view.setOnNavigationItemSelectedListener(this)
        view_pager.addOnPageChangeListener(onPageChangeListener)
        view_pager.adapter = myPagerAdapter
        currentPage = viewModel.getCurrentPage().value ?: MyPagerAdapter.CAMERA
        bindEvent()
    }

    private fun bindEvent() {
        viewModel.getCurrentPage().observe(this, Observer {
            if(currentPage != it) {
                viewPager.currentItem = it
                bottom_navigation_view.menu.getItem(it).isChecked = true
            }
        })
    }

    override val layoutId: Int get() = R.layout.activity_home

    override fun setupComponent() {
        getUserComponent().inject(this)
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

        override fun onPageSelected(position: Int) {
            val newPage = myPagerAdapter.getPage(position)
            if (newPage is BaseFragment) {
                newPage.onResume()
            }
            val prePage = myPagerAdapter.getPage(currentPage)
            if (prePage is BaseFragment) {
                prePage.onPause()
            }
            currentPage = position
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

}
