package com.maruchin.medihelper.presentation.feature.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.entities.AppMode
import com.maruchin.medihelper.domain.entities.MedicinePlanType
import com.maruchin.medihelper.domain.entities.MedicinePlanWithMedicine
import com.maruchin.medihelper.domain.entities.Person
import com.maruchin.medihelper.domain.usecases.DateTimeUseCases
import com.maruchin.medihelper.domain.usecases.MedicinePlanUseCases
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.domain.usecases.ServerConnectionUseCases
import com.maruchin.medihelper.presentation.model.MedicinePlanItem
import com.maruchin.medihelper.presentation.model.PersonItem
import kotlinx.coroutines.launch

class MedicinePlanListViewModel(
    private val personUseCases: PersonUseCases,
    private val serverConnectionUseCases: ServerConnectionUseCases,
    private val medicinePlanUseCases: MedicinePlanUseCases,
    private val dateTimeUseCases: DateTimeUseCases
) : ViewModel() {

    val colorPrimaryId: LiveData<Int>
    val appModeConnected: LiveData<Boolean>
    val currPersonItem: LiveData<PersonItem>
    val medicinePlanItemOngoingList: LiveData<List<MedicinePlanItem>>
    val medicinePlanItemEndedList: LiveData<List<MedicinePlanItem>>

    private val currPerson: LiveData<Person> = personUseCases.getCurrPersonLive()
    private val medicinePlanWithMedicineList: LiveData<List<MedicinePlanWithMedicine>>

    init {
        currPersonItem = Transformations.map(currPerson) { PersonItem(it) }
        colorPrimaryId = Transformations.map(currPersonItem) { it.colorId }
        appModeConnected = Transformations.map(serverConnectionUseCases.getAppModeLive()) { it == AppMode.CONNECTED }
        medicinePlanWithMedicineList = Transformations.switchMap(currPerson) {
            medicinePlanUseCases.getMedicinePlanWithMedicineListLiveByPersonId(it.personId)
        }
        medicinePlanItemOngoingList = Transformations.map(medicinePlanWithMedicineList) { list ->
            filterAndMapToMedicinePlanItemList(list, MedicinePlanType.ONGOING)
        }
        medicinePlanItemEndedList = Transformations.map(medicinePlanWithMedicineList) { list ->
            filterAndMapToMedicinePlanItemList(list, MedicinePlanType.ENDED)
        }
    }

    fun deleteMedicinePlan(medicinePlanId: Int) = viewModelScope.launch {
        medicinePlanUseCases.deleteMedicinePlanById(medicinePlanId)
    }

    private fun filterAndMapToMedicinePlanItemList(
        list: List<MedicinePlanWithMedicine>,
        medicinePlanType: MedicinePlanType
    ) = list.filter { medicinePlanWithMedicine ->
        val currDate = dateTimeUseCases.getCurrDate()
        medicinePlanWithMedicine.getPlanType(currDate) == medicinePlanType
    }.map {
        MedicinePlanItem(
            medicinePlanWithMedicine = it,
            colorPrimaryId = colorPrimaryId.value!!,
            isAppModeConnected = appModeConnected
        )
    }
}