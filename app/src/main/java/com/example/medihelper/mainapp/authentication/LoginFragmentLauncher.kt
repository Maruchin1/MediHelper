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
import com.example.medihelper.databinding.FragmentLoginBinding
import com.example.medihelper.mainapp.LauncherActivity
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.launcher.LauncherOptionFragment
import com.example.medihelper.service.LoadingScreenService
import kotlinx.android.synthetic.main.fragment_launcher_login.*
import org.koin.android.ext.android.inject

class LoginFragmentLauncher : LauncherOptionFragment() {

    private val viewModel: LoginViewModel by inject()
    private val loadingScreenService: LoadingScreenService by inject()
    private val directions by lazy { LoginFragmentLauncherDirections }
    private val launcherActivity: LauncherActivity
        get() = requireActivity() as LauncherActivity

    fun onClickConfirm() = viewModel.loginUser()

    fun onClickBack() = findNavController().popBackStack()

    fun onClickRegister() = findNavController().navigate(directions.toRegisterFragment())

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
                loadingScreenService.showLoadingScreen(childFragmentManager)
            } else {
                loadingScreenService.closeLoadingScreen()
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