package com.example.highschoolmathsolver.ui.scan.fragment

import android.os.Bundle
import android.view.View
import com.example.highschoolmathsolver.presenter.scan.CustomCameraPresenter
import com.example.highschoolmathsolver.ui.RuntimePermissionFragment
import com.example.highschoolmathsolver.ui.scan.view.CustomCameraView
import com.example.highschoolmathsolver.util.DialogHelper
import kotlinx.android.synthetic.main.kyc_camera_preview.*
import java.util.concurrent.Semaphore
import javax.inject.Inject

abstract class BaseCustomCameraFragment : RuntimePermissionFragment(),
    CustomCameraView {

    @Inject
    internal lateinit var mPresenter: CustomCameraPresenter
    protected var hasRequestedCameraPermission: Boolean = false
    protected val mCameraOpenCloseLock = Semaphore(1)

    abstract fun requestPermission()

    abstract fun freeCamera()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.resume()
    }

    override fun initView() {
        setControllability(false)
    }

    override fun permissionGranted(permissionRequestCode: Int, isGranted: Boolean) {
        hasRequestedCameraPermission = true
        when (permissionRequestCode) {
            CAMERA -> permission_denied_panel.visibility = if (isGranted) View.GONE else View.VISIBLE
            else -> Unit
        }
    }

    override fun showLoading() {
        DialogHelper.showLoading(activity)
    }

    override fun hideLoading() {
        DialogHelper.hideLoading()
    }

    override fun showError(resourceId: Int) {
        showError(getString(resourceId))
    }

    override fun showError(message: String) {
        showErrorDialog(message)
    }

    override fun onPause() {
        mPresenter.pause()
        hideLoading()
        freeCamera()
        super.onPause()
    }

    override fun onDestroyView() {
        if (mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
        mPresenter.destroy()
        super.onDestroyView()
    }

    override fun getData(): Bundle? = arguments

    internal fun setControllability(clickable: Boolean) {

    }

    override fun saveExpression(expression : String) {
        model.saveData(expression)
    }

    override fun solve(expression : String) {
        model.execute(expression)
    }
}