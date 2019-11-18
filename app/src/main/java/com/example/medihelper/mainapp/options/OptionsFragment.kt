package com.example.medihelper.mainapp.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentOptionsBinding
import org.koin.android.ext.android.inject

class OptionsFragment : Fragment() {

    private val viewModel: OptionsViewModel by inject()
    private val directions by lazy { OptionsFragmentDirections }

    fun onClickLogin() = findNavController().navigate(directions.toLoginFragment())

    fun onClickRegister() = findNavController().navigate(directions.toRegisterFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentOptionsBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_options,
            viewModel = viewModel
        )
    }
}