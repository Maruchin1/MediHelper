package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.AppDateTime
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import kotlinx.coroutines.launch
import java.util.*

class ScheduleViewModel : ViewModel() {

    val timelineDaysCount = 10000
    val initialDatePosition = timelineDaysCount / 2

    val selectedPersonItemLive = AppRepository.getSelectedPersonItemLive()
    val colorPrimaryLive: LiveData<Int>
    val calendarLayoutVisibleLive = MutableLiveData(false)
    val selectedDateLive = MutableLiveData<Date>()

    init {
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
    }

    fun selectDate(position: Int) = selectDate(getDateForPosition(position))

    fun selectDate(date: Date) {
        val currDate = selectedDateLive.value
        if (currDate != null) {
            if (AppDateTime.compareDates(date, currDate) != 0) {
                selectedDateLive.value = date
            }
        } else {
            selectedDateLive.value = date
        }
    }

    fun getDateForPosition(position: Int): Date {
        val calendar = AppDateTime.getCurrCalendar()
        calendar.add(Calendar.DAY_OF_YEAR, position - (timelineDaysCount / 2))
        return calendar.time
    }

    fun getPositionForDate(date: Date): Int {
        val currCalendar = AppDateTime.getCurrCalendar()
        val daysDiff = AppDateTime.daysBetween(currCalendar.time, date)
        return (timelineDaysCount / 2) + daysDiff.toInt()
    }

    fun getPlannedMedicineItemListByDateLive(date: Date): LiveData<List<PlannedMedicineItem>> {
        return Transformations.switchMap(selectedPersonItemLive) { personItem ->
            AppRepository.getPlannedMedicineItemListLiveByDate(date, personItem.personID)
        }
    }

}