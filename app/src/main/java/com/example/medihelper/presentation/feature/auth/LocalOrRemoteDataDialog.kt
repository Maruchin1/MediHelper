package com.example.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.example.medihelper.presentation.framework.AppBottomSheetDialog
import com.example.medihelper.databinding.DialogLocalOrRemoteDataBinding
import com.example.medihelper.presentation.framework.bind

class LocalOrRemoteDataDialog : AppBottomSheetDialog() {

    override val TAG: String
        get() = "LocalOrRemoteDataDialog"

    fun onClickUseLocalData() {

    }

    fun onClickUseRemoteData() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogLocalOrRemoteDataBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_local_or_remote_data
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
    }
}