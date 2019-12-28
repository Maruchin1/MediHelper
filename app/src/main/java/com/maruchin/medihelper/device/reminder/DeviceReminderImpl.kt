package com.maruchin.medihelper.device.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineNotifDataUseCase
import com.maruchin.medihelper.presentation.feature.alarm.AlarmActivity
import java.util.*

class DeviceReminderImpl(
    private val context: Context,
    private val deviceCalendar: DeviceCalendar
) : DeviceReminder {

    companion object {
        const val ACTION_PLANNED_MEDICINE = "action-planned-medicine"
        private const val REQUEST_CODE_PLANNED_MEDICINE = 0
    }

    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun addReminders(plannedMedicines: List<PlannedMedicine>) {
        val currDate = deviceCalendar.getCurrDate()
        val currTime = deviceCalendar.getCurrTime()

        plannedMedicines.forEach {
            if (isUpToDate(it, currDate, currTime)) {
                addReminder(it)
            }
        }
    }

    override fun cancelReminders(plannedMedicines: List<PlannedMedicine>) {
        val currDate = deviceCalendar.getCurrDate()
        val currTime = deviceCalendar.getCurrTime()

        plannedMedicines.forEach {
            if (isUpToDate(it, currDate, currTime)) {
                cancelReminder(it.entityId)
            }
        }
    }

    override fun updateReminder(plannedMedicine: PlannedMedicine) {
        val currDate = deviceCalendar.getCurrDate()
        val currTime = deviceCalendar.getCurrTime()

        if (isUpToDate(plannedMedicine, currDate, currTime)) {
            cancelReminder(plannedMedicine.entityId)
            addReminder(plannedMedicine)
        }
    }

    override suspend fun launchReminderNotification(plannedMedicineId: String) {
        PlannedMedicineNotification(context).notify(plannedMedicineId)
    }

    override fun launchReminderAlarm(plannedMedicineId: String) {
        val intent = Intent(context, AlarmActivity::class.java).apply {
            putExtra(AlarmActivity.EXTRA_PLANNED_MEDICINE_ID, plannedMedicineId)
        }

        context.startActivity(intent)
    }

    private fun isUpToDate(
        plannedMedicine: PlannedMedicine,
        currDate: AppDate,
        currTime: AppTime
    ) = when {
        plannedMedicine.plannedDate > currDate -> true
        plannedMedicine.plannedDate < currDate -> false
        else -> when {
            plannedMedicine.plannedTime > currTime -> true
            else -> false
        }
    }


    private fun addReminder(plannedMedicine: PlannedMedicine) {
        val reminderCalendar = getCalendar(plannedMedicine.plannedDate, plannedMedicine.plannedTime)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderCalendar.timeInMillis,
            getPendingIntent(plannedMedicine.entityId)
        )
    }

    private fun cancelReminder(plannedMedicineId: String) {
        val pendingIntent = getPendingIntent(plannedMedicineId)
        alarmManager.cancel(pendingIntent)
    }

    private fun getCalendar(date: AppDate, time: AppTime) = Calendar.getInstance().apply {
        set(date.year, date.month - 1, date.day, time.hour, time.minute, 0)
    }

    private fun getPendingIntent(plannedMedicineId: String): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PLANNED_MEDICINE,
            getIntent(plannedMedicineId),
            0
        )
    }

    private fun getIntent(plannedMedicineId: String): Intent {
        return Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_PLANNED_MEDICINE
            data = plannedMedicineId.toUri()
        }
    }
}