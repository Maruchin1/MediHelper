package com.example.medihelper.presentation.feature.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.DialogNewPasswordBinding
import com.example.medihelper.presentation.feature.options.NewPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPasswordDialog : AppBottomSheetDialog() {
    override val TAG = "NewPasswordDialog"

    private val viewModel: NewPasswordViewModel by viewModel()
    private var newPasswordSelectedListener: ((newPassword: String) -> Unit)? = null

    fun onClickConfirm() {
        val validNewPassword = viewModel.getValidNewPassword()
        if (validNewPassword != null) {
            newPasswordSelectedListener?.invoke(validNewPassword)
            dismiss()
        }
    }

    fun setNewPasswordSelectedListener(listener: (newPassword: String) -> Unit) {
        newPasswordSelectedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogNewPasswordBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_new_password,
            viewModel = viewModel
        )
    }
}