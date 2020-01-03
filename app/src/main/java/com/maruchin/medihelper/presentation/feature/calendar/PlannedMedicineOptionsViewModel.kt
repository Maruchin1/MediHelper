package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.R
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

    val basicData: LiveData<BasicData>
        get() = _basicData
    val statusData: LiveData<StatusData>
        get() = _statusData
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val plannedMedicineId: String
        get() = _plannedMedicineId
    val plannedTime: AppTime
        get() = _plannedTime

    private val _basicData = MutableLiveData<BasicData>()
    private val _statusData = MutableLiveData<StatusData>()
    private val _loadingInProgress = MutableLiveData<Boolean>(true)
    private lateinit var _plannedMedicineId: String
    private lateinit var _plannedTime: AppTime

    fun initData(plannedMedicineId: String) = viewModelScope.launch {
        _plannedMedicineId = plannedMedicineId
        val data = getPlannedMedicineDetailsUseCase.execute(plannedMedicineId)
        val basicData = getBasicData(data)
        val statusData = getStatusData(data.status)
        _basicData.postValue(basicData)
        _statusData.postValue(statusData)
        _loadingInProgress.postValue(false)
    }

    fun changePlannedMedicineTaken() = GlobalScope.launch {
        changePlannedMedicineTakenUseCase.execute(_plannedMedicineId)

    }

    fun changePlannedTime(newTime: AppTime) = GlobalScope.launch {
        changePlannedMedicineTimeUseCase.execute(_plannedMedicineId, newTime)
    }

    private fun getBasicData(data: PlannedMedicineDetails): BasicData {
        return BasicData(
            medicineName = data.medicineName,
            plannedDateTime = "Zaplanowano na ${data.plannedDate.formatString} godz. ${data.plannedTime.formatString}",
            plannedDose = "Do przyjęcia ${data.plannedDoseSize} ${data.medicineUnit}"
        )
    }

    private fun getStatusData(status: PlannedMedicine.Status): StatusData {
        return when (status) {
            PlannedMedicine.Status.TAKEN -> getStatusDataForTaken()
            PlannedMedicine.Status.NOT_TAKEN -> getStatusDataForNotTaken()
        }
    }

    private fun getStatusDataForTaken(): StatusData {
        return StatusData(
            statusText = "Przyjęty",
            statusColorId = R.color.colorStateGood,
            statusIconId = R.drawable.round_check_circle_24,
            changeStateText = "Anuluj przyjęcie leku",
            changeStateIconId = R.drawable.round_close_24
        )
    }

    private fun getStatusDataForNotTaken(): StatusData {
        return StatusData(
            statusText = "Nieprzyjęty",
            statusColorId = R.color.colorStateSmall,
            statusIconId = R.drawable.round_radio_button_unchecked_24,
            changeStateText = "Oznacz jako przyjęty",
            changeStateIconId = R.drawable.baseline_check_24
        )
    }

    data class BasicData(
        val medicineName: String,
        val plannedDateTime: String,
        val plannedDose: String
    )

    data class StatusData(
        val statusText: String,
        val statusColorId: Int,
        val statusIconId: Int,
        val changeStateText: String,
        val changeStateIconId: Int
    )
}