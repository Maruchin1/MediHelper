package com.maruchin.medihelper.device.reminder.alarm

import androidx.lifecycle.*
import com.google.gson.Gson
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.usecases.plannedmedicines.SetPlannedMedicineTakenUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val setPlannedMedicineTakenUseCase: SetPlannedMedicineTakenUseCase
) : ViewModel() {

    val data: LiveData<PlannedMedicineNotifData>
        get() = _data

    private val _data = MutableLiveData<PlannedMedicineNotifData>()

    fun initData(data: String) = viewModelScope.launch {
        val notifData = Gson().fromJson(data, PlannedMedicineNotifData::class.java)
        _data.postValue(notifData)
    }

    fun setPlannedMedicineTaken(plannedMedicineId: String) = GlobalScope.launch {
        setPlannedMedicineTakenUseCase.execute(plannedMedicineId)
    }
}