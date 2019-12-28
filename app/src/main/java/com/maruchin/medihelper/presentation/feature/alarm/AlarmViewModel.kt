package com.maruchin.medihelper.presentation.feature.alarm

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.device.DeviceRingtone
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.model.PlannedMedicineNotfiData
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTimeUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineNotifDataUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.SetPlannedMedicineTakenUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val setPlannedMedicineTakenUseCase: SetPlannedMedicineTakenUseCase,
    private val changePlannedMedicineTimeUseCase: ChangePlannedMedicineTimeUseCase
) : ViewModel() {

    val data: LiveData<PlannedMedicineNotfiData>
        get() = _data
    val changedTime: LiveData<String>
        get() = _changedTime

    private val _data = MutableLiveData<PlannedMedicineNotfiData>()
    private val _changedTime = MutableLiveData<String>()

    fun setData(data: PlannedMedicineNotfiData) {
        _data.postValue(data)
    }

    fun setPlannedMedicineTaken(plannedMedicineId: String) = GlobalScope.launch {
        setPlannedMedicineTakenUseCase.execute(plannedMedicineId)
    }

    fun changePlannedTime(plannedMedicineId: String, newTime: AppTime) = GlobalScope.launch {
        _changedTime.postValue(newTime.formatString)
        changePlannedMedicineTimeUseCase.execute(plannedMedicineId, newTime)
    }
}