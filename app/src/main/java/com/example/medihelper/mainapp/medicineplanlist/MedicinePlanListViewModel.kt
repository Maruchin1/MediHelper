package com.example.medihelper.mainapp.medicineplanlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.AppDate
import com.example.medihelper.AppDateTime
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.services.PersonProfileService
import kotlinx.coroutines.launch

class MedicinePlanListViewModel(
    private val medicinePlanRepository: MedicinePlanRepository,
    personProfileService: PersonProfileService
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val selectedPersonItemLive = personProfileService.getCurrPersonItemLive()
    private val medicinePlanItemListLive: LiveData<List<MedicinePlanItem>>
    private val medicinePlanItemOngoingListLive: LiveData<List<MedicinePlanItem>>
    private val medicinePlanItemEndedListLive: LiveData<List<MedicinePlanItem>>

    init {
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
        medicinePlanItemListLive = Transformations.switchMap(selectedPersonItemLive) { personItem ->
            medicinePlanRepository.getItemListLive(personItem.personID)
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
        medicinePlanRepository.delete(medicinePlanID)
    }

    fun getMedicinePlanDisplayData(medicinePlanItem: MedicinePlanItem) = MedicinePlanDisplayData(
        medicinePlanID = medicinePlanItem.medicinePlanID,
        colorPrimaryResID = colorPrimaryLive.value!!,
        medicineName = medicinePlanItem.medicineName,
        planDuration = when (medicinePlanItem.durationType) {
            MedicinePlanEntity.DurationType.ONCE -> "Jednorazowo ${medicinePlanItem.startDate.formatString}"
            MedicinePlanEntity.DurationType.PERIOD -> "Od ${medicinePlanItem.startDate.formatString} " +
                    "do ${medicinePlanItem.endDate?.formatString}"
            MedicinePlanEntity.DurationType.CONTINUOUS -> "Pzyjmowanie ciągłe od ${medicinePlanItem.startDate.formatString}"
        },
        daysType = when (medicinePlanItem.daysType) {
            MedicinePlanEntity.DaysType.NONE -> null
            MedicinePlanEntity.DaysType.EVERYDAY -> "Codziennie"
            MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> medicinePlanItem.daysOfWeek?.getSelectedDaysString() ?: "--"
            MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> "Co ${medicinePlanItem.intervalOfDays ?: "--"} dni"
        }
    )

    private fun getMedicinePlanType(medicinePlanItem: MedicinePlanItem): MedicinePlanType {
        val currDate = AppDate.currDate()
        return when (medicinePlanItem.durationType) {
            MedicinePlanEntity.DurationType.ONCE -> {
                when (AppDate.compareDates(currDate, medicinePlanItem.startDate)) {
                    1 -> MedicinePlanType.ENDED
                    else -> MedicinePlanType.ONGOING
                }
            }
            MedicinePlanEntity.DurationType.PERIOD -> {
                when (AppDate.compareDates(currDate, medicinePlanItem.endDate!!)) {
                    1 -> MedicinePlanType.ENDED
                    else -> MedicinePlanType.ONGOING
                }
            }
            MedicinePlanEntity.DurationType.CONTINUOUS -> MedicinePlanType.ONGOING
        }
    }

    data class MedicinePlanDisplayData(
        val medicinePlanID: Int,
        val colorPrimaryResID: Int,
        val medicineName: String,
        val planDuration: String,
        val daysType: String?
    )

    enum class MedicinePlanType(val pageTitle: String) {
        ENDED("Zakończone"),
        ONGOING("Trwające")
    }
}