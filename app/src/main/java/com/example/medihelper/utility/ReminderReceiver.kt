package com.example.medihelper.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ReminderReceiver : BroadcastReceiver() {
    private val TAG = "ReminderReceiver"

    companion object {
        const val EXTRA_PLANNED_MEDICINE_ID = "extra-planned-medicine-id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Reminder here!")

        val plannedMedicineId = intent.getIntExtra(EXTRA_PLANNED_MEDICINE_ID, -1)

        if (plannedMedicineId != -1) {
            val reminderUtil = ReminderUtil(context)
            reminderUtil.remindAboutPlannedMedicine(plannedMedicineId)
        }
    }
}