package com.example.medihelper.mainapp.launcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.custom.showShortSnackbar
import com.example.medihelper.databinding.FragmentLoginBinding
import com.example.medihelper.mainapp.LauncherActivity
import com.example.medihelper.service.LoadingScreenService
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment : LauncherOptionFragment() {

    private val viewModel: LoginViewModel by inject()
    private val loadingScreenService: LoadingScreenService by inject()
    private val launcherActivity: LauncherActivity by lazy { requireActivity() as LauncherActivity }
    private val directions by lazy { LoginFragmentDirections }

    fun onClickConfirm() = viewModel.loginUser()

    fun onClickBack() = findNavController().popBackStack()

    fun onClickRegister() = findNavController().navigate(directions.toRegisterFragment())

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
        viewModel.loginError.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                launcherActivity.startMainActivity()
            } else {
                showShortSnackbar(rootLayout = root_lay, message = errorMessage)
            }
        })
    }
}