package com.maruchin.medihelper.presentation.feature.options.reminders

import android.content.Context
import com.maruchin.medihelper.R

class RemindersHelp(context: Context) : HelpGenerator(context) {

    override fun generateHeaders(): List<Int> {
        return listOf(
            R.string.help_header_reminders,
            R.string.help_header_reminders_not_working
        )
    }

    override fun generateBodies(): List<Int> {
        return listOf(
            R.string.help_body_reminders,
            R.string.help_body_reminders_not_working
        )
    }
}