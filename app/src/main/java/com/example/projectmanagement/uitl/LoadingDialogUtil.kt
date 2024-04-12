package com.example.projectmanagement.uitl

import android.content.Context
import com.example.projectmanagement.ui.activity.CustomLoadingDialog

object LoadingDialogUtil {
    private var loadingDialog: CustomLoadingDialog? = null

    fun showLoadingDialog(context: Context) {
        if (loadingDialog == null) {
            loadingDialog = CustomLoadingDialog(context)
        }
        loadingDialog?.show()
    }

    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}
