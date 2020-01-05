package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicinesItemsByDateUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.launch

class CalendarDayViewModel(
    private val getLivePlannedMedicinesItemsByDateUseCase: GetLivePlannedMedicinesItemsByDateUseCase,
    private val selectedProfile: SelectedProfile
) : ViewModel() {

    val dataLoaded: LiveData<Boolean>
    val morningItems: LiveData<List<PlannedMedicineItemData>>
    val afternoonItems: LiveData<List<PlannedMedicineItemData>>
    val eveningItems: LiveData<List<PlannedMedicineItemData>>
    val morningAvailable: LiveData<Boolean>
    val afternoonAvailable: LiveData<Boolean>
    val eveningAvailable: LiveData<Boolean>
    val noItemsAvailable: LiveData<Boolean>

    private val data = MediatorLiveData<CalendarDayData>()

    init {
        dataLoaded = getLiveDataLoaded()
        morningItems = getLiveMorningItems()
        afternoonItems = getLiveAfternoonItems()
        eveningItems = getLiveEveningItems()
        morningAvailable = getLiveItemsAvailable(morningItems)
        afternoonAvailable = getLiveItemsAvailable(afternoonItems)
        eveningAvailable = getLiveItemsAvailable(eveningItems)
        noItemsAvailable = getLiveNoItemsAvailable()
    }

    fun initViewModel(calendarDayDate: AppDate) = viewModelScope.launch {
        val itemsLive = getLiveItems(calendarDayDate)
        val dataLive = transformLiveItemsToData(itemsLive)
        data.addSource(dataLive) { newData ->
            data.value = newData
        }
    }

    private fun getLiveDataLoaded(): LiveData<Boolean> {
        return Transformations.map(data) { it != null }
    }

    private fun getLiveMorningItems(): LiveData<List<PlannedMedicineItemData>> {
        return Transformations.map(data) { it.morningItems }
    }

    private fun getLiveAfternoonItems(): LiveData<List<PlannedMedicineItemData>> {
        return Transformations.map(data) { it.afternoonItems }
    }

    private fun getLiveEveningItems(): LiveData<List<PlannedMedicineItemData>> {
        return Transformations.map(data) { it.eveningItems }
    }

    private fun getLiveItemsAvailable(itemsLive: LiveData<List<PlannedMedicineItemData>>): LiveData<Boolean> {
        return Transformations.map(itemsLive) { items ->
            items.isNotEmpty()
        }
    }

    private fun getLiveNoItemsAvailable(): LiveData<Boolean> {
        return Transformations.map(data) {
            it.morningItems.isEmpty() and it.afternoonItems.isEmpty() and it.eveningItems.isEmpty()
        }
    }

    private fun getLiveItems(date: AppDate): LiveData<List<PlannedMedicineItem>> {
        return Transformations.switchMap(selectedProfile.profileIdLive) { selectedProfileId ->
            liveData {
                val itemsLive = getLivePlannedMedicinesItemsByDateUseCase.execute(selectedProfileId, date)
                emitSource(itemsLive)
            }
        }
    }

    private fun transformLiveItemsToData(itemsLive: LiveData<List<PlannedMedicineItem>>): LiveData<CalendarDayData> {
        return Transformations.map(itemsLive) { items ->
            CalendarDayData.fromDomainModel(items)
        }
    }
}