package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.services.PersonProfileService
import com.example.medihelper.services.SharedPrefService


class ScheduleViewModel(
    private val plannedMedicineRepository: PlannedMedicineRepository,
    private val personProfileService: PersonProfileService,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {

    val timelineDaysCount = 10000
    val initialDatePosition = timelineDaysCount / 2

    val isAppModeConnectedLive: LiveData<Boolean>
    val selectedPersonItemLive = personProfileService.getCurrPersonItemLive()
    val colorPrimaryLive: LiveData<Int>
    val calendarLayoutVisibleLive = MutableLiveData(false)
    val selectedDateLive = MutableLiveData<AppDate>()

    init {
        isAppModeConnectedLive = Transformations.map(sharedPrefService.getAppModeLive()) {
            it == SharedPrefService.AppMode.CONNECTED
        }
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem?.personColorResID
        }
    }

    fun selectDate(position: Int) = selectDate(getDateForPosition(position))

    fun selectDate(date: AppDate) {
        val currDate = selectedDateLive.value
        if (currDate != null) {
            if (date != currDate) {
                selectedDateLive.value = date
            }
        } else {
            selectedDateLive.value = date
        }
    }

    fun selectTodayDate() = selectDate(AppDate.currDate())

    fun getDateForPosition(position: Int) = AppDate.currDate().apply {
        addDays(position - (timelineDaysCount / 2))
    }

    fun getPositionForDate(date: AppDate): Int {
        val daysDiff = AppDate.daysBetween(AppDate.currDate(), date)
        return (timelineDaysCount / 2) + daysDiff.toInt()
    }

    fun getPlannedMedicineItemListByDateLive(date: AppDate): LiveData<List<PlannedMedicineItem>> {
        return Transformations.switchMap(selectedPersonItemLive) { personItem ->
            personItem?.let { plannedMedicineRepository.getItemListLiveByDate(date, it.personID) }
        }
    }
}