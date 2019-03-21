package com.example.highschoolmathsolver.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.example.highschoolmathsolver.R

class DialogHelper {
    companion object {
        var loadingDialog: Dialog? = null
        var myDialog: Dialog? = null

        fun showLoading(activity: Activity?) {
            loadingDialog?.let {
                if (it.isShowing) it.dismiss()
            }
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

        fun showError(
            activity: Activity?,
            negativeTitle: String = "Cancel",
            message: String = "",
            action: () -> Unit = {}
        ) {
            myDialog?.let {
                if (it.isShowing) it.dismiss()
            }
            myDialog = activity?.let {
                val dialog = Dialog(it)
                dialog.setContentView(R.layout.error_dialog_layout)
                val negativeButton: Button = dialog.findViewById(R.id.cancel)
                val contentView: TextView = dialog.findViewById(R.id.message)

                negativeButton.apply {
                    this.text = negativeTitle
                    this.setOnClickListener {
                        action()
                        dialog.dismiss()
                    }
                }

                contentView.text = message
                dialog.setCanceledOnTouchOutside(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog
            }
            myDialog?.show()
        }

        fun showNotifyDialog(
            activity: Activity?,
            positiveTitle: String = "Ok",
            negativeTitle: String = "Cancel",
            message: String = "",
            positiveAction: () -> Unit,
            negativeAction: () -> Unit
        ) {
            myDialog?.let {
                if (it.isShowing) it.dismiss()
            }
            myDialog = activity?.let {
                val dialog = Dialog(it)
                dialog.setContentView(R.layout.custom_dialog_layout)
                val positiveButton: Button = dialog.findViewById(R.id.accept)
                val negativeButton: Button = dialog.findViewById(R.id.cancel)
                val contentView: TextView = dialog.findViewById(R.id.message)
                positiveButton.apply {
                    this.text = positiveTitle
                    this.setOnClickListener {
                        positiveAction()
                        dialog.dismiss()
                    }
                }

                negativeButton.apply {
                    this.text = negativeTitle
                    this.setOnClickListener {
                        negativeAction()
                        dialog.dismiss()
                    }
                }

                contentView.text = message
                dialog.setCanceledOnTouchOutside(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog
            }
            myDialog?.show()
        }
    }
}