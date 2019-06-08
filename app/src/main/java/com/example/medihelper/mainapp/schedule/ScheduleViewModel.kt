package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.ViewModel
import com.example.medihelper.Repository

class ScheduleViewModel : ViewModel() {

    fun getScheduledMedicinesByDateLive(date: String) =
        Repository.getScheduledMedicinesByDateLive(date)

}