package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineDetails
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTakenUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineDetailsUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlannedMedicineOptionsViewModel(
    private val getPlannedMedicineDetailsUseCase: GetPlannedMedicineDetailsUseCase,
    private val changePlannedMedicineTakenUseCase: ChangePlannedMedicineTakenUseCase,
    selectedProfile: SelectedProfile
) : ViewModel() {

    val colorPrimary: LiveData<String?> = selectedProfile.profileColorLive
    val changeStatusText: LiveData<String>
    val changeStatusIcon: LiveData<Int>

    val details: LiveData<PlannedMedicineDetails>
        get() = _details

    private val _details = MutableLiveData<PlannedMedicineDetails>()

    init {
        changeStatusText = Transformations.map(_details) {
            if (it.status == PlannedMedicine.Status.TAKEN) {
                "Anuluj przyjęcie leku"
            } else {
                "Przyjmj lek"
            }
        }
        changeStatusIcon = Transformations.map(_details) {
            if (it.status == PlannedMedicine.Status.TAKEN) {
                R.drawable.round_close_24
            } else {
                R.drawable.baseline_check_24
            }
        }
    }

    fun initData(plannedMedicineId: String) = viewModelScope.launch {
        val data = getPlannedMedicineDetailsUseCase.execute(plannedMedicineId)
        _details.postValue(data)
    }

    fun changePlannedMedicineTaken() = GlobalScope.launch {
        _details.value?.plannedMedicineId?.let { plannedMedicineId ->
            changePlannedMedicineTakenUseCase.execute(plannedMedicineId)
        }
    }
}