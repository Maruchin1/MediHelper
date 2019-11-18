package com.example.medihelper.mainapp.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentLoginBinding
import org.koin.android.ext.android.inject

class LoginFragment : AppFullScreenDialog() {

    private val viewModel: LoginViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentLoginBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_login,
            viewModel = viewModel
        )
    }


}