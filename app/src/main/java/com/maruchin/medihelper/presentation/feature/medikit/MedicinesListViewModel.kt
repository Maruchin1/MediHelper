package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.usecases.medicines.GetLiveAllMedicinesItemsUseCase
import com.maruchin.medihelper.presentation.model.MedicineItemData
import com.maruchin.medihelper.presentation.utils.PicturesRef

class MedicinesListViewModel(
    private val getAllMedicinesItemsUseCase: GetLiveAllMedicinesItemsUseCase,
    private val picturesRef: PicturesRef
) : ViewModel() {

    val medicineItemList: LiveData<List<MedicineItemData>>
    val loadingInProgress: LiveData<Boolean>
    val noMedicines: LiveData<Boolean>

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
            val medicineItemsLive = getAllMedicinesItemsUseCase.execute()
            val dataLive = transformLiveMedicineItemsToData(medicineItemsLive)
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

    private fun transformLiveMedicineItemsToData(
        medicineItemsLive: LiveData<List<MedicineItem>>
    ): LiveData<List<MedicineItemData>> {
        return Transformations.map(medicineItemsLive) { medicineItems ->
            mapMedicineItemsToData(medicineItems)
        }
    }

    private fun mapMedicineItemsToData(medicineItems: List<MedicineItem>): List<MedicineItemData> {
        return medicineItems.map { medicineItem ->
            MedicineItemData.fromDomainModel(medicineItem)
        }
    }
}