package com.example.medihelper.presentation.feature.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.domain.usecases.PlannedMedicineUseCases
import com.example.medihelper.presentation.model.PlannedMedicineItem

class CalendarDayViewModel(
    private val personUseCases: PersonUseCases,
    private val plannedMedicineUseCases: PlannedMedicineUseCases
) : ViewModel() {

    val date = MutableLiveData<AppDate>()
    val morningPlannedMedicineItemList: LiveData<List<PlannedMedicineItem>>
    val afternoonPlannedMedicineItemList: LiveData<List<PlannedMedicineItem>>
    val eveningPlannedMedicineItemList: LiveData<List<PlannedMedicineItem>>
    val plannedMedicineAvailable: LiveData<Boolean>
    val morningMedicineAvailable: LiveData<Boolean>
    val afternoonMedicineAvailable: LiveData<Boolean>
    val eveningMedicineAvailable: LiveData<Boolean>

    private val plannedMedicinesForDate: LiveData<List<PlannedMedicineWithMedicine>>

    init {
        plannedMedicinesForDate = Transformations.switchMap(date) { date ->
            Transformations.switchMap(personUseCases.getCurrPersonLive()) { person ->
                person?.let {
                    plannedMedicineUseCases.getPlannedMedicineWithMedicineListLiveByDateAndPerson(date, person.personId)
                }
            }
        }
        morningPlannedMedicineItemList = Transformations.map(plannedMedicinesForDate) { list ->
            list.filter { it.plannedTime < MORNING_AFTERNOON_LIMIT }.map { PlannedMedicineItem(it) }
        }
        afternoonPlannedMedicineItemList = Transformations.map(plannedMedicinesForDate) { list ->
            list.filter { it.plannedTime >= MORNING_AFTERNOON_LIMIT &&
                        it.plannedTime < AFTERNOON_EVENING_LIMIT
            }.map { PlannedMedicineItem(it) }
        }
        eveningPlannedMedicineItemList = Transformations.map(plannedMedicinesForDate) { list ->
            list.filter { it.plannedTime >= AFTERNOON_EVENING_LIMIT }.map { PlannedMedicineItem(it) }
        }
        plannedMedicineAvailable = Transformations.map(plannedMedicinesForDate) { !it.isNullOrEmpty() }
        morningMedicineAvailable = Transformations.map(morningPlannedMedicineItemList) { !it.isNullOrEmpty() }
        afternoonMedicineAvailable = Transformations.map(afternoonPlannedMedicineItemList) { !it.isNullOrEmpty() }
        eveningMedicineAvailable = Transformations.map(eveningPlannedMedicineItemList) { !it.isNullOrEmpty() }
    }

    companion object {
        private val MORNING_AFTERNOON_LIMIT = AppTime(12, 0)
        private val AFTERNOON_EVENING_LIMIT = AppTime(18, 0)
    }
}