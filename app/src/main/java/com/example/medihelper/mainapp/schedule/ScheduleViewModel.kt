package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.services.DateTimeService
import com.example.medihelper.services.PersonProfileService
import com.example.medihelper.services.SharedPrefService


class ScheduleViewModel(
    private val plannedMedicineRepository: PlannedMedicineRepository,
    private val personProfileService: PersonProfileService,
    private val sharedPrefService: SharedPrefService,
    private val dateTimeService: DateTimeService
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
        if (currDate == null) {
            selectedDateLive.value = date
        } else if (date != currDate) {
            selectedDateLive.value = date
        }
    }

    fun selectTodayDate() = selectDate(dateTimeService.getCurrDate())

    fun getDateForPosition(position: Int) = dateTimeService.getCurrDate().apply {
        addDays(position - (timelineDaysCount / 2))
    }

    fun getPositionForDate(date: AppDate): Int {
        val daysDiff = dateTimeService.daysBetween(dateTimeService.getCurrDate(), date)
        return (timelineDaysCount / 2) + daysDiff.toInt()
    }
}