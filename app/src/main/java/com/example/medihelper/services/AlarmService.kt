package com.example.medihelper.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.mainapp.AlarmActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AlarmService(
    private val context: Context,
    private val plannedMedicineRepository: PlannedMedicineRepository
) {
    private val TAG = "AlarmService"

    companion object {
        private const val REQUEST_CODE_PLANNED_MEDICINE_ALARM = 0
    }

    private val alarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun setPlannedMedicineAlarm(plannedMedicineID: Int, date: AppDate, time: AppTime) = GlobalScope.launch {
        val plannedMedicineAlarmData = plannedMedicineRepository.getAlarmData(plannedMedicineID)
        Log.i(TAG, "alarmData = $plannedMedicineAlarmData")

        val intent = Intent(context, AlarmActivity::class.java).apply {
            putExtras(
                Bundle().apply {
                    putInt(AlarmActivity.EXTRA_PLANNED_MEDICINE_ID, plannedMedicineID)
                }
            )
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_PLANNED_MEDICINE_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmTimeMillis = date.timeInMillis + time.timeInMillis
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent)
    }
}