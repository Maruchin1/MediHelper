package com.example.medihelper.service

import androidx.fragment.app.FragmentManager
import com.example.medihelper.presentation.dialogs.LoadingDialog

interface LoadingScreenService {
    fun showLoadingScreen(fragmentManager: FragmentManager)
    fun closeLoadingScreen()
}

class LoadingScreenServiceImpl : LoadingScreenService {

    private var loadingDialog: LoadingDialog? = null

    override fun showLoadingScreen(fragmentManager: FragmentManager) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(fragmentManager)
        }
    }

    override fun closeLoadingScreen() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }
}