package com.maruchin.medihelper.presentation.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogLoadingBinding
import com.maruchin.medihelper.presentation.framework.BaseDialog

class LoadingScreen {

    private var loadingDialog: LoadingDialog? = null

    fun bind(fragment: Fragment, loadingInProgressLive: LiveData<Boolean>) {
        loadingInProgressLive.observe(fragment.viewLifecycleOwner, Observer { loadingInProgress ->
            if (loadingInProgress) {
                showLoadingScreen(fragment.childFragmentManager)
            } else {
                closeLoadingScreen()
            }
        })
    }

    private fun showLoadingScreen(fragmentManager: FragmentManager) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(fragmentManager)
        }
    }

    private fun closeLoadingScreen() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    class LoadingDialog : BaseDialog<DialogLoadingBinding>(R.layout.dialog_loading) {
        override val TAG: String
            get() = "LoadingDialog"

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            isCancelable = false
        }
    }
}