package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.model.PlanCalendarData
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.usecases.plans.GetLivePlansCalendarDataForProfileUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileSimpleItemUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import java.util.*

class CalendarViewModel(
    private val getProfileSimpleItemUseCase: GetProfileSimpleItemUseCase,
    private val getLivePlansCalendarDataForProfileUseCase: GetLivePlansCalendarDataForProfileUseCase,
    private val selectedProfile: SelectedProfile,
    private val deviceCalendar: DeviceCalendar
) : ViewModel() {

    companion object {
        private const val CALENDAR_DAYS_OFFSET = 30
    }

    val colorPrimary: LiveData<String> = selectedProfile.profileColorLive
    val profileName: LiveData<String>
    val data: CalendarData

    private val currDate: AppDate = deviceCalendar.getCurrDate()
    private val profilePlans: LiveData<List<PlanCalendarData>>

    init {
        profileName = getLiveProfileName()
        profilePlans = getLiveProfilePlans()
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

    fun getLivePlannedMedicinesForDate(date: AppDate): LiveData<List<PlannedMedicineItem>> {
        return Transformations.switchMap(profilePlans) { plans ->
            liveData {
                val items = mutableListOf<PlannedMedicineItem>()
                plans.forEach { singlePlan ->
                    val planItems = singlePlan.getPlannedMedicinesItemsForDate(date)
                    items.addAll(planItems)
                }
                emit(items.toList())
            }
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

    private fun getLiveProfilePlans(): LiveData<List<PlanCalendarData>> {
        return Transformations.switchMap(selectedProfile.profileIdLive) { selectedProfileId ->
            liveData {
                val plans = getLivePlansCalendarDataForProfileUseCase.execute(selectedProfileId)
                emitSource(plans)
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