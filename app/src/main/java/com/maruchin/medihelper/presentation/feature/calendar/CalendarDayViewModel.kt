package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.launch

class CalendarDayViewModel() : ViewModel() {

    val dataLoading: LiveData<Boolean>
    val morningSection: CalendarDaySectionModel
    val afternoonSection: CalendarDaySectionModel
    val eveningSection: CalendarDaySectionModel

    private val data = MediatorLiveData<CalendarDayData>().apply { value = null }

    init {
        dataLoading = getLiveDataLoading()
        morningSection = getSectionModel(CalendarDaySectionModel.Type.MORNING)
        afternoonSection = getSectionModel(CalendarDaySectionModel.Type.AFTERNOON)
        eveningSection = getSectionModel(CalendarDaySectionModel.Type.EVENING)
    }

    fun initViewModel(itemsLive: LiveData<List<PlannedMedicineItem>>) = viewModelScope.launch {
        val dataLive = transformLiveItemsToData(itemsLive)
        data.addSource(dataLive) { newData ->
            data.value = newData
        }
    }

    private fun getLiveDataLoading(): LiveData<Boolean> {
        return Transformations.map(data) { it == null }
    }

    private fun getSectionModel(type: CalendarDaySectionModel.Type): CalendarDaySectionModel {
        return CalendarDaySectionModel(type, data)
    }

    private fun transformLiveItemsToData(itemsLive: LiveData<List<PlannedMedicineItem>>): LiveData<CalendarDayData> {
        return Transformations.map(itemsLive) { items ->
            CalendarDayData.fromDomainModel(items)
        }
    }
}