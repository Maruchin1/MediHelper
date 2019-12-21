package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.usecases.medicines.GetLiveAllMedicinesItemsUseCase
import com.maruchin.medihelper.presentation.utils.PicturesRef

class MedicinesListViewModel(
    private val getAllMedicinesItemsUseCase: GetLiveAllMedicinesItemsUseCase,
    private val picturesRef: PicturesRef
) : ViewModel() {

    val medicineItemList: LiveData<List<MedicineItem>>
    val anyMedicineAvailable: LiveData<Boolean>
    val nameQuery = MutableLiveData<String>("")

    init {
        medicineItemList = liveData {
            val medicineItemsLive = getAllMedicinesItemsUseCase.execute()
            emitSource(medicineItemsLive)
        }
        anyMedicineAvailable = Transformations.map(medicineItemList) { !it.isNullOrEmpty() }
        //todo wyłączono wyszukiwanie
//        medicineList = Transformations.switchMap(nameQuery) { nameQuery ->
//            if (nameQuery.isNullOrEmpty()) {
//                medicineUseCases.getAllMedicineListLive()
//            } else {
//                medicineUseCases.getMedicineListLiveFilteredByName(nameQuery)
//            }
//        }
    }

    fun getMedicinePictureRef(pictureName: String?): StorageReference? {
        return if (pictureName != null) picturesRef.get(pictureName) else null
    }
}