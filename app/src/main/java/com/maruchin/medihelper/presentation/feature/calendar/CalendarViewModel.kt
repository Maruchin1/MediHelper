package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.domain.usecases.profile.GetProfileSimpleItemUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import java.util.*

class CalendarViewModel(
    private val getProfileSimpleItemUseCase: GetProfileSimpleItemUseCase,
    private val selectedProfile: SelectedProfile,
    private val deviceCalendar: DeviceCalendar
) : ViewModel() {

    companion object {
        private const val CALENDAR_DAYS_RANGE = 365
    }

    val colorPrimary: LiveData<String>
    val currProfileName: LiveData<String>
    val startCalendar: Calendar
    val endCalendar: Calendar
    val calendarDaysCount: Int
    val initialCalendar: Calendar
    val initialPosition: Int

    private val currProfile: LiveData<ProfileSimpleItem?>
    private val currDate: AppDate = deviceCalendar.getCurrDate()

    init {
        currProfile = Transformations.switchMap(selectedProfile.profileIdLive) { profileId ->
            liveData {
                val profileSimpleItem = getProfileSimpleItemUseCase.execute(profileId)
                emit(profileSimpleItem)
            }
        }
        colorPrimary = Transformations.map(currProfile) { it?.color }
        currProfileName = Transformations.map(currProfile) { it?.name }

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
}