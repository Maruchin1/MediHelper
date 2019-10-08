package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.services.PersonProfileService

class ScheduleDayViewModel(
    private val plannedMedicineRepository: PlannedMedicineRepository,
    private val personProfileService: PersonProfileService
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
            Transformations.switchMap(personProfileService.getCurrPersonItemLive()) { personItem ->
                personItem?.let { plannedMedicineRepository.getItemListLiveByDate(appDate, personItem.personID) }
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