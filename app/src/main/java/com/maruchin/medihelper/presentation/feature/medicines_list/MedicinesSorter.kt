package com.maruchin.medihelper.presentation.feature.medicines_list

import com.maruchin.medihelper.domain.model.MedicineItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicinesSorter {

    suspend fun sortItems(items: List<MedicineItem>, param: Param, order: Order) = withContext(Dispatchers.Default) {
        return@withContext when (order) {
            Order.ASC -> sortItemsAsc(items, param)
            Order.DES -> sortItemsDes(items, param)
        }
    }

    private fun sortItemsAsc(items: List<MedicineItem>, param: Param): List<MedicineItem> {
        return when (param) {
            Param.ALPHABETICAL -> items.sortedBy { it.name }
            Param.BY_STATE -> items.sortedBy { it.state }
            Param.BY_EXPIRE_DATE -> items.sortedBy { it.expireDate }
        }
    }

    private fun sortItemsDes(items: List<MedicineItem>, param: Param): List<MedicineItem> {
        return when (param) {
            Param.ALPHABETICAL -> items.sortedByDescending { it.name }
            Param.BY_STATE -> items.sortedByDescending { it.state }
            Param.BY_EXPIRE_DATE -> items.sortedByDescending { it.expireDate }
        }
    }

    enum class Param {
        ALPHABETICAL, BY_STATE, BY_EXPIRE_DATE
    }

    enum class Order {
        ASC, DES
    }
}