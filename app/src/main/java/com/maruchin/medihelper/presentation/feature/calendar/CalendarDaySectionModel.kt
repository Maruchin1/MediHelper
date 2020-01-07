package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class CalendarDaySectionModel(
    private val type: Type,
    private val dataLive: LiveData<CalendarDayData>
) {

    val items: LiveData<List<PlannedMedicineItemData>>
    val itemsCheckBoxes: LiveData<List<PlannedMedicineItemCheckBoxData>>
    val itemsAvailable: LiveData<Boolean>
    val sectionExpanded: LiveData<Boolean>
        get() = _sectionExpanded

    private val _sectionExpanded = MutableLiveData<Boolean>(false)

    init {
        items = getLiveItemsByType()
        itemsCheckBoxes = getLiveItemsCheckBoxes()
        itemsAvailable = getLiveItemsAvailable()
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

    private fun getLiveItemsCheckBoxes() = Transformations.map(items) { items ->
        items.map { singleItem ->
            PlannedMedicineItemCheckBoxData(singleItem)
        }
    }

    private fun getLiveItemsAvailable() = Transformations.map(items) { items ->
        items.isNotEmpty()
    }

    enum class Type {
        MORNING, AFTERNOON, EVENING
    }
}