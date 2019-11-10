package com.example.medihelper.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.medihelper.custom.AppDate
import com.example.medihelper.custom.AppTime
import com.example.medihelper.mainapp.alarm.AlarmActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface AlarmService {
    fun setPlannedMedicineAlarm(plannedMedicineId: Int, date: AppDate, time: AppTime)
}

class AlarmServiceImpl(
    private val context: Context,
    private val plannedMedicineService: PlannedMedicineService
) : AlarmService {
    private val TAG = "AlarmService"

    companion object {
        private const val REQUEST_CODE_PLANNED_MEDICINE_ALARM = 0
    }

    private val alarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    override fun setPlannedMedicineAlarm(plannedMedicineId: Int, date: AppDate, time: AppTime) {
        GlobalScope.launch {
            val plannedMedicineAlarmData = plannedMedicineService.getAlarmData(plannedMedicineId)
            Log.i(TAG, "alarmData = $plannedMedicineAlarmData")

            val intent = Intent(context, AlarmActivity::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putInt(AlarmActivity.EXTRA_PLANNED_MEDICINE_ID, plannedMedicineId)
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
}