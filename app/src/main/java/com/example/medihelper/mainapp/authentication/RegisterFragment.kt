package com.example.medihelper.mainapp.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.custom.showShortSnackbar
import com.example.medihelper.databinding.FragmentRegisterBinding
import com.example.medihelper.mainapp.launcher.LauncherOptionFragment
import com.example.medihelper.service.LoadingScreenService
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject

class RegisterFragment : LauncherOptionFragment() {

    private val viewModel: RegisterViewModel by inject()
    private val loadingScreenService: LoadingScreenService by inject()

    fun onClickConfirm() = viewModel.registerUser()

    fun onClickBack() = findNavController().popBackStack()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentRegisterBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_register,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loadingInProgress.observe(viewLifecycleOwner, Observer { inProgress ->
            if (inProgress) {
                loadingScreenService.showLoadingScreen(childFragmentManager)
            } else {
                loadingScreenService.closeLoadingScreen()
            }
        })
        viewModel.registerError.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                findNavController().popBackStack()
            } else {
                showShortSnackbar(rootLayout = root_lay, message = errorMessage)
            }
        })
    }
}