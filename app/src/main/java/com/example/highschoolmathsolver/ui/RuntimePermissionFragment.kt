package com.example.highschoolmathsolver.ui

import com.example.highschoolmathsolver.util.PermissionUtils
import timber.log.Timber
import java.util.*

abstract class RuntimePermissionFragment : BaseFragment() {
    internal abstract fun permissionGranted(permissionRequestCode: Int, isGranted: Boolean)

    companion object {
        val DENY_PERMISSION = -1
        val READ_PHONE_STATE = 11
        val PERMISSION_STORAGE = 12
        val WRITE_EXTERNAL_STORAGE = 13
        val READ_EXTERNAL_STORAGE = 14
        val CAMERA = 15
        val ACCESS_FINE_LOCATION = 18
        val ACCESS_COARSE_LOCATION = 19
    }

    internal fun isPermissionGrantedAndRequest(permissions: Array<String>, requestPermissionCode: Int): Boolean {
        if (!PermissionUtils.checkAndroidMVersion()) {
            return true
        }

        val selfPermission = PermissionUtils.getSelfPermission(context, permissions)

        if (PermissionUtils.verifyPermission(selfPermission)) {
            return true
        }
        requestPermissions(permissions, requestPermissionCode)

        return false
    }

    internal fun isPermissionGrantedAndRequest(permissions: String, requestPermissionCode: Int): Boolean =
        isPermissionGrantedAndRequest(arrayOf(permissions), requestPermissionCode)

    internal fun isPermissionGranted(permissions: Array<String>): Boolean = when {
        !PermissionUtils.checkAndroidMVersion() -> true

        else -> {
            val selfPermission = PermissionUtils.getSelfPermission(context, permissions)
            PermissionUtils.verifyPermission(selfPermission)
        }
    }

    internal fun isPermissionGranted(permissions: String): Boolean = isPermissionGranted(arrayOf(permissions))

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d(
            "Permissions result: requestCode [%s] permissions [%s] results[%s]",
            requestCode,
            Arrays.toString(permissions),
            Arrays.toString(grantResults)
        )

        if (isAdded) {
            permissionGranted(requestCode, PermissionUtils.verifyPermission(grantResults))
        }
    }
}