package com.example.medihelper.device.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.medihelper.domain.usecases.PlannedMedicineUseCases
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.get

class MedicineTakenReceiver : BroadcastReceiver(), KoinComponent {
    private val TAG = "MedicineTakenReceiver"

    companion object {
        const val EXTRA_PLANNED_MEDICINE_ID = "extra-planned-medicine-id"
        const val EXTRA_MEDICINE_TAKEN = "extra-medicine-taken"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Medicine taken !")

        val plannedMedicineId = intent.getIntExtra(EXTRA_PLANNED_MEDICINE_ID, -1)
        val medicineTaken = intent.getBooleanExtra(EXTRA_MEDICINE_TAKEN, false)

        if (plannedMedicineId != -1) {
            val useCases: PlannedMedicineUseCases = get()
            GlobalScope.launch {
                useCases.changeMedicineTaken(plannedMedicineId, medicineTaken)
            }
            val notification: MedicineReminderNotification = get()
            notification.cancel(plannedMedicineId)
        }
    }
}