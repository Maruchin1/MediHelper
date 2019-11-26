package com.maruchin.medihelper.presentation.feature.plans

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.usecases.DateTimeUseCases
import com.maruchin.medihelper.domain.usecases.MedicinePlanUseCases
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.presentation.model.MedicinePlanItem
import com.maruchin.medihelper.presentation.model.PersonItem
import kotlinx.coroutines.launch

class MedicinePlanListViewModel(
    private val personUseCases: PersonUseCases,
    private val medicinePlanUseCases: MedicinePlanUseCases,
    private val dateTimeUseCases: DateTimeUseCases
) : ViewModel() {

    val colorPrimaryId: LiveData<Int>
    val appModeConnected: LiveData<Boolean> = MutableLiveData(false)
    val currPersonItem: LiveData<PersonItem> = MutableLiveData()
    val medicinePlanItemOngoingList: LiveData<List<MedicinePlanItem>> = MutableLiveData()
    val medicinePlanItemEndedList: LiveData<List<MedicinePlanItem>> = MutableLiveData()

    private val currProfile: LiveData<Profile> = MutableLiveData()
//    private val medicinePlanWithMedicineList: LiveData<List<MedicinePlanWithMedicine>>

    init {
//        currPersonItem = Transformations.map(currProfile) { PersonItem(it) }
        colorPrimaryId = Transformations.map(currPersonItem) { it.colorId }
//        medicinePlanWithMedicineList = Transformations.switchMap(currProfile) {
//            medicinePlanUseCases.getMedicinePlanWithMedicineListLiveByPersonId(it.profileId)
//        }
//        medicinePlanItemOngoingList = Transformations.map(medicinePlanWithMedicineList) { list ->
//            filterAndMapToMedicinePlanItemList(list, MedicinePlanType.ONGOING)
//        }
//        medicinePlanItemEndedList = Transformations.map(medicinePlanWithMedicineList) { list ->
//            filterAndMapToMedicinePlanItemList(list, MedicinePlanType.ENDED)
//        }
    }

    fun deleteMedicinePlan(medicinePlanId: Int) = viewModelScope.launch {
//        medicinePlanUseCases.deleteMedicinePlanById(medicinePlanId)
    }

//    private fun filterAndMapToMedicinePlanItemList(
//        list: List<MedicinePlanWithMedicine>,
//        medicinePlanType: MedicinePlanType
//    ) = list.filter { medicinePlanWithMedicine ->
//        val currDate = dateTimeUseCases.getCurrDate()
//        medicinePlanWithMedicine.getPlanType(currDate) == medicinePlanType
//    }.map {
//        MedicinePlanItem(
//            medicinePlanWithMedicine = it,
//            colorPrimaryId = colorPrimaryId.value!!,
//            isAppModeConnected = appModeConnected
//        )
//    }
}