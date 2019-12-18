package com.maruchin.medihelper.presentation.feature.mediplan

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.model.MedicinePlanDetails
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanDetailsUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class MedicinePlanDetailViewModel(
    private val getMedicinePlanDetailsUseCase: GetMedicinePlanDetailsUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String>
    val medicineName: LiveData<String>
    val medicineUnit: LiveData<String>
    val durationTime: LiveData<DurationTime>

    val actionDataLoaded: LiveData<Boolean>
        get() = _actionDataLoaded
    val medicineId: String
        get() = medicinePlanDetails.value?.medicineId ?: throw Exception("medicineId is null")

    private val _actionDataLoaded = ActionLiveData()

    private val medicinePlanDetails = MutableLiveData<MedicinePlanDetails>()

    init {
        colorPrimary = Transformations.map(medicinePlanDetails) { it.profileColor }
        medicineName = Transformations.map(medicinePlanDetails) { it.medicineName }
        medicineUnit = Transformations.map(medicinePlanDetails) { it.medicineUnit }
        durationTime = Transformations.map(medicinePlanDetails) {
            DurationTime(
                planType = when (it.planType) {
                    MedicinePlan.Type.ONCE -> "Jednorazowo"
                    MedicinePlan.Type.PERIOD -> "Przez okres dni"
                    MedicinePlan.Type.CONTINUOUS -> "Przyjmowanie ciągłe"
                },
                startDate = if (it.endDate != null) {
                    "Od ${it.startDate.formatString}"
                } else {
                    it.startDate.formatString
                },
                endDate = if (it.endDate != null) {
                    "Do ${it.endDate.formatString}"
                } else {
                    null
                }
            )
        }
    }

    fun setArgs(args: MedicinePlanDetailsFragmentArgs) = viewModelScope.launch {
        val data = getMedicinePlanDetailsUseCase.execute(args.medicinePlanId)
        if (data != null) {
            medicinePlanDetails.postValue(data)
        }
        _actionDataLoaded.sendAction()
    }

    data class DurationTime(
        val planType: String,
        val startDate: String,
        val endDate: String?
    )
}