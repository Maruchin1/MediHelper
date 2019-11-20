package com.example.medihelper.presentation.feature.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.custom.showShortSnackbar
import com.example.medihelper.databinding.FragmentOptionsBinding
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.presentation.dialogs.ConfirmDialog
import com.example.medihelper.service.LoadingScreenService
import kotlinx.android.synthetic.main.fragment_options.*
import org.koin.android.ext.android.inject

class OptionsFragment : Fragment() {
    private val TAG = "OptionsFragment"

    private val viewModel: OptionsViewModel by inject()
    private val loadingScreenService: LoadingScreenService by inject()
    private val directions by lazy { OptionsFragmentDirections }
    private val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

    fun onClickLogin() = findNavController().navigate(OptionsFragmentDirections.toLoginFragment())

    fun onClickRegister() = findNavController().navigate(OptionsFragmentDirections.toRegisterFragment())

    fun onClickConnectWithPatron() = findNavController().navigate(OptionsFragmentDirections.toPatronConnectFragment())

    fun onClickChangePassword() = NewPasswordDialog().apply {
        setNewPasswordSelectedListener { newPassword ->
            viewModel.changePassword(newPassword)
        }
    }.show(childFragmentManager)

    fun onClickLogout() = LogoutDialog().apply {
        setOnLogoutSelectedListener { clearLocalData ->
            viewModel.logoutUser(clearLocalData)
        }
    }.show(childFragmentManager)

    fun onClickCancelPatronConnect() = ConfirmDialog().apply {
        title = "Anuluj polączenie"
        message = "Twoje połączenie z opiekunem zostanie anulowane. Z aplikacji zostaną usunięte wszystkie udostępnione przez opiekuna dane. Czy chcesz kontynuwoać?"
        iconResId = R.drawable.round_people_white_36
        setOnConfirmClickListener {
            viewModel.cancelPatronConnect()
        }
    }.show(childFragmentManager)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentOptionsBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_options,
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
        viewModel.errorChangePassword.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage == null) {
                showShortSnackbar(root_lay, "Hasło zostało zmienione")
            } else {
                showShortSnackbar(root_lay, errorMessage)
            }
        })
        viewModel.actionLogoutComplete.observe(viewLifecycleOwner, Observer {
            mainActivity.restartApp()
        })
        viewModel.actionCancelPatronConnectComplete.observe(viewLifecycleOwner, Observer {
            mainActivity.restartApp()
        })
    }
}