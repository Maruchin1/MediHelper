package com.maruchin.medihelper.presentation.feature.options

import android.content.Context
import com.maruchin.medihelper.R

class NotifAlarmHelp(context: Context) : HelpGenerator(context, getHeaders(), getBodies()) {

    companion object {

        private fun getHeaders() = listOf(
            R.string.help_header_planned_medicines_notifications,
            R.string.help_header_planned_medicines_alarms,
            R.string.help_header_notifications_and_alarms_not_working
        )

        private fun getBodies() = listOf(
            R.string.help_body_planned_medicines_notifications,
            R.string.help_body_planned_medicines_alarms,
            R.string.help_body_notifications_and_alarms_not_working
        )
    }
}