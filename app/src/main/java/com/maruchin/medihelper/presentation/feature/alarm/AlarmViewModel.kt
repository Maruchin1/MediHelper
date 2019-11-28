package com.maruchin.medihelper.presentation.feature.alarm

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.usecases.PlannedMedicineUseCases
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val plannedMedicineUseCases: PlannedMedicineUseCases
) : ViewModel() {
    private val TAG = "AlarmViewModel"

    val plannedTimeLive = MutableLiveData<AppTime>()
    val personNameLive = MutableLiveData<String>()
    val medicineNameLive = MutableLiveData<String>()
    val medicineDoseLive = MutableLiveData<String>()
    val personColorLive = MutableLiveData<Int>()

    val changedTimeLive = MutableLiveData<AppTime>()

    fun loadPlannedMedicineID(plannedMedicineID: Int) = viewModelScope.launch {
//        val alarmData = plannedMedicineService.getAlarmData(plannedMedicineID)
//        plannedTimeLive.postValue(alarmData.plannedTime)
//        personNameLive.postValue(alarmData.profileName)
//        medicineNameLive.postValue(alarmData.medicineName)
//        medicineDoseLive.postValue("${alarmData.plannedDoseSize} ${alarmData.medicineUnit}")
//        personColorLive.postValue(alarmData.personColorResId)
    }
}