package com.example.medihelper.mainapp.more.patronconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.example.medihelper.custom.AppDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.DialogConnectSuccessBinding

class ConnectSuccessDialog : AppDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogConnectSuccessBinding>(
            inflater = inflater,
            layoutResId = R.layout.dialog_connect_success,
            container = container
        )
    }
}