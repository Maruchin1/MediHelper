package com.maruchin.medihelper.presentation.feature.calendar

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.model.PlannedMedicineItem

data class CalendarDayData(
    val morningItems: List<PlannedMedicineItemData>,
    val afternoonItems: List<PlannedMedicineItemData>,
    val eveningItems: List<PlannedMedicineItemData>
) {
    companion object {

        private val MORNING_AFTERNOON_LIMIT = AppTime(12, 0)
        private val AFTERNOON_EVENING_LIMIT = AppTime(18, 0)

        fun fromDomainModel(plannedMedicinesItems: List<PlannedMedicineItem>): CalendarDayData {
            val morningItems =
                getMorningItems(
                    plannedMedicinesItems
                )
            val afternoonItems =
                getAfternoonItems(
                    plannedMedicinesItems
                )
            val eveningItems =
                getEveningItems(
                    plannedMedicinesItems
                )
            return CalendarDayData(
                morningItems = mapItemsToItemsData(
                    morningItems
                ),
                afternoonItems = mapItemsToItemsData(
                    afternoonItems
                ),
                eveningItems = mapItemsToItemsData(
                    eveningItems
                )
            )
        }

        private fun getMorningItems(items: List<PlannedMedicineItem>): List<PlannedMedicineItem> {
            return items.filter { item ->
                item.plannedTime < MORNING_AFTERNOON_LIMIT
            }.sortedBy { item ->
                item.plannedTime
            }
        }

        private fun getAfternoonItems(items: List<PlannedMedicineItem>): List<PlannedMedicineItem> {
            return items.filter { item ->
                item.plannedTime >= MORNING_AFTERNOON_LIMIT && item.plannedTime < AFTERNOON_EVENING_LIMIT
            }.sortedBy { item ->
                item.plannedTime
            }
        }

        private fun getEveningItems(items: List<PlannedMedicineItem>): List<PlannedMedicineItem> {
            return items.filter { item ->
                item.plannedTime >= AFTERNOON_EVENING_LIMIT
            }.sortedBy { item ->
                item.plannedTime
            }
        }

        private fun mapItemsToItemsData(items: List<PlannedMedicineItem>): List<PlannedMedicineItemData> {
            return items.map { item ->
                PlannedMedicineItemData.fromDomainModel(
                    item
                )
            }
        }
    }
}