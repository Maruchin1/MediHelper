package com.maruchin.medihelper.presentation.feature.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.domain.usecases.DateTimeUseCases
import com.maruchin.medihelper.domain.usecases.MedicinePlanUseCases
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.presentation.model.MedicinePlanHistoryItem

class MedicinePlanHistoryViewModel(
    private val personUseCases: PersonUseCases,
    private val medicinePlanUseCases: MedicinePlanUseCases,
    private val dateTimeUseCases: DateTimeUseCases
) : ViewModel() {

    val colorPrimaryId: LiveData<Int> = MutableLiveData()
    val medicineName: LiveData<String> = MutableLiveData()
    val historyItemList: LiveData<List<MedicinePlanHistoryItem>> = MutableLiveData()

    private val selectedMedicinePlanId = MutableLiveData<Int>()
//    private val medicinePlanWithMedicineAndPlannedMedicines: LiveData<MedicinePlanWithMedicineAndPlannedMedicines>

    init {
//        colorPrimaryId = Transformations.map(personUseCases.getCurrPersonLive()) { it.color }
//        medicinePlanWithMedicineAndPlannedMedicines = Transformations.switchMap(selectedMedicinePlanId) {
//            medicinePlanUseCases.getMedicinePlanWithMedicineAndPlannedMedicinesLiveById(it)
//        }
//        medicineName = Transformations.map(medicinePlanWithMedicineAndPlannedMedicines) {
//            it.medicinePlanWithMedicine.medicine.name
//        }
//        historyItemList = Transformations.map(medicinePlanWithMedicineAndPlannedMedicines) {
//            val list = mutableListOf<MedicinePlanHistoryItem>()
//
//            it.plannedMedicineList.groupBy { plannedMedicine ->
//                plannedMedicine.plannedDate
//            }.forEach { entry->
//                val currDate = dateTimeUseCases.getCurrDate()
//                val plannedDate = entry.key
//                val plannedMedicineList = entry.value
//                list.add(MedicinePlanHistoryItem(plannedDate, plannedMedicineList, currDate))
//            }
//
//            list.sortedBy { medicinePlanHistoryItem ->
//                medicinePlanHistoryItem.plannedDate
//            }
//        }
    }

//    fun setArgs(args: MedicinePlanHistoryDialogArgs) {
//        selectedMedicinePlanId.value = args.medicinePlanID
//    }
}