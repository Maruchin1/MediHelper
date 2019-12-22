package com.maruchin.medihelper.presentation.feature.mediplan

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.model.MedicinePlanDetails
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteMedicinePlanUseCase
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanDetailsUseCase
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanHistoryUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class MedicinePlanDetailsViewModel(
    private val getMedicinePlanDetailsUseCase: GetMedicinePlanDetailsUseCase,
    private val getMedicinePlanHistoryUseCase: GetMedicinePlanHistoryUseCase,
    private val deleteMedicinePlanUseCase: DeleteMedicinePlanUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String>
    val medicineName: LiveData<String>
    val medicineUnit: LiveData<String>
    val durationTime: LiveData<DurationTime>
    val days: LiveData<Days>
    val timesDoses: LiveData<List<TimeDose>>

    val medicinePlanId: String
        get() = medicinePlanDetails.value?.medicinePlanId ?: throw Exception("medicinePlanId is null")
    val medicineId: String
        get() = medicinePlanDetails.value?.medicineId ?: throw Exception("medicineId is null")
    val actionDataLoaded: LiveData<Boolean>
        get() = _actionDataLoaded
    val actionPlanDeleted: LiveData<Boolean>
        get() = _actionPlanDeleted
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val historyItems: LiveData<List<HistoryItem>>
        get() = _historyItems

    private val _actionDataLoaded = ActionLiveData()
    private val _actionPlanDeleted = ActionLiveData()
    private val _loadingInProgress = MutableLiveData<Boolean>()
    private val _historyItems = MutableLiveData<List<HistoryItem>>()

    private val medicinePlanDetails = MutableLiveData<MedicinePlanDetails>()

    init {
        colorPrimary = Transformations.map(medicinePlanDetails) { it.profileColor }
        medicineName = Transformations.map(medicinePlanDetails) { it.medicineName }
        medicineUnit = Transformations.map(medicinePlanDetails) { it.medicineUnit }
        durationTime = Transformations.map(medicinePlanDetails) { getDurationTime(it) }
        days = Transformations.map(medicinePlanDetails) { details -> details.intakeDays?.let { getDays(it) } }
        timesDoses = Transformations.map(medicinePlanDetails) { details ->
            details.timeDoseList.sortedBy { it.time }
        }
    }

    fun deletePlan() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        deleteMedicinePlanUseCase.execute(medicinePlanId)

        _loadingInProgress.postValue(false)
        _actionPlanDeleted.sendAction()
    }

    fun setArgs(args: MedicinePlanDetailsFragmentArgs) = viewModelScope.launch {
        val details = getMedicinePlanDetailsUseCase.execute(args.medicinePlanId)
        if (details != null) {
            medicinePlanDetails.postValue(details)
        }
        _actionDataLoaded.sendAction()
        val history = getMedicinePlanHistoryUseCase.execute(args.medicinePlanId)
        _historyItems.postValue(history)
    }

    private fun getDurationTime(details: MedicinePlanDetails): DurationTime {
        return DurationTime(
            planType = when (details.planType) {
                MedicinePlan.Type.ONCE -> "Jednego dnia"
                MedicinePlan.Type.PERIOD -> "Przez okres dni"
                MedicinePlan.Type.CONTINUOUS -> "Przyjmowanie ciągłe"
            },
            startDate = if (details.endDate != null) {
                "Od ${details.startDate.formatString}"
            } else {
                details.startDate.formatString
            },
            endDate = if (details.endDate != null) {
                "Do ${details.endDate.formatString}"
            } else {
                null
            }
        )
    }

    private fun getDays(intakeDays: IntakeDays): Days {
        var daysType = ""
        var daysDetails: String? = null
        when (intakeDays) {
            is IntakeDays.Everyday -> {
                daysType = "Codziennie"
            }
            is IntakeDays.DaysOfWeek -> {
                daysType = "Wybrane dni tygodnia"
                daysDetails = daysOfWeekToString(intakeDays)
            }
            is IntakeDays.Interval -> {
                daysType = "Co ${intakeDays.daysCount} dni"
            }
            is IntakeDays.Sequence -> {
                daysType = "Sekwencja dni"
                daysDetails = "${intakeDays.intakeCount} dni przyjmowania,\n${intakeDays.notIntakeCount} dni przerwy"
            }
        }
        return Days(daysType, daysDetails)
    }

    private fun daysOfWeekToString(daysOfWeek: IntakeDays.DaysOfWeek): String {
        return StringBuilder()
            .append(if (daysOfWeek.monday) "po, " else "")
            .append(if (daysOfWeek.tuesday) "wt, " else "")
            .append(if (daysOfWeek.wednesday) "śr, " else "")
            .append(if (daysOfWeek.thursday) "cz, " else "")
            .append(if (daysOfWeek.friday) "pi, " else "")
            .append(if (daysOfWeek.saturday) "so, " else "")
            .append(if (daysOfWeek.sunday) "nd, " else "")
            .toString().apply {
                dropLast(2)
            }
    }

    data class DurationTime(
        val planType: String,
        val startDate: String,
        val endDate: String?
    )

    data class Days(
        val daysType: String,
        val daysDetails: String?
    )
}