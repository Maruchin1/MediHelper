package com.maruchin.medihelper.presentation.feature.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentOptionsBinding
import com.maruchin.medihelper.presentation.dialogs.ConfirmDialog
import com.maruchin.medihelper.presentation.framework.BaseHomeFragment
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_options.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment : BaseHomeFragment<FragmentOptionsBinding>(R.layout.fragment_options) {

    private val viewModel: OptionsViewModel by viewModel()
    private val loadingScreen: LoadingScreen by inject()

    fun onClickChangePassword() {
        findNavController().navigate(OptionsFragmentDirections.toChangePasswordDialog())
    }

    fun onClickSignOut() {
        ConfirmDialog(
            title = "Wyloguj",
            message = "Czy na pewno chcesz się wylogować z aplikacji?",
            iconResId = R.drawable.baseline_exit_to_app_black_36
        ).apply {
            setOnConfirmClickListener {
                viewModel.signOutUser()
            }
        }.show(childFragmentManager)
    }

    fun onClickNotificationsAndAlarmsHelp() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLoadingScreen()
        setupMedicinesRemindingOptions()
        observeViewModel()
    }

    private fun bindLoadingScreen() {
        loadingScreen.bind(this, viewModel.loadingInProgress)
    }

    private fun setupMedicinesRemindingOptions() {
        switch_notifications_enabled.setOnCheckedChangeListener { _, checked ->
            viewModel.setNotificationsEnabled(checked)
        }
        switch_alarms_enabled.setOnCheckedChangeListener { _, checked ->
            viewModel.setAlarmsEnabled(checked)
        }
    }

    private fun observeViewModel() {
        viewModel.actionSignOutSuccessful.observe(viewLifecycleOwner, Observer {
            super.mainActivity.restartApp()
        })
    }
}