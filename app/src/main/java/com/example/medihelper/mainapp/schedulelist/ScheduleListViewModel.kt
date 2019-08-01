package com.example.medihelper.mainapp.schedulelist

import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository

class ScheduleListViewModel : ViewModel() {
    val scheduledMedicineListLive = AppRepository.getScheduledMedicinesLive()
}