package com.example.medihelper.mainapp.more


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentMoreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoreFragment : Fragment() {

    private val viewModel: MoreViewModel by viewModel()
    private val directions by lazy { MoreFragmentDirections }

    fun onClickMediHelperAccount() {
        if (viewModel.isAppModeLogged) {
            findNavController().navigate(directions.toLoggedUserFragment())
        } else {
            findNavController().navigate(directions.toLoginRegisterFragment())
        }
    }

    fun onClickPatronConnect() {
        if (viewModel.isAppModeConnected) {
            findNavController().navigate(directions.toConnectedPersonFragment())
        } else {
            findNavController().navigate(directions.toPatronConnectFragment())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentMoreBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_more,
            viewModel = viewModel
        )
    }


}
