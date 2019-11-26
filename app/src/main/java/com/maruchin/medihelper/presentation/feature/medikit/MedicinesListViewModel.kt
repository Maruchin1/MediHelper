package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.domain.entities.AppMode
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.usecases.MedicineUseCases
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.domain.usecases.ServerConnectionUseCases
import com.maruchin.medihelper.presentation.model.MedicineItem

class MedicinesListViewModel(
    private val serverConnectionUseCases: ServerConnectionUseCases,
    private val medicineUseCases: MedicineUseCases,
    private val personUseCases: PersonUseCases
) : ViewModel() {

    val colorPrimary: LiveData<Int>
    val medicineItemList: LiveData<List<MedicineItem>>
    val anyMedicineAvailable: LiveData<Boolean>
    val nameQuery = MutableLiveData<String>("")
    val appModeConnected: LiveData<Boolean>

    private val medicineList: LiveData<List<Medicine>>

    init {
        colorPrimary = personUseCases.getMainPersonColorLive()
        medicineList = Transformations.switchMap(nameQuery) { nameQuery ->
            if (nameQuery.isNullOrEmpty()) {
                medicineUseCases.getAllMedicineListLive()
            } else {
                medicineUseCases.getMedicineListLiveFilteredByName(nameQuery)
            }
        }
        medicineItemList = Transformations.map(medicineList) { medicineList -> medicineList.map { MedicineItem(it) } }
        anyMedicineAvailable = Transformations.map(medicineList) { !it.isNullOrEmpty() }
        appModeConnected = Transformations.map(serverConnectionUseCases.getAppModeLive()) { it == AppMode.CONNECTED }
    }
}