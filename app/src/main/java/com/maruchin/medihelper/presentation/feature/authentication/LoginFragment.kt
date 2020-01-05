package com.maruchin.medihelper.presentation.feature.authentication

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.*
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentLoginBinding
import com.maruchin.medihelper.presentation.framework.BaseLauncherFragment
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseLauncherFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModel()
    private val loadingScreen: LoadingScreen by inject()

    fun onClickRegister() {
        findNavController().navigate(LoginFragmentDirections.toRegisterFragment())
    }

    fun onClickSignIn() {
        viewModel.signIn()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundColorStatusBar()
        bindLoadingScreen()
        observeViewModel()
    }

    private fun setTransitions() {
        sharedElementEnterTransition = TransitionSet().apply {
            addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
            duration = 1000
        }
        enterTransition = Fade().apply {
            duration = 500
            startDelay = 1000
        }
        exitTransition = Slide(Gravity.BOTTOM).apply {
            duration = 300
        }
        reenterTransition = Slide(Gravity.BOTTOM).apply {
            startDelay = 600
            duration = 300
        }
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun setBackgroundColorStatusBar() {
        super.setStatusBarColor(R.color.colorBackground)
    }

    private fun bindLoadingScreen() {
        loadingScreen.bind(this, viewModel.loadingInProgress)
    }

    private fun observeViewModel() {
        viewModel.actionSignInSuccessful.observe(viewLifecycleOwner, Observer {
            super.launcherActivity.startMainActivity()
        })
        viewModel.errorGlobal.observe(viewLifecycleOwner, Observer {
            super.showSnackbar(root_lay, it)
        })
    }
}