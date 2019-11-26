package com.maruchin.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.AppBottomSheetDialog
import com.maruchin.medihelper.databinding.DialogLocalOrRemoteDataBinding
import com.maruchin.medihelper.presentation.framework.bind

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