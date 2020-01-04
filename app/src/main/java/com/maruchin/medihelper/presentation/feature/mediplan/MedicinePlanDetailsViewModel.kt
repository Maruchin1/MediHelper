package com.maruchin.medihelper.presentation.feature.mediplan

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.model.MedicinePlanDetails
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteSingleMedicinePlanUseCase
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanDetailsUseCase
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanHistoryUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.model.*
import kotlinx.coroutines.launch

class MedicinePlanDetailsViewModel(
    private val getMedicinePlanDetailsUseCase: GetMedicinePlanDetailsUseCase,
    private val getMedicinePlanHistoryUseCase: GetMedicinePlanHistoryUseCase,
    private val deleteSingleMedicinePlanUseCase: DeleteSingleMedicinePlanUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String>
    val medicine: LiveData<MedicineBasicData>
    val durationTime: LiveData<MedicinePlanDurationTimeData>
    val days: LiveData<MedicinePlanDaysData?>
    val timesDoses: LiveData<List<TimeDoseData>>

    val history: LiveData<List<HistoryItemData>>
        get() = _history
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionDetailsLoaded: LiveData<Boolean>
        get() = _actionDetailsLoaded
    val actionHistoryLoaded: LiveData<Boolean>
        get() = _actionHistoryLoaded
    val actionPlanDeleted: LiveData<Boolean>
        get() = _actionPlanDeleted
    val medicinePlanId: String
        get() = _medicinePlanId
    val medicineId: String
        get() = _medicineId

    private val _history = MutableLiveData<List<HistoryItemData>>()
    private val _loadingInProgress = MutableLiveData<Boolean>()
    private val _actionDetailsLoaded = ActionLiveData()
    private val _actionHistoryLoaded = ActionLiveData()
    private val _actionPlanDeleted = ActionLiveData()
    private lateinit var _medicinePlanId: String
    private lateinit var _medicineId: String

    private val details = MutableLiveData<MedicinePlanDetails>()

    init {
        colorPrimary = getLiveColorPrimary()
        medicine = getLiveMedicineBasicData()
        durationTime = getLiveDurationTimeData()
        days = getLiveDaysData()
        timesDoses = getLiveTimesDosesData()
    }

    fun initViewModel(medicinePlanId: String) = viewModelScope.launch {
        _medicinePlanId = medicinePlanId
        loadPlanDetails()
        _actionDetailsLoaded.sendAction()
        loadPlanHistory()
        _actionHistoryLoaded.sendAction()
    }

    fun deletePlan() = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        deleteSingleMedicinePlanUseCase.execute(medicinePlanId)
        _loadingInProgress.postValue(false)
        _actionPlanDeleted.sendAction()
    }

    private fun getLiveColorPrimary() = Transformations.map(details) { details ->
        details.profileColor
    }

    private fun getLiveMedicineBasicData() = Transformations.map(details) { details ->
        MedicineBasicData(
            medicineId = details.medicineId,
            name = details.medicineName,
            unit = "Jednostka: ${details.medicineUnit}")
    }

    private fun getLiveDurationTimeData() = Transformations.map(details) { details ->
        MedicinePlanDurationTimeData.fromDomainModel(details)
    }

    private fun getLiveDaysData() = Transformations.map(details) { details ->
        MedicinePlanDaysData.fromDomainModel(details)
    }

    private fun getLiveTimesDosesData() = Transformations.map(details) { details ->
        details.timeDoseList.map { timeDose ->
            TimeDoseData.fromDomainModel(
                model = timeDose,
                medicineUnit = details.medicineUnit,
                profileColor = details.profileColor
            )
        }
    }

    private suspend fun loadPlanDetails() {
        val details = getMedicinePlanDetailsUseCase.execute(medicinePlanId)
        _medicineId = details.medicineId
        this.details.postValue(details)
    }

    private suspend fun loadPlanHistory() {
        val history = getMedicinePlanHistoryUseCase.execute(medicinePlanId)
        val historyData = mapHistoryToData(history)
        _history.postValue(historyData)
    }

    private fun mapHistoryToData(history: List<HistoryItem>): List<HistoryItemData> {
        return history.map { historyItem ->
            HistoryItemData.fromDomainModel(historyItem)
        }
    }
}