package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.usecases.profile.GetProfileSimpleItemUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import java.util.*

class CalendarViewModel(
    private val getProfileSimpleItemUseCase: GetProfileSimpleItemUseCase,
    private val selectedProfile: SelectedProfile,
    private val deviceCalendar: DeviceCalendar
) : ViewModel() {

    companion object {
        private const val CALENDAR_DAYS_OFFSET = 365
    }

    val colorPrimary: LiveData<String> = selectedProfile.profileColorLive
    val profileName: LiveData<String>
    val data: CalendarData

    private val currDate: AppDate = deviceCalendar.getCurrDate()

    init {
        profileName = getLiveProfileName()
        data = getCalendarData()
    }

    fun getDateForPosition(position: Int): AppDate {
        val currDate = currDate.copy()
        currDate.addDays(position - CALENDAR_DAYS_OFFSET)
        return currDate
    }

    fun getCalendarForPosition(position: Int): Calendar {
        val date = getDateForPosition(position)
        return Calendar.getInstance().apply {
            timeInMillis = date.timeInMillis
        }
    }

    private fun getLiveProfileName(): LiveData<String> {
        return Transformations.switchMap(selectedProfile.profileIdLive) { selectedProfileId ->
            liveData {
                val profileSimpleItem = getProfileSimpleItemUseCase.execute(selectedProfileId)
                val profileName = profileSimpleItem.name
                emit(profileName)
            }
        }
    }

    private fun getCalendarData(): CalendarData {
        return CalendarData.get(
            daysOffset = CALENDAR_DAYS_OFFSET,
            currDate = currDate
        )
    }
}