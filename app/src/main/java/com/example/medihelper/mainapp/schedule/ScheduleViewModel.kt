package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.AppDateTime
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.services.PersonProfileService
import java.sql.Date
import java.util.*


class ScheduleViewModel(
    private val plannedMedicineRepository: PlannedMedicineRepository,
    personProfileService: PersonProfileService
) : ViewModel() {

    val timelineDaysCount = 10000
    val initialDatePosition = timelineDaysCount / 2

    val selectedPersonItemLive = personProfileService.getCurrPersonItemLive()
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
        return Date(calendar.timeInMillis)
    }

    fun getPositionForDate(date: Date): Int {
        val daysDiff = AppDateTime.daysBetween(AppDateTime.getCurrDate(), date)
        return (timelineDaysCount / 2) + daysDiff.toInt()
    }

    fun getPlannedMedicineItemListByDateLive(date: Date): LiveData<List<PlannedMedicineItem>> {
        return Transformations.switchMap(selectedPersonItemLive) { personItem ->
            plannedMedicineRepository.getItemListLiveByDate(date, personItem.personID)
        }
    }

}