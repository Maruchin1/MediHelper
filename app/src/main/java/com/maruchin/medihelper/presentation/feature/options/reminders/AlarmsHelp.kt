package com.maruchin.medihelper.presentation.feature.options.reminders

import android.content.Context
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.feature.options.reminders.HelpGenerator

class AlarmsHelp(context: Context) : HelpGenerator(context) {

    override fun generateHeaders(): List<Int> {
        return listOf(
            R.string.help_header_planned_medicines_alarms,
            R.string.help_header_notifications_and_alarms_not_working
        )
    }

    override fun generateBodies(): List<Int> {
        return listOf(
            R.string.help_body_planned_medicines_alarms,
            R.string.help_body_notifications_and_alarms_not_working
        )
    }
}