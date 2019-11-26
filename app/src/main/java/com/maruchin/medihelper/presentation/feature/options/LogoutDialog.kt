package com.maruchin.medihelper.presentation.feature.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.AppBottomSheetDialog
import com.maruchin.medihelper.databinding.DialogLogoutBinding
import com.maruchin.medihelper.presentation.framework.bind

class LogoutDialog : AppBottomSheetDialog() {

    override val TAG: String
        get() = "LogoutDialog"

    private var onLogoutSelectedListener: ((clearLocalData: Boolean) -> Unit)? = null

    fun onClickKeepLocalData() {
        onLogoutSelectedListener?.invoke(false)
        dismiss()
    }

    fun onClickDeleteLocalData() {
        onLogoutSelectedListener?.invoke(true)
        dismiss()
    }

    fun setOnLogoutSelectedListener(listener: (clearLocalData: Boolean) -> Unit) {
        onLogoutSelectedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogLogoutBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_logout
        )
    }
}