package com.maruchin.medihelper.presentation.feature.plans

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.profile.GetProfileItemUseCase
import com.maruchin.medihelper.presentation.model.MedicinePlanItem
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.launch

class MedicinePlanListViewModel(
    private val selectedProfile: SelectedProfile,
    private val getProfileItemUseCase: GetProfileItemUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String>
    val appModeConnected: LiveData<Boolean> = MutableLiveData(false)
    val medicinePlanItemOngoingList: LiveData<List<MedicinePlanItem>> = MutableLiveData()
    val medicinePlanItemEndedList: LiveData<List<MedicinePlanItem>> = MutableLiveData()

    val selectedProfileSimpleItem: LiveData<ProfileItem?>

    private val selectedProfileId: LiveData<String> = selectedProfile.profileIdLive
//    private val medicinePlanWithMedicineList: LiveData<List<MedicinePlanWithMedicine>>

    init {
        selectedProfileSimpleItem = Transformations.switchMap(selectedProfileId) { profileId ->
            liveData {
                val data = getProfileItemUseCase.execute(profileId)
                emit(data)
            }
        }
        colorPrimary = Transformations.map(selectedProfileSimpleItem) { it?.color }

//        currPersonItem = Transformations.map(currProfile) { PersonItem(it) }
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