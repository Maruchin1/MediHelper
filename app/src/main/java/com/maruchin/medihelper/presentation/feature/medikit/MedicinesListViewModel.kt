package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.usecases.medicines.GetLiveAllMedicinesItemsUseCase
import com.maruchin.medihelper.presentation.model.MedicineItemData
import com.maruchin.medihelper.presentation.utils.MedicinesSorter
import com.maruchin.medihelper.presentation.utils.PicturesRef

class MedicinesListViewModel(
    private val getAllMedicinesItemsUseCase: GetLiveAllMedicinesItemsUseCase,
    private val picturesRef: PicturesRef,
    private val medicinesSorter: MedicinesSorter
) : ViewModel() {

    val medicineItemList: LiveData<List<MedicineItemData>>
    val loadingInProgress: LiveData<Boolean>
    val noMedicines: LiveData<Boolean>

    val sortingParam = MutableLiveData<MedicinesSorter.Param>(MedicinesSorter.Param.ALPHABETICAL)
    val sortingOrder = MutableLiveData<MedicinesSorter.Order>(MedicinesSorter.Order.ASC)

    init {
        medicineItemList = getLiveMedicineItemsData()
        loadingInProgress = getLiveLoadingInProgress()
        noMedicines = getLiveNoMedicines()
    }

    fun getMedicinePictureRef(pictureName: String?): StorageReference? {
        return if (pictureName != null) picturesRef.get(pictureName) else null
    }

    private fun getLiveMedicineItemsData(): LiveData<List<MedicineItemData>> {
        return liveData {
            val itemsLive = getAllMedicinesItemsUseCase.execute()
            //todo transformować przez filtry
            val sortedItemsLive = transformLiveItemsWithSorting(itemsLive)
            val dataLive = transformLiveItemsToData(sortedItemsLive)
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

    private fun transformLiveItemsToData(
        itemsLive: LiveData<List<MedicineItem>>
    ): LiveData<List<MedicineItemData>> {
        return Transformations.map(itemsLive) { medicineItems ->
            medicineItems.map { item ->
                MedicineItemData.fromDomainModel(item)
            }
        }
    }

    private fun transformLiveItemsWithSorting(
        itemsLive: LiveData<List<MedicineItem>>
    ): LiveData<List<MedicineItem>> {
        return Transformations.switchMap(itemsLive) { items ->
            Transformations.switchMap(sortingParam) { sortingParam ->
                Transformations.map(sortingOrder) { sortingOrder ->
                    medicinesSorter.sortItems(items, sortingParam, sortingOrder)
                }
            }
        }
    }
}