package com.example.medihelper.mainapp

import androidx.lifecycle.*
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val plannedMedicineRepository: PlannedMedicineRepository
) : ViewModel() {
    private val TAG = "AlarmViewModel"

    val plannedTimeLive = MutableLiveData<AppTime>()
    val personNameLive = MutableLiveData<String>()
    val medicineNameLive = MutableLiveData<String>()
    val medicineDoseLive = MutableLiveData<String>()
    val personColorLive = MutableLiveData<Int>()

    val changedTimeLive = MutableLiveData<AppTime>()

    fun loadPlannedMedicineID(plannedMedicineID: Int) = viewModelScope.launch {
        val alarmData = plannedMedicineRepository.getAlarmData(plannedMedicineID)
        plannedTimeLive.postValue(alarmData.plannedTime)
        personNameLive.postValue(alarmData.personName)
        medicineNameLive.postValue(alarmData.medicineName)
        medicineDoseLive.postValue("${alarmData.plannedDoseSize} ${alarmData.medicineUnit}")
        personColorLive.postValue(alarmData.personColorResID)
    }
}