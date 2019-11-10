package com.example.medihelper.mainapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.medihelper.R
import com.example.medihelper.custom.AppDialog

class LoadingDialog : AppDialog() {
    private val TAG = "LoadingDialog"

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
    }
}