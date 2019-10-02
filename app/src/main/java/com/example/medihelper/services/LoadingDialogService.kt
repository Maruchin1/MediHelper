package com.example.medihelper.services

import androidx.fragment.app.FragmentManager
import com.example.medihelper.dialogs.LoadingDialog

class LoadingDialogService {

    private var loadingDialog: LoadingDialog? = null

    fun showLoadingDialog(fragmentManager: FragmentManager) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(fragmentManager)
        }
    }

    fun dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }
}