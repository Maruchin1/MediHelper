package com.example.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.custom.showShortSnackbar
import com.example.medihelper.databinding.FragmentRegisterBinding
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.presentation.feature.auth.RegisterViewModel
import com.example.medihelper.service.LoadingScreenService
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject

class RegisterFragment : AppFullScreenDialog() {

    private val viewModel: RegisterViewModel by inject()
    private val loadingScreenService: LoadingScreenService by inject()
    private val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

    fun onClickConfirm() = viewModel.registerUser()

    fun onClickClose() = dismiss()

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
        setTransparentStatusBar()
        setLightStatusBar()
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
        viewModel.errorRegister.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                dismiss()
                mainActivity.showSnackbar("Zarejestrowano pomyślnie")
            } else {
                showShortSnackbar(rootLayout = root_lay, message = errorMessage)
            }
        })
    }
}