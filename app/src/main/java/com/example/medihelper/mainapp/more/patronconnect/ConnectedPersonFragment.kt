package com.example.medihelper.mainapp.more.patronconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.custom.setTransparentStatusBar
import com.example.medihelper.databinding.FragmentConnectedPersonBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectedPersonFragment : AppFullScreenDialog() {

    private val viewModel: ConnectedPersonViewModel by viewModel()

    fun onClickCancelConnection() {
        viewModel.cancelConnection()
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentConnectedPersonBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_connected_person,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentStatusBar()
    }
}