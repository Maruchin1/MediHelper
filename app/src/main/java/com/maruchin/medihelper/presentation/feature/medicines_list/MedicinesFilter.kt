package com.maruchin.medihelper.presentation.feature.medicines_list

import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.model.MedicineItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicinesFilter {

    suspend fun filterItems(items: List<MedicineItem>, state: State): List<MedicineItem> = withContext(Dispatchers.Default) {
        return@withContext if (state.noSelected) {
            items
        } else {
            items.filter { item ->
                isItemMatchingState(item, state)
            }
        }
    }

    private fun isItemMatchingState(item: MedicineItem, state: State): Boolean {
        return when (item.state.type) {
            MedicineState.Type.EMPTY -> state.empty
            MedicineState.Type.SMALL -> state.small
            MedicineState.Type.MEDIUM -> state.medium
            MedicineState.Type.GOOD -> state.good
        }
    }

    data class State(
        var empty: Boolean = false,
        var small: Boolean = false,
        var medium: Boolean = false,
        var good: Boolean = false
    ) {
        val noSelected: Boolean
            get() = arrayOf(
                empty,
                small,
                medium,
                good
            ).all { !it }
    }
}