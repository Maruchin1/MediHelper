package com.maruchin.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentRegisterBinding
import com.maruchin.medihelper.presentation.framework.BaseLauncherFragment
import kotlinx.android.synthetic.main.fragment_add_edit_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseLauncherFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController(findNavController())
    }
}