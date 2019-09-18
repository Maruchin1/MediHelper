package com.example.medihelper.mainapp.loginregister

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
import kotlinx.android.synthetic.main.fragment_login_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginRegisterFragment : AppFullScreenDialog() {

    private val viewModel: LoginRegisterViewModel by viewModel()

    fun onClickOpenRegisterFragment() = childFragmentManager.beginTransaction()
        .replace(R.id.frame_fragments, LoginRegisterInputFragment().apply { fragmentMode = LoginRegisterInputFragment.Mode.REGISTER })
        .commit()

    fun onClickOpenLoginFragment() = childFragmentManager.beginTransaction()
        .replace(R.id.frame_fragments, LoginRegisterInputFragment().apply { fragmentMode = LoginRegisterInputFragment.Mode.LOGIN })
        .commit()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoginRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_register, container, false)
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
        viewModel.loginSuccessfulAction.observe(viewLifecycleOwner, Observer { loginSuccessful ->
            if (loginSuccessful == true) {
                dismiss()
            }
        })
        viewModel.registrationSuccessfulAction.observe(viewLifecycleOwner, Observer { registrationSuccessful ->
            if (registrationSuccessful == true) {
                dismiss()
            }
        })
    }

    private fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorTransparent)
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }
}