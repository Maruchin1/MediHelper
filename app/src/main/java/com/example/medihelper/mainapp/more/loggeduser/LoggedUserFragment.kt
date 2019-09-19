package com.example.medihelper.mainapp.more.loggeduser

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.databinding.FragmentLoggedUserBinding
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.dialogs.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.*
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.root_lay
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.toolbar
import kotlinx.android.synthetic.main.fragment_logged_user.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoggedUserFragment : AppFullScreenDialog() {

    private val viewModel: LoggedUserViewModel by viewModel()
    private var loadingDialog: LoadingDialog? = null

    fun onClickChangePassword() = NewPasswordDialog().apply {
        setNewPasswordSelectedListener { newPassword ->
            viewModel.changeUserPassword(requireContext(), newPassword)
        }
    }.show(childFragmentManager)

    fun onClickLogoutUser() = ConfirmDialog().apply {
        title = "Wyloguj"
        message =
            "Wylogowanie sprawi, że twoje dane nie będą synchronizowane z chmurą oraz aplikacjami podopiecznych. Czy chcesz kontynuować?"
        iconResId = R.drawable.baseline_account_circle_white_36
        setOnConfirmClickListener {
            viewModel.logoutUser()
            this@LoggedUserFragment.dismiss()
        }
    }.show(childFragmentManager)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoggedUserBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_logged_user, container, false)
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
    }

    private fun observeViewModel() {
        viewModel.loadingStartedAction.observe(viewLifecycleOwner, Observer { loadingStarted ->
            if (loadingStarted == true) {
                showLoadingDialog()
            }
        })
        viewModel.changePasswordSuccessfulAction.observe(viewLifecycleOwner, Observer { changePasswordSuccessful ->
            dismissLoadingDialog()
            if (changePasswordSuccessful == true) {
                Snackbar.make(root_lay, "Hasło zostało pomyślnie zmienione", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
            }
        })
    }

    private fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorTransparent)
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun showLoadingDialog() {
        loadingDialog = LoadingDialog()
        loadingDialog?.show(childFragmentManager)
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }
}