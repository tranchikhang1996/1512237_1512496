package com.example.highschoolmathsolver.util

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.highschoolmathsolver.R

class DialogHelper {
    companion object {
        var loadingDialog: Dialog? = null
        var errorDialog: AlertDialog? = null

        fun showLoading(activity: Activity?) {
            loadingDialog = activity?.let {
                val dialog = Dialog(it)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setContentView(R.layout.loading_layout)
                dialog
            }
            loadingDialog?.show()
        }

        fun hideLoading() = loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        fun showError(activity: Activity?, negetiveTitle: String, message: String = "", action: () -> Unit) {
            errorDialog = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setCancelable(false)
                builder.setMessage(message)
                builder.setNegativeButton(negetiveTitle) { dialog, _ ->
                    action()
                    dialog.cancel()
                }
                builder.create()
            }
            errorDialog?.show()
        }

        fun showError(activity: Activity?, message: String) {
            showError(activity, "Đóng", message) {}
        }
    }
}