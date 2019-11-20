package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.localdata.pojo.PlannedMedicineItem
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.PlannedMedicineService

class ScheduleDayViewModel(
    private val personService: PersonService,
    private val plannedMedicineService: PlannedMedicineService
) : ViewModel() {

    val dateLive = MutableLiveData<AppDate>()

    val morningPlannedMedicineItemListLive: LiveData<List<PlannedMedicineItem>>
    val afternoonPlannedMedicineItemListLive: LiveData<List<PlannedMedicineItem>>
    val eveningPlannedMedicineItemListLive: LiveData<List<PlannedMedicineItem>>

    val plannedMedicineAvailableLive: LiveData<Boolean>
    val morningMedicineAvailableLive: LiveData<Boolean>
    val afternoonMedicineAvailableLive: LiveData<Boolean>
    val eveningMedicineAvailableLive: LiveData<Boolean>

    private val plannedMedicineItemListLive: LiveData<List<PlannedMedicineItem>>

    init {
        plannedMedicineItemListLive = Transformations.switchMap(dateLive) { appDate ->
            Transformations.switchMap(personService.getCurrPersonItemLive()) { personItem ->
                personItem?.let { plannedMedicineService.getItemListLiveByDateAndPerson(appDate, personItem.personId) }
            }
        }
        morningPlannedMedicineItemListLive = Transformations.map(plannedMedicineItemListLive) { list ->
            list.filter { it.plannedTime < MORNING_AFTERNOON_LIMIT }
        }
        afternoonPlannedMedicineItemListLive = Transformations.map(plannedMedicineItemListLive) { list ->
            list.filter { it.plannedTime >= MORNING_AFTERNOON_LIMIT && it.plannedTime < AFTERNOON_EVENING_LIMIT }
        }
        eveningPlannedMedicineItemListLive = Transformations.map(plannedMedicineItemListLive) { list ->
            list.filter { it.plannedTime >= AFTERNOON_EVENING_LIMIT }
        }
        plannedMedicineAvailableLive = Transformations.map(plannedMedicineItemListLive) { !it.isNullOrEmpty() }
        morningMedicineAvailableLive = Transformations.map(morningPlannedMedicineItemListLive) { !it.isNullOrEmpty() }
        afternoonMedicineAvailableLive = Transformations.map(afternoonPlannedMedicineItemListLive) { !it.isNullOrEmpty() }
        eveningMedicineAvailableLive = Transformations.map(eveningPlannedMedicineItemListLive) { !it.isNullOrEmpty() }
    }

    companion object {
        private val MORNING_AFTERNOON_LIMIT = AppTime(12, 0)
        private val AFTERNOON_EVENING_LIMIT = AppTime(18, 0)
    }
}