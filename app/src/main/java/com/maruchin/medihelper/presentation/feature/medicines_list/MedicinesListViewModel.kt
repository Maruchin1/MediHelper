package com.maruchin.medihelper.presentation.feature.medicines_list

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.usecases.medicines.GetLiveAllMedicinesItemsUseCase
import com.maruchin.medihelper.presentation.utils.PicturesStorageRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedicinesListViewModel(
    private val getAllMedicinesItemsUseCase: GetLiveAllMedicinesItemsUseCase,
    private val picturesStorageRef: PicturesStorageRef,
    private val medicinesSorter: MedicinesSorter,
    private val medicinesFilter: MedicinesFilter
) : ViewModel() {

    val medicineItemList: LiveData<List<MedicineItemData>>
    val loadingInProgress: LiveData<Boolean>
    val noMedicines: LiveData<Boolean>

    val sortingParam = MutableLiveData<MedicinesSorter.Param>(
        MedicinesSorter.Param.ALPHABETICAL
    )
    val sortingOrder = MutableLiveData<MedicinesSorter.Order>(
        MedicinesSorter.Order.ASC
    )

    private val filterState = MutableLiveData<MedicinesFilter.State>(
        MedicinesFilter.State()
    )

    init {
        medicineItemList = getLiveMedicineItemsData()
        loadingInProgress = getLiveLoadingInProgress()
        noMedicines = getLiveNoMedicines()
    }

    fun getMedicinePictureRef(pictureName: String?): StorageReference? {
        return if (pictureName != null) picturesStorageRef.getPictureRef(pictureName) else null
    }

    fun setFilterStateByChipsIds(ids: List<Int>) = viewModelScope.launch {
        val state = mapFilterChipIdsToFilterState(ids)
        filterState.postValue(state)
    }

    private fun getLiveMedicineItemsData(): LiveData<List<MedicineItemData>> {
        return liveData {
            val itemsLive = getAllMedicinesItemsUseCase.execute()
            val sortedItemsLive = sortLiveItems(itemsLive)
            val filteredItemsLive = filterLiveItems(sortedItemsLive)
            val dataLive = mapLiveItemsToData(filteredItemsLive)
            emitSource(dataLive)
        }
    }

    private fun getLiveLoadingInProgress(): LiveData<Boolean> {
        return Transformations.map(medicineItemList) { list ->
            list == null
        }
    }

    private fun getLiveNoMedicines(): LiveData<Boolean> {
        return Transformations.map(medicineItemList) { list ->
            list.isEmpty()
        }
    }

    private fun mapLiveItemsToData(
        itemsLive: LiveData<List<MedicineItem>>
    ): LiveData<List<MedicineItemData>> {
        return Transformations.map(itemsLive) { medicineItems ->
            medicineItems.map { item ->
                MedicineItemData.fromDomainModel(item)
            }
        }
    }

    private fun sortLiveItems(
        itemsLive: LiveData<List<MedicineItem>>
    ): LiveData<List<MedicineItem>> {
        return Transformations.switchMap(itemsLive) { items ->
            Transformations.switchMap(sortingParam) { sortingParam ->
                Transformations.switchMap(sortingOrder) { sortingOrder ->
                    liveData {
                        val value = medicinesSorter.sortItems(items, sortingParam, sortingOrder)
                        emit(value)
                    }
                }
            }
        }
    }

    private fun filterLiveItems(
        itemsLive: LiveData<List<MedicineItem>>
    ): LiveData<List<MedicineItem>> {
        return Transformations.switchMap(itemsLive) { items ->
            Transformations.switchMap(filterState) { filterState ->
                liveData {
                    val value = medicinesFilter.filterItems(items, filterState)
                    emit(value)
                }
            }
        }
    }

    private suspend fun mapFilterChipIdsToFilterState(ids: List<Int>) = withContext(Dispatchers.Default) {
        return@withContext MedicinesFilter.State(
            empty = ids.contains(R.id.chip_state_empty),
            small = ids.contains(R.id.chip_state_small),
            medium = ids.contains(R.id.chip_state_medium),
            good = ids.contains(R.id.chip_state_good)
        )
    }
}