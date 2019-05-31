package com.example.highschoolmathsolver.ui.solution.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.highschoolmathsolver.ui.scan.fragment.ScanFragment
import com.example.highschoolmathsolver.ui.history.fragment.HistoryFragment
import com.example.highschoolmathsolver.ui.solution.fragment.SolutionFragment
import java.lang.ref.WeakReference

class MyPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val holder: SparseArrayCompat<WeakReference<Fragment>>

    init {
        this.holder = SparseArrayCompat(count)
    }

    companion object {
        const val NO_TAB = 3
        const val CAMERA = 0
        const val SOLUTION = 1
        const val HISTORY = 2
    }
    override fun getItem(position: Int): Fragment = when(position) {
        CAMERA -> ScanFragment.newInstance()
        SOLUTION -> SolutionFragment()
        HISTORY -> HistoryFragment()
        else -> Fragment()
    }

    override fun getCount(): Int = NO_TAB

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = super.instantiateItem(container, position)
        if (item is Fragment) {
            holder.put(position, WeakReference(item))
        }
        return item
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj : Any) {
        holder.remove(position)
        super.destroyItem(container, position, obj)
    }

    fun getPage(position: Int): Fragment? {
        val weakRefItem = holder.get(position)
        return weakRefItem?.get()
    }
}