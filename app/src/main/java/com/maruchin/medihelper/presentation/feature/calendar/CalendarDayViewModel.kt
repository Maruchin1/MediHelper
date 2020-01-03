package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.R
import com.maruchin.medihelper.data.di.mapperModule
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicinesItemsByDateUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile

class CalendarDayViewModel(
    private val getLivePlannedMedicinesItemsByDateUseCase: GetLivePlannedMedicinesItemsByDateUseCase,
    private val selectedProfile: SelectedProfile
) : ViewModel() {

    companion object {
        private val MORNING_AFTERNOON_LIMIT = AppTime(12, 0)
        private val AFTERNOON_EVENING_LIMIT = AppTime(18, 0)
    }

    val morningCalendarItems: LiveData<List<CalendarItem>>
    val afternoonCalendarItems: LiveData<List<CalendarItem>>
    val eveningCalendarItems: LiveData<List<CalendarItem>>

    val medicinesAvailable: LiveData<Boolean>
    val dataLoaded: LiveData<Boolean>
    val noPlannedMedicines: LiveData<Boolean>
    val morningAvailable: LiveData<Boolean>
    val afternoonAvailable: LiveData<Boolean>
    val eveningAvailable: LiveData<Boolean>

    private val plannedMedicines: LiveData<List<PlannedMedicineItem>>
    private val calendarDayDate = MutableLiveData<AppDate>()

    init {
        plannedMedicines = Transformations.switchMap(calendarDayDate) { date ->
            Transformations.switchMap(selectedProfile.profileIdLive) { profileId ->
                liveData {
                    val source = getLivePlannedMedicinesItemsByDateUseCase.execute(profileId, date)
                    emitSource(source)
                }
            }
        }
        morningCalendarItems = Transformations.map(plannedMedicines) { list ->
            val morningPlannedMedicines = getMorningPlannedMedicinesOrderedByTime(list)
            mapPlannedMedicinesToCalendarItems(morningPlannedMedicines)
        }
        afternoonCalendarItems = Transformations.map(plannedMedicines) { list ->
            val afternoonPlannedMedicines = getAfterNoonPlannedMedicinesOrderedByTime(list)
            mapPlannedMedicinesToCalendarItems(afternoonPlannedMedicines)
        }
        eveningCalendarItems = Transformations.map(plannedMedicines) { list ->
            val eveningPlannedMedicines = getEveningPlannedMedicinesOrderedByTime(list)
            mapPlannedMedicinesToCalendarItems(eveningPlannedMedicines)
        }
        dataLoaded = Transformations.map(plannedMedicines) { it != null }
        noPlannedMedicines = Transformations.map(plannedMedicines) { it.isEmpty() }
        medicinesAvailable = Transformations.map(plannedMedicines) { it.isNotEmpty() }
        morningAvailable = Transformations.map(morningCalendarItems) { !it.isNullOrEmpty() }
        afternoonAvailable = Transformations.map(afternoonCalendarItems) { !it.isNullOrEmpty() }
        eveningAvailable = Transformations.map(eveningCalendarItems) { !it.isNullOrEmpty() }
    }

    fun initData(calendarDayDate: AppDate) {
        this.calendarDayDate.postValue(calendarDayDate)
    }

    private fun getMorningPlannedMedicinesOrderedByTime(plannedMedicines: List<PlannedMedicineItem>): List<PlannedMedicineItem> {
        return plannedMedicines.filter {
            it.plannedTime < MORNING_AFTERNOON_LIMIT
        }.sortedBy {
            it.plannedTime
        }
    }

    private fun getAfterNoonPlannedMedicinesOrderedByTime(plannedMedicines: List<PlannedMedicineItem>): List<PlannedMedicineItem> {
        return plannedMedicines.filter {
            it.plannedTime >= MORNING_AFTERNOON_LIMIT && it.plannedTime < AFTERNOON_EVENING_LIMIT
        }.sortedBy {
            it.plannedTime
        }
    }

    private fun getEveningPlannedMedicinesOrderedByTime(plannedMedicines: List<PlannedMedicineItem>): List<PlannedMedicineItem> {
        return plannedMedicines.filter {
            it.plannedTime >= AFTERNOON_EVENING_LIMIT
        }.sortedBy {
            it.plannedTime
        }
    }

    private fun mapPlannedMedicinesToCalendarItems(plannedMedicines: List<PlannedMedicineItem>): List<CalendarItem> {
        return plannedMedicines.map {
            getCalendarItem(it)
        }
    }

    private fun getCalendarItem(data: PlannedMedicineItem): CalendarItem {
        return CalendarItem(
            plannedMedicineId = data.plannedMedicineId,
            medicineName = data.medicineName,
            plannedDose = "${data.plannedDoseSize} ${data.medicineUnit}",
            plannedTime = data.plannedTime.formatString,
            statusIconId = when (data.status) {
                PlannedMedicine.Status.TAKEN -> R.drawable.round_check_circle_24
                PlannedMedicine.Status.NOT_TAKEN -> R.drawable.round_radio_button_unchecked_24
            },
            statusColorId = when (data.status) {
                PlannedMedicine.Status.TAKEN -> R.color.colorStateGood
                PlannedMedicine.Status.NOT_TAKEN -> R.color.colorStateSmall
            }
        )
    }

    data class CalendarItem(
        val plannedMedicineId: String,
        val medicineName: String,
        val plannedDose: String,
        val plannedTime: String,
        val statusColorId: Int,
        val statusIconId: Int
    )
}