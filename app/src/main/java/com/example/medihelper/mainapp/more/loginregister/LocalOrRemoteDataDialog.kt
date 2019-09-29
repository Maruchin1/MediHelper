package com.example.medihelper.mainapp.more.loginregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
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
        val binding: DialogLocalOrRemoteDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_local_or_remote_data,
            container,
            false
        )
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}