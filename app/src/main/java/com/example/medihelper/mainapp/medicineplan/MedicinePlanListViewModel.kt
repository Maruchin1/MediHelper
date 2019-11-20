package com.example.medihelper.mainapp.medicineplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.localdata.pojo.MedicinePlanItem
import com.example.medihelper.domain.entities.DaysType
import com.example.medihelper.domain.entities.DurationType
import com.example.medihelper.service.*
import kotlinx.coroutines.launch

class MedicinePlanListViewModel(
    private val personService: PersonService,
    private val serverApiService: ServerApiService,
    private val medicinePlanService: MedicinePlanService,
    private val dateTimeService: DateTimeService
) : ViewModel() {

    val isAppModeConnectedLive: LiveData<Boolean>
    val colorPrimaryLive: LiveData<Int>
    val selectedPersonItemLive = personService.getCurrPersonItemLive()
    private val medicinePlanItemListLive: LiveData<List<MedicinePlanItem>>
    private val medicinePlanItemOngoingListLive: LiveData<List<MedicinePlanItem>>
    private val medicinePlanItemEndedListLive: LiveData<List<MedicinePlanItem>>

    init {
        isAppModeConnectedLive = Transformations.map(serverApiService.getAppModeLive()) {
            it == AppMode.CONNECTED
        }
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem?.personColorResId
        }
        medicinePlanItemListLive = Transformations.switchMap(selectedPersonItemLive) { personItem ->
            medicinePlanService.getItemListLive(personItem.personId)
        }
        medicinePlanItemOngoingListLive = Transformations.map(medicinePlanItemListLive) { medicinePlanItemList ->
            medicinePlanItemList.filter { medicinePlanItem ->
                getMedicinePlanType(medicinePlanItem) == MedicinePlanType.ONGOING
            }
        }
        medicinePlanItemEndedListLive = Transformations.map(medicinePlanItemListLive) { medicinePlanItemList ->
            medicinePlanItemList.filter { medicinePlanItem ->
                getMedicinePlanType(medicinePlanItem) == MedicinePlanType.ENDED
            }
        }
    }

    fun getMedicinePlanItemListLive(medicinePlanType: MedicinePlanType): LiveData<List<MedicinePlanItem>> {
        return when (medicinePlanType) {
            MedicinePlanType.ONGOING -> medicinePlanItemOngoingListLive
            MedicinePlanType.ENDED -> medicinePlanItemEndedListLive
        }
    }

    fun deleteMedicinePlan(medicinePlanID: Int) = viewModelScope.launch {
        medicinePlanService.delete(medicinePlanID)
    }

    fun getMedicinePlanDisplayData(medicinePlanItem: MedicinePlanItem) = MedicinePlanDisplayData(
        medicinePlanID = medicinePlanItem.medicinePlanId,
        colorPrimaryResID = colorPrimaryLive.value!!,
        medicineName = medicinePlanItem.medicineName,
        planDuration = when (medicinePlanItem.durationType) {
            DurationType.ONCE -> "Jednorazowo ${medicinePlanItem.startDate.formatString}"
            DurationType.PERIOD -> "Od ${medicinePlanItem.startDate.formatString} " +
                    "do ${medicinePlanItem.endDate?.formatString}"
            DurationType.CONTINUOUS -> "Pzyjmowanie ciągłe od ${medicinePlanItem.startDate.formatString}"
        },
        daysType = when (medicinePlanItem.daysType) {
            DaysType.EVERYDAY -> "Codziennie"
            DaysType.DAYS_OF_WEEK -> medicinePlanItem.daysOfWeek?.getSelectedDaysString() ?: "--"
            DaysType.INTERVAL_OF_DAYS -> "Co ${medicinePlanItem.intervalOfDays ?: "--"} dni"
            else -> null
        },
        isAppModeConnected = serverApiService.getAppMode() == AppMode.CONNECTED
    )

    private fun getMedicinePlanType(medicinePlanItem: MedicinePlanItem): MedicinePlanType {
        val currDate = dateTimeService.getCurrDate()
        return when (medicinePlanItem.durationType) {
            DurationType.ONCE -> {
                when {
                    currDate > medicinePlanItem.startDate -> MedicinePlanType.ENDED
                    else -> MedicinePlanType.ONGOING
                }
            }
            DurationType.PERIOD -> {
                when {
                    currDate > medicinePlanItem.endDate!! -> MedicinePlanType.ENDED
                    else -> MedicinePlanType.ONGOING
                }
            }
            DurationType.CONTINUOUS -> MedicinePlanType.ONGOING
        }
    }

    data class MedicinePlanDisplayData(
        val medicinePlanID: Int,
        val colorPrimaryResID: Int,
        val medicineName: String,
        val planDuration: String,
        val daysType: String?,
        val isAppModeConnected: Boolean
    )

    enum class MedicinePlanType(val pageTitle: String) {
        ENDED("Zakończone"),
        ONGOING("Trwające")
    }
}