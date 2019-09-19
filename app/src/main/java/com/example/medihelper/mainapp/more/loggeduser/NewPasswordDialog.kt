package com.example.medihelper.mainapp.more.loggeduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.databinding.DialogNewPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPasswordDialog : AppBottomSheetDialog() {
    override val TAG = "NewPasswordDialog"

    private val viewModel: NewPasswordViewModel by viewModel()
    private var newPasswordSelectedListener: ((newPassword: String) -> Unit)? = null

    fun onClickConfirm() {
        if (viewModel.validatePasswordsInputData()) {
            newPasswordSelectedListener?.invoke(viewModel.newPasswordLive.value!!)
            dismiss()
        }
    }

    fun setNewPasswordSelectedListener(listener: (newPassword: String) -> Unit) {
        newPasswordSelectedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogNewPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_new_password, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}