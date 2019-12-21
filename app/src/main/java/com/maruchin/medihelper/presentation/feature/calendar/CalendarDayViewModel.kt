package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
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

    val morningPlannedMedicines: LiveData<List<PlannedMedicineItem>>
    val afternoonPlannedMedicines: LiveData<List<PlannedMedicineItem>>
    val eveningPlannedMedicines: LiveData<List<PlannedMedicineItem>>

    val loadingInProgress: LiveData<Boolean>
    val medicinesAvailable: LiveData<Boolean>
    val noMedicinesForDay: LiveData<Boolean>

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
        morningPlannedMedicines = Transformations.map(plannedMedicines) { list ->
            list.filter {
                it.plannedTime < MORNING_AFTERNOON_LIMIT
            }.sortedBy { it.plannedTime }
        }
        afternoonPlannedMedicines = Transformations.map(plannedMedicines) { list ->
            list.filter {
                it.plannedTime >= MORNING_AFTERNOON_LIMIT &&
                        it.plannedTime < AFTERNOON_EVENING_LIMIT
            }.sortedBy { it.plannedTime }
        }
        eveningPlannedMedicines = Transformations.map(plannedMedicines) { list ->
            list.filter {
                it.plannedTime >= AFTERNOON_EVENING_LIMIT
            }.sortedBy { it.plannedTime }
        }
        loadingInProgress = Transformations.map(plannedMedicines) { it == null }
        medicinesAvailable = Transformations.map(plannedMedicines) { it.isNotEmpty() }
        noMedicinesForDay = Transformations.map(plannedMedicines) { it.size == 0 }
        morningAvailable = Transformations.map(morningPlannedMedicines) { !it.isNullOrEmpty() }
        afternoonAvailable = Transformations.map(afternoonPlannedMedicines) { !it.isNullOrEmpty() }
        eveningAvailable = Transformations.map(eveningPlannedMedicines) { !it.isNullOrEmpty() }
    }

    fun initData(calendarDayDate: AppDate) {
        this.calendarDayDate.postValue(calendarDayDate)
    }
}