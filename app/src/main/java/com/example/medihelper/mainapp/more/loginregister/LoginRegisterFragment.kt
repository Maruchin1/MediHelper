package com.example.medihelper.mainapp.more.loginregister

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.databinding.FragmentLoginRegisterBinding
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.service.LoadingScreenService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login_register.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginRegisterFragment : AppFullScreenDialog() {

    private val viewModel: LoginRegisterViewModel by viewModel()
    private val loadingScreenService: LoadingScreenService by inject()

    fun onClickOpenRegisterFragment() = childFragmentManager.beginTransaction()
        .replace(R.id.frame_fragments, LoginRegisterInputFragment().apply {
            fragmentMode =
                LoginRegisterInputFragment.Mode.REGISTER
        })
        .commit()

    fun onClickOpenLoginFragment() = childFragmentManager.beginTransaction()
        .replace(R.id.frame_fragments, LoginRegisterInputFragment().apply {
            fragmentMode =
                LoginRegisterInputFragment.Mode.LOGIN
        })
        .commit()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginRegisterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_register, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentStatusBar()
        setupToolbar()
        observeViewModel()
        onClickOpenLoginFragment()
    }

    private fun observeViewModel() {
        viewModel.loadingInProgressLive.observe(viewLifecycleOwner, Observer { inProgress ->
            if (inProgress) {
                loadingScreenService.showLoadingScreen(childFragmentManager)
            } else {
                loadingScreenService.closeLoadingScreen()
            }
        })
        viewModel.remoteDataIsAvailableAction.observe(viewLifecycleOwner, Observer { openLocalOrRemoteDataDialog() })
        viewModel.loginErrorLive.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                dismiss()
                (requireActivity() as MainActivity).showSnackbar("Zalogowano pomyślnie")
            } else {
                showSnackbar(errorMessage)
            }
        })
        viewModel.registerErrorLive.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                onClickOpenLoginFragment()
            } else {
                showSnackbar(errorMessage)
            }
        })
    }

    private fun openLocalOrRemoteDataDialog() = LocalOrRemoteDataDialog().show(childFragmentManager)

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun showSnackbar(message: String) =
        Snackbar.make(root_lay, message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
}