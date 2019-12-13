package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.usecases.datetime.GetCurrDateUseCase
import kotlinx.coroutines.launch
import java.util.*

class CalendarViewModel(
    private val getCurrDateUseCase: GetCurrDateUseCase
) : ViewModel() {

    companion object {
        private const val CALENDAR_DAYS_RANGE = 365
    }

    val colorPrimaryId: LiveData<Int> = MutableLiveData()

    val startCalendar: Calendar
    val endCalendar: Calendar
    val calendarDaysCount: Int
    val initialCalendar: Calendar
    val initialPosition: Int

    val fullCalendarMode: LiveData<Boolean>
        get() = _fullCalendarMode

    private val _fullCalendarMode = MutableLiveData<Boolean>(false)

    private val currDate: AppDate = getCurrDateUseCase.execute()

    init {
        startCalendar = Calendar.getInstance().apply {
            timeInMillis = currDate.timeInMillis
            add(Calendar.DATE, -CALENDAR_DAYS_RANGE)
        }
        endCalendar = Calendar.getInstance().apply {
            timeInMillis = currDate.timeInMillis
            add(Calendar.DATE, CALENDAR_DAYS_RANGE)
        }
        calendarDaysCount = (CALENDAR_DAYS_RANGE * 2) + 1
        initialCalendar = Calendar.getInstance().apply {
            timeInMillis = currDate.timeInMillis
        }
        initialPosition = CALENDAR_DAYS_RANGE
    }

    fun getDateForPosition(position: Int): AppDate {
        val currDate = currDate.copy()
        currDate.addDays(position - CALENDAR_DAYS_RANGE)
        return currDate
    }

    fun getCalendarForPosition(position: Int): Calendar {
        val date = getDateForPosition(position)
        return Calendar.getInstance().apply {
            timeInMillis = date.timeInMillis
        }
    }

    fun changeFullCalendarMode(enabled: Boolean) {
        _fullCalendarMode.value = enabled
    }

    fun updateAllStatus() = viewModelScope.launch {
//        plannedMedicineUseCases.updateAllStatus()
    }
}