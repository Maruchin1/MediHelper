package com.maruchin.medihelper.presentation.feature.planned_medicine_options

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineDetails
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTakenUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTimeUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineDetailsUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlannedMedicineOptionsViewModel(
    private val getPlannedMedicineDetailsUseCase: GetPlannedMedicineDetailsUseCase,
    private val changePlannedMedicineTakenUseCase: ChangePlannedMedicineTakenUseCase,
    private val changePlannedMedicineTimeUseCase: ChangePlannedMedicineTimeUseCase,
    selectedProfile: SelectedProfile
) : ViewModel() {

    val colorPrimary: LiveData<String> = selectedProfile.profileColorLive

    val basicData: LiveData<PlannedMedicineBasicData>
        get() = _basicData
    val statusData: LiveData<PlannedMedicineStatusData>
        get() = _statusData
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val plannedTime: AppTime
        get() = _plannedTime

    private val _basicData = MutableLiveData<PlannedMedicineBasicData>()
    private val _statusData = MutableLiveData<PlannedMedicineStatusData>()
    private val _loadingInProgress = MutableLiveData<Boolean>(true)
    private lateinit var _plannedTime: AppTime

    private lateinit var plannedMedicineId: String

    fun initData(plannedMedicineId: String) = viewModelScope.launch {
        this@PlannedMedicineOptionsViewModel.plannedMedicineId = plannedMedicineId
        val data = getPlannedMedicineDetailsUseCase.execute(plannedMedicineId)
        val basicData = getBasicData(data)
        val statusData = getStatusData(data.status)
        _plannedTime = data.plannedTime
        _basicData.postValue(basicData)
        _statusData.postValue(statusData)
        _loadingInProgress.postValue(false)
    }

    fun changePlannedMedicineTaken() = GlobalScope.launch {
        changePlannedMedicineTakenUseCase.execute(plannedMedicineId)
    }

    fun changePlannedTime(newTime: AppTime) = GlobalScope.launch {
        changePlannedMedicineTimeUseCase.execute(plannedMedicineId, newTime)
    }

    private fun getBasicData(model: PlannedMedicineDetails): PlannedMedicineBasicData {
        return PlannedMedicineBasicData.fromDomainModel(model)
    }

    private fun getStatusData(status: PlannedMedicine.Status): PlannedMedicineStatusData {
        return PlannedMedicineStatusData.fromDomainModel(status)
    }
}