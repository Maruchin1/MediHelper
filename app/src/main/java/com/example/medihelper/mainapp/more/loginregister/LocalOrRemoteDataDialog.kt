package com.example.medihelper.mainapp.more.loginregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.DialogLocalOrRemoteDataBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LocalOrRemoteDataDialog : AppBottomSheetDialog() {
    override val TAG = "LocalOrRemoteDataDialog"

    private val viewModel: LoginRegisterViewModel by sharedViewModel(from = { requireParentFragment() })

    fun onClickUseLocalData() {
        viewModel.useLocalDataAfterLogin()
        dismiss()
    }

    fun onClickUseRemoteData() {
        viewModel.useRemoteDataAfterLogin()
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind<DialogLocalOrRemoteDataBinding>(
            inflater = inflater,
            layoutResId = R.layout.dialog_local_or_remote_data,
            container = container,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
    }
}