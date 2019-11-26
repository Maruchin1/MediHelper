package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.usecases.medicines.GetAllMedicinesItemsLiveUseCase
import com.maruchin.medihelper.presentation.framework.map
import com.maruchin.medihelper.presentation.model.MedicineItem

class MedicinesListViewModel(
    private val getAllMedicinesItemsUseCase: GetAllMedicinesItemsLiveUseCase
) : ViewModel() {

    val medicineItemList: LiveData<List<MedicineItem>>
    val anyMedicineAvailable: LiveData<Boolean>
    val nameQuery = MutableLiveData<String>("")

    init {
        medicineItemList = liveData {
            val medicineItemsLive = getAllMedicinesItemsUseCase.execute().map { list ->
                list.map { MedicineItem(it) }
            }
            emitSource(medicineItemsLive)
        }
        anyMedicineAvailable = medicineItemList.map { !it.isNullOrEmpty()}
        //todo wyłączono wyszukiwanie
//        medicineList = Transformations.switchMap(nameQuery) { nameQuery ->
//            if (nameQuery.isNullOrEmpty()) {
//                medicineUseCases.getAllMedicineListLive()
//            } else {
//                medicineUseCases.getMedicineListLiveFilteredByName(nameQuery)
//            }
//        }
    }
}