package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppMode
import com.maruchin.medihelper.domain.usecases.DateTimeUseCases
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.domain.usecases.PlannedMedicineUseCases
import com.maruchin.medihelper.domain.usecases.ServerConnectionUseCases
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val serverConnectionUseCases: ServerConnectionUseCases,
    private val personUseCases: PersonUseCases,
    private val dateTimeUseCases: DateTimeUseCases,
    private val plannedMedicineUseCases: PlannedMedicineUseCases
) : ViewModel() {

    val timelineDaysCount = 1000
    val initialDatePosition = timelineDaysCount / 2

    val colorPrimaryId: LiveData<Int>
    val currPersonName: LiveData<String>
    val isAppModeConnectedLive: LiveData<Boolean>
    val calendarLayoutVisible = MutableLiveData<Boolean>(false)
    val selectedDate = MutableLiveData<AppDate>()

    init {
        isAppModeConnectedLive = Transformations.map(serverConnectionUseCases.getAppModeLive()) {
            it == AppMode.CONNECTED
        }
        colorPrimaryId = Transformations.map(personUseCases.getCurrPersonLive()) { it.colorId }
        currPersonName = Transformations.map(personUseCases.getCurrPersonLive()) { it.name }
    }

    fun selectDate(position: Int) = selectDate(getDateForPosition(position))

    fun selectDate(date: AppDate) {
        val currDate = selectedDate.value
        if (currDate == null) {
            selectedDate.value = date
        } else if (date != currDate) {
            selectedDate.value = date
        }
    }

    fun selectTodayDate() = selectDate(dateTimeUseCases.getCurrDate())

    fun getDateForPosition(position: Int) = dateTimeUseCases.getCurrDate().apply {
        addDays(position - (timelineDaysCount / 2))
    }

    fun getPositionForDate(date: AppDate): Int {
        val daysDiff = dateTimeUseCases.calcDaysBetween(dateTimeUseCases.getCurrDate(), date)
        return (timelineDaysCount / 2) + daysDiff.toInt()
    }

    fun updateAllStatus() = viewModelScope.launch {
        plannedMedicineUseCases.updateAllStatus()
    }
}