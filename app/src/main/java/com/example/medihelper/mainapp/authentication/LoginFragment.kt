package com.example.medihelper.mainapp.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.custom.showShortSnackbar
import com.example.medihelper.databinding.FragmentLoginBinding
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.service.LoadingScreenService
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment : AppFullScreenDialog(), ILoginFragment {

    private val viewModel: LoginViewModel by inject()
    private val loadingScreenService: LoadingScreenService by inject()
    private val mainActivity: MainActivity by lazy { requireActivity() as MainActivity }

    override fun onClickConfirm() {
        viewModel.loginUser()
    }

    override fun onClickBack() {
        dismiss()
    }

    override fun onClickRegister() {

    }

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
        viewModel.loadingInProgress.observe(viewLifecycleOwner, Observer { inProgress ->
            if (inProgress) {
                loadingScreenService.showLoadingScreen(childFragmentManager)
            } else {
                loadingScreenService.closeLoadingScreen()
            }
        })
        viewModel.loginError.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                mainActivity.restartApp()
            } else {
                showShortSnackbar(rootLayout = root_lay, message = errorMessage)
            }
        })
    }
}