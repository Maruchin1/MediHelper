package com.example.medihelper.mainapp.more.loggeduser

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
import com.example.medihelper.databinding.FragmentLoggedUserBinding
import com.example.medihelper.mainapp.dialog.ConfirmDialog
import com.example.medihelper.mainapp.dialog.LoadingDialog
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_logged_user.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoggedUserFragment : AppFullScreenDialog() {

    private val viewModel: LoggedUserViewModel by viewModel()
    private var loadingDialog: LoadingDialog? = null

    fun onClickChangePassword() = NewPasswordDialog().apply {
        setNewPasswordSelectedListener { newPassword ->
            viewModel.changeUserPassword(newPassword)
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
            (requireActivity() as MainActivity).showSnackbar("Wylogowano z konta MediHelper")
        }
    }.show(childFragmentManager)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoggedUserBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_logged_user, container, false)
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
        viewModel.loadingStartedAction.observe(viewLifecycleOwner, Observer { showLoadingDialog() })
        viewModel.changePasswordErrorLive.observe(viewLifecycleOwner, Observer { errorMessage ->
            dismissLoadingDialog()
            showSnackbar(errorMessage)
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

    private fun showSnackbar(message: String) = Snackbar.make(root_lay, message, Snackbar.LENGTH_SHORT)
        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
}