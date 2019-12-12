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

    val currCalendarData: LiveData<CalendarData>
        get() = _currCalendarData

    private val _currCalendarData = MutableLiveData<CalendarData>()

    private val initialDate: AppDate = getCurrDateUseCase.execute()

    init {
        startCalendar = Calendar.getInstance().apply {
            timeInMillis = initialDate.timeInMillis
            add(Calendar.DATE, -CALENDAR_DAYS_RANGE)
        }
        endCalendar = Calendar.getInstance().apply {
            timeInMillis = initialDate.timeInMillis
            add(Calendar.DATE, CALENDAR_DAYS_RANGE)
        }
        calendarDaysCount = (CALENDAR_DAYS_RANGE * 2) + 1

        val initialCalendarData = CalendarData(
            date = Calendar.getInstance().apply {
                timeInMillis = initialDate.timeInMillis
            },
            position = CALENDAR_DAYS_RANGE
        )
        _currCalendarData.postValue(initialCalendarData)


    }

    fun getDateForPosition(position: Int): AppDate {
        val currDate = initialDate.copy()
        currDate.addDays(position - CALENDAR_DAYS_RANGE)
        return currDate
    }

    fun selectDate(position: Int) {
        val dateForPosition = getDateForPosition(position)

        val newCalendarDate = CalendarData(
            date = Calendar.getInstance().apply {
                timeInMillis = dateForPosition.timeInMillis
            },
            position = position
        )
        _currCalendarData.postValue(newCalendarDate)
    }

//    fun selectDate(position: Int) = selectDate(getDateForPosition(position))
//
//    fun selectDate(date: AppDate) {
//        val currDate = _selectedDate.value
//        if (currDate == null) {
//            _selectedDate.value = date
//        } else if (date != currDate) {
//            _selectedDate.value = date
//        }
//    }
//
//    fun selectTodayDate() = selectDate(getCurrDateUseCase.execute())
//
//    fun getDateForPosition(position: Int): AppDate {
//        val currDate = getCurrDateUseCase.execute()
//        currDate.addDays(position - (INITIAL_DATE_POSITION))
//        return currDate
//    }
//
//    suspend fun getPositionForDate(date: AppDate): Int {
//        val daysDiff = calcDaysDiffUseCase.execute(date)
//        return (INITIAL_DATE_POSITION) + daysDiff
//    }

    fun updateAllStatus() = viewModelScope.launch {
//        plannedMedicineUseCases.updateAllStatus()
    }

    data class CalendarData(
        val date: Calendar,
        val position: Int
    )
}