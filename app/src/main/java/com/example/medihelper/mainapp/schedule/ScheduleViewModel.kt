package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.custom.AppDate
import com.example.medihelper.service.AppMode
import com.example.medihelper.service.DateTimeService
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.ServerApiService


class ScheduleViewModel(
    private val personService: PersonService,
    private val dateTimeService: DateTimeService,
    private val serverApiService: ServerApiService
) : ViewModel() {

    val timelineDaysCount = 10000
    val initialDatePosition = timelineDaysCount / 2

    val isAppModeConnectedLive: LiveData<Boolean>
    val selectedPersonItemLive = personService.getCurrPersonItemLive()
    val colorPrimaryLive: LiveData<Int>
    val calendarLayoutVisibleLive = MutableLiveData(false)
    val selectedDateLive = MutableLiveData<AppDate>()

    init {
        isAppModeConnectedLive = Transformations.map(serverApiService.getAppModeLive()) {
            it == AppMode.CONNECTED
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