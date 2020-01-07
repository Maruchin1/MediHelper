package com.maruchin.medihelper.presentation.feature.plan_details

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.model.PlanDetails
import com.maruchin.medihelper.domain.usecases.plans.DeleteSinglePlanUseCase
import com.maruchin.medihelper.domain.usecases.plans.GetPlanDetailsUseCase
import com.maruchin.medihelper.domain.usecases.plans.GetPlanHistoryUseCase
import com.maruchin.medihelper.presentation.feature.add_edit_plan.MedicineBasicData
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class PlanDetailsViewModel(
    private val getPlanDetailsUseCase: GetPlanDetailsUseCase,
    private val getPlanHistoryUseCase: GetPlanHistoryUseCase,
    private val deleteSinglePlanUseCase: DeleteSinglePlanUseCase,
    private val deviceCalendar: DeviceCalendar
) : ViewModel() {

    val colorPrimary: LiveData<String>
    val medicine: LiveData<MedicineBasicData>
    val durationTime: LiveData<DurationTimeData>
    val days: LiveData<DaysData?>
    val timesDoses: LiveData<List<TimeDoseData>>
    val todayHistoryPosition: LiveData<Int>

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

    private val details = MutableLiveData<PlanDetails>()

    init {
        colorPrimary = getLiveColorPrimary()
        medicine = getLiveMedicineBasicData()
        durationTime = getLiveDurationTimeData()
        days = getLiveDaysData()
        timesDoses = getLiveTimesDosesData()
        todayHistoryPosition = getLiveTodayHistoryPosition()
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
        deleteSinglePlanUseCase.execute(medicinePlanId)
        _loadingInProgress.postValue(false)
        _actionPlanDeleted.sendAction()
    }

    private fun getLiveColorPrimary() = Transformations.map(details) { details ->
        details.profileColor
    }

    private fun getLiveMedicineBasicData() = Transformations.map(details) { details ->
        MedicineBasicData.fromDomainModel(details)
    }

    private fun getLiveDurationTimeData() = Transformations.map(details) { details ->
        DurationTimeData.fromDomainModel(details)
    }

    private fun getLiveDaysData() = Transformations.map(details) { details ->
        DaysData.fromDomainModel(details)
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

    private fun getLiveTodayHistoryPosition() = Transformations.map(_history) { history ->
        history.indexOfFirst { item ->
            item.currDate
        }
    }

    private suspend fun loadPlanDetails() {
        val details = getPlanDetailsUseCase.execute(medicinePlanId)
        _medicineId = details.medicineId
        this.details.postValue(details)
    }

    private suspend fun loadPlanHistory() {
        val history = getPlanHistoryUseCase.execute(medicinePlanId)
        val historyData = mapHistoryToData(history)
        _history.postValue(historyData)
    }

    private fun mapHistoryToData(history: List<HistoryItem>): List<HistoryItemData> {
        return history.map { historyItem ->
            HistoryItemData.fromDomainModel(historyItem, deviceCalendar.getCurrDate())
        }
    }
}