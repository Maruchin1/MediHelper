package com.example.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentLoginBinding
import com.example.medihelper.presentation.feature.launcher.LauncherActivity
import com.example.medihelper.presentation.feature.launcher.LauncherOptionFragment
import com.example.medihelper.presentation.framework.bind
import com.example.medihelper.presentation.framework.showShortSnackbar
import com.example.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_launcher_login.*
import org.koin.android.ext.android.inject

class LoginFragmentLauncher : LauncherOptionFragment() {

    private val viewModel: LoginViewModel by inject()
    private val loadingScreen: LoadingScreen by inject()
    private val directions by lazy { LoginFragmentLauncherDirections }
    private val launcherActivity: LauncherActivity
        get() = requireActivity() as LauncherActivity

    fun onClickConfirm() = viewModel.loginUser()

    fun onClickBack() = findNavController().popBackStack()

    fun onClickRegister() = findNavController().navigate(LoginFragmentLauncherDirections.toRegisterFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentLoginBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_launcher_login,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                launcherActivity.startMainActivity()
            } else {
                showShortSnackbar(rootLayout = root_lay, message = errorMessage)
            }
        })
    }
}