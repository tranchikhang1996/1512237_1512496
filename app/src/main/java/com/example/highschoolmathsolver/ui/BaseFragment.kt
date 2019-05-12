package com.example.highschoolmathsolver.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.util.DialogHelper
import com.example.highschoolmathsolver.viewmodel.ViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    internal val mCompositeDisposable = CompositeDisposable()

    @Inject
    internal lateinit var viewModelFactory : ViewModelFactory
    internal abstract fun setupFragmentComponent()

    abstract val requestLayoutID : Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = inflater.inflate(requestLayoutID, container, false)
        setupFragmentComponent()
        return view
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    private fun showErrorDialog(message : String) {
        DialogHelper.showError(activity = activity, message = message)
    }

    fun showLoading() {
        DialogHelper.showLoading(activity)
    }

    fun hideLoading() {
        DialogHelper.hideLoading()
    }

    fun showError(resourceId: Int) {
        showError(getString(resourceId))
    }

    fun showError(message: String) {
        showErrorDialog(message)
    }

    fun showConfirmDialog(message: String, pMsg : String = "OK", nMsg : String = "Cancel", positiveAction : () -> Unit, negativeAction: () -> Unit) {
        DialogHelper.showNotifyDialog(activity = activity, message = message, positiveTitle = pMsg, negativeTitle = nMsg, positiveAction = positiveAction, negativeAction =  negativeAction)
    }
    fun getUserComponent() : UserComponent = AndroidApplication.instance.userComponent
}