package com.maruchin.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.AppFullScreenDialog
import com.maruchin.medihelper.databinding.FragmentLoginBinding
import com.maruchin.medihelper.presentation.MainActivity
import com.maruchin.medihelper.presentation.framework.bind
import com.maruchin.medihelper.presentation.framework.showShortSnackbar
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment : AppFullScreenDialog() {

    private val viewModel: LoginViewModel by inject()
    private val loadingScreen: LoadingScreen by inject()
    private val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

    fun onClickConfirm() = viewModel.loginUser()

    fun onClickClose() = dismiss()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentLoginBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_login,
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
        viewModel.loadingInProcess.observe(viewLifecycleOwner, Observer { inProgress ->
            if (inProgress) {
                loadingScreen.showLoadingScreen(childFragmentManager)
            } else {
                loadingScreen.closeLoadingScreen()
            }
        })
        viewModel.errorLogin.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                dismiss()
                mainActivity.showSnackbar("Zalogowano pomyślnie")
            } else {
                showShortSnackbar(rootLayout = root_lay, message = errorMessage)
            }
        })
    }
}