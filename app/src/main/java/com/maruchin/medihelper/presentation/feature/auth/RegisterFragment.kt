package com.maruchin.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentRegisterBinding
import com.maruchin.medihelper.presentation.framework.BaseLauncherFragment
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_add_edit_profile.toolbar
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseLauncherFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModel()
    private val loadingScreen: LoadingScreen by inject()

    fun onClickSignUp() {
        viewModel.signUpUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Slide(Gravity.BOTTOM).apply {
            startDelay = 600
            duration = 300
        }
        returnTransition = Slide(Gravity.BOTTOM).apply {
            duration = 300
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingScreen.bind(this, viewModel.loadingInProgress)
        toolbar.setupWithNavController(findNavController())
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionUserSignedUp.observe(viewLifecycleOwner, Observer {
            super.launcherActivity.startMainActivity()
        })
        viewModel.errorGlobal.observe(viewLifecycleOwner, Observer {
            super.showSnackbar(root_lay, it)
        })
    }
}