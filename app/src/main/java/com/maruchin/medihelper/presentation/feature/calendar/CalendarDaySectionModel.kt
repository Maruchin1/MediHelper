package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.domain.entities.PlannedMedicine

class CalendarDaySectionModel(
    private val type: Type,
    private val dataLive: LiveData<CalendarDayData>
) {

    val items: LiveData<List<PlannedMedicineItemData>>
    val itemsAvailable: LiveData<Boolean>
    val takenMedicinesInfo: LiveData<String>
    val sectionExpanded: LiveData<Boolean>
        get() = _sectionExpanded

    private val _sectionExpanded = MutableLiveData<Boolean>(false)

    init {
        items = getLiveItemsByType()
        itemsAvailable = getLiveItemsAvailable()
        takenMedicinesInfo = getLiveTakenMedicinesInfo()
    }

    fun changeSectionCollapsed() {
        val currValue = _sectionExpanded.value!!
        _sectionExpanded.value = !currValue
    }

    private fun getLiveItemsByType() = Transformations.map(dataLive) { data ->
        when (type) {
            Type.MORNING -> data?.morningItems
            Type.AFTERNOON -> data?.afternoonItems
            Type.EVENING -> data?.eveningItems
        } ?: emptyList()
    }

    private fun getLiveItemsAvailable() = Transformations.map(items) { items ->
        items.isNotEmpty()
    }

    private fun getLiveTakenMedicinesInfo() = Transformations.map(items) { items ->
        val allItemsCount = items.size
        val takenItemsCount = numberOfTakenItems(items)
        return@map "Przyjęto $takenItemsCount z $allItemsCount"
    }

    private fun numberOfTakenItems(items: List<PlannedMedicineItemData>): Int {
        val takenItems = items.filter { item ->
            item.statusData.status  == PlannedMedicine.Status.TAKEN
        }
        return takenItems.size
    }

    enum class Type {
        MORNING, AFTERNOON, EVENING
    }
}