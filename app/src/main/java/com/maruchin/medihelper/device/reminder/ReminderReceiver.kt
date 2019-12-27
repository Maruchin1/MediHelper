package com.maruchin.medihelper.device.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.maruchin.medihelper.device.notifications.ReminderWorkManager

class ReminderReceiver : BroadcastReceiver() {
    private val TAG = "ReminderReceiver"

    companion object {
        const val EXTRA_PLANNED_MEDICINE_ID = "extra-planned-medicine-id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Reminder here!")

        when (intent.action) {
            DeviceReminderImpl.ACTION_PLANNED_MEDICINE -> notifyAboutPlannedMedicine(context, intent)
        }
    }

    private fun notifyAboutPlannedMedicine(context: Context, intent: Intent) {
        val plannedMedicineId = intent.data.toString()
        val work = OneTimeWorkRequestBuilder<PlannedMedicineWorker>()
            .setInputData(
                workDataOf(
                    PlannedMedicineWorker.INPUT_PLANNED_MEDICINE_ID to plannedMedicineId
                )
            )
            .build()
        WorkManager.getInstance(context).enqueue(work)
    }
}