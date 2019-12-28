package com.maruchin.medihelper.presentation.feature.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentOptionsBinding
import com.maruchin.medihelper.domain.entities.ReminderMode
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingScreen.bind(this, viewModel.loadingInProgress)
        setupReminderModeChipGroup()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionSignOutSuccessful.observe(viewLifecycleOwner, Observer {
            super.mainActivity.restartApp()
        })
        viewModel.reminderMode.observe(viewLifecycleOwner, Observer {
            when (it) {
                ReminderMode.NOTIFICATION -> checkReminderMode(R.id.chip_notification)
                ReminderMode.ALARM -> checkReminderMode(R.id.chip_alarm)
            }
        })
    }

    private fun setupReminderModeChipGroup() {
        chip_group_reminder_mode.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_notification -> viewModel.setReminderMode(ReminderMode.NOTIFICATION)
                R.id.chip_alarm -> viewModel.setReminderMode(ReminderMode.ALARM)
            }
        }
    }

    private fun checkReminderMode(chipId: Int) {
        if (chip_group_reminder_mode.checkedChipId != chipId) {
            chip_group_reminder_mode.check(chipId)
        }
    }
}