package com.maruchin.medihelper.device.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class NotificationActionReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SET_MEDICINE_TAKEN = "action-set-medicine-taken"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_SET_MEDICINE_TAKEN -> setMedicineTaken(context, intent)
            else -> throw InvalidIntentActionException()
        }
    }

    private fun setMedicineTaken(context: Context, intent: Intent) {
        val data = intent.data ?: throw PlannedMedicineIdNotPassedInIntentDataException()
        val plannedMedicineId = data.toString()
        val work = OneTimeWorkRequestBuilder<SetMedicineTakenWorker>()
            .setInputData(
                workDataOf(
                    SetMedicineTakenWorker.INPUT_PLANNED_MEDICINE_ID to plannedMedicineId
                )
            ).build()
        WorkManager.getInstance(context).enqueue(work)
    }

    class InvalidIntentActionException : Exception()
    class PlannedMedicineIdNotPassedInIntentDataException : Exception()
}