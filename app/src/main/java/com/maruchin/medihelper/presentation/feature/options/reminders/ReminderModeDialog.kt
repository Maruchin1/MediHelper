package com.maruchin.medihelper.presentation.feature.options.reminders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogReminderModeBinding
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_reminder_mode.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderModeDialog : BaseBottomDialog<DialogReminderModeBinding>(R.layout.dialog_reminder_mode) {
    override val TAG: String
        get() = "ReminderModeDialog"
    override val viewModel: ReminderModeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setupModeSelection()
        observeViewModel()
    }

    private fun setupModeSelection() {
        radio_group_reminder_mode.setOnCheckedChangeListener { _, checkedId ->
            val newMode = getNewModeByRadioId(checkedId)
            if (newMode != null) {
                viewModel.setReminderMode(newMode)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.reminderMode.observe(viewLifecycleOwner, Observer { mode ->
            val radioId = getRadioIdByMode(mode)
            radio_group_reminder_mode.check(radioId)
        })
    }

    private fun getNewModeByRadioId(id: Int): ReminderMode? {
        return when (id) {
            R.id.radio_notifications -> ReminderMode.NOTIFICATIONS
            R.id.radio_alarms -> ReminderMode.ALARMS
            else -> null
        }
    }

    private fun getRadioIdByMode(mode: ReminderMode): Int {
        return when (mode) {
            ReminderMode.NOTIFICATIONS -> R.id.radio_notifications
            ReminderMode.ALARMS -> R.id.radio_alarms
        }
    }
}