package com.example.highschoolmathsolver.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class PermissionUtils {
    companion object {
        fun verifyPermission(grantResult: IntArray): Boolean {
            if (grantResult.isEmpty()) {
                return false
            }

            for (result in grantResult) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }

            return true
        }

        fun checkAndroidMVersion(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


        fun getSelfPermission(context: Context?, permission: Array<String>): IntArray {
            context ?: return intArrayOf()

            val selfPermission = IntArray(permission.size)
            for (i in permission.indices) {
                selfPermission[i] = ContextCompat.checkSelfPermission(context, permission[i])
            }

            return selfPermission
        }


        fun verifyPermission(context: Context?, permission: Array<String>): Boolean {
            context ?: return false

            val selfPermission = IntArray(permission.size)
            for (i in permission.indices) {
                selfPermission[i] = ContextCompat.checkSelfPermission(context, permission[i])
            }

            return verifyPermission(selfPermission)
        }
    }
}