package com.maruchin.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentLoginBinding
import com.maruchin.medihelper.presentation.framework.BaseLauncherFragment
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
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

        sharedElementEnterTransition = TransitionSet().apply {
            addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
            duration = 1000
        }
        enterTransition = Fade().apply {
            duration = 500
            startDelay = 1000
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setStatusBarColor(R.color.colorBackground)
        loadingScreen.bind(this, viewModel.loadingInProgress)
        observeViewModel()
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