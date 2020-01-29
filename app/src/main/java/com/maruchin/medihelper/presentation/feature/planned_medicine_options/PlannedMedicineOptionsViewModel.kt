package com.maruchin.medihelper.presentation.feature.planned_medicine_options

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.usecases.planned_medicines.ChangePlannedMedicineTakenUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTimeUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlannedMedicineOptionsViewModel(
    private val changePlannedMedicineTakenUseCase: ChangePlannedMedicineTakenUseCase,
    selectedProfile: SelectedProfile
) : ViewModel() {

    val colorPrimary: LiveData<String> = selectedProfile.profileColorLive

    val basicData: LiveData<PlannedMedicineBasicData>
        get() = _basicData
    val statusData: LiveData<PlannedMedicineStatusData>
        get() = _statusData
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress

    private val _basicData = MutableLiveData<PlannedMedicineBasicData>()
    private val _statusData = MutableLiveData<PlannedMedicineStatusData>()
    private val _loadingInProgress = MutableLiveData<Boolean>(true)

    fun initData(data: PlannedMedicineItem) = viewModelScope.launch {
        val basicData = getBasicData(data)
        val statusData = getStatusData(data.status)
        _basicData.postValue(basicData)
        _statusData.postValue(statusData)
        _loadingInProgress.postValue(false)
    }

    fun changePlannedMedicineTaken() = GlobalScope.launch {
//        changePlannedMedicineTakenUseCase.execute(plannedMedicineId)
    }

    private fun getBasicData(model: PlannedMedicineItem): PlannedMedicineBasicData {
        return PlannedMedicineBasicData.fromDomainModel(model)
    }

    private fun getStatusData(status: PlannedMedicine.Status): PlannedMedicineStatusData {
        return PlannedMedicineStatusData.fromDomainModel(status)
    }
}