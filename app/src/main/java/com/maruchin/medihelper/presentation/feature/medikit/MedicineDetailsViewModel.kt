package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.utils.PicturesRef
import kotlinx.coroutines.launch

class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
    private val picturesRef: PicturesRef
) : ViewModel() {

    val medicineStateAvailable: LiveData<Boolean>

    val medicinePicture: LiveData<StorageReference?>
    val medicineName: LiveData<String>
    val medicineUnit: LiveData<String>
    val expireDate: LiveData<AppExpireDate>
    val daysRemain: LiveData<Int>
    val stateData: LiveData<MedicineStateData>
    val profileSimpleItemListAvailable: LiveData<Boolean>
    val profileSimpleItemList: LiveData<List<ProfileItem>>

    val medicineId: String
        get() = medicineDetails.value!!.medicineId
    val actionDataLoaded: LiveData<Boolean>
        get() = _actionDataLoaded
    val actionMedicineDeleted: LiveData<Boolean>
        get() = _actionMedicineDeleted
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress

    private val _actionDataLoaded = ActionLiveData()
    private val _actionMedicineDeleted = ActionLiveData()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)

    private val medicineDetails = MutableLiveData<MedicineDetails>()

    init {
        medicinePicture = Transformations.switchMap(medicineDetails) { details ->
            liveData {
                details.pictureName?.let {
                    emit(picturesRef.get(it))
                }
            }
        }
        medicineName = Transformations.map(medicineDetails) { it.name }
        medicineUnit = Transformations.map(medicineDetails) { it.unit }
        expireDate = Transformations.map(medicineDetails) { it.expireDate }
        daysRemain = Transformations.map(medicineDetails) { it.daysRemains }
        stateData = Transformations.map(medicineDetails) { it.stateData }
        profileSimpleItemListAvailable = Transformations.map(medicineDetails) {
            !it.profileItems.isNullOrEmpty()
        }
        profileSimpleItemList = Transformations.map(medicineDetails) { it.profileItems }
        medicineStateAvailable = Transformations.map(stateData) { it != null }
    }

    fun setArgs(args: MedicineDetailsFragmentArgs) = viewModelScope.launch {
        val data = getMedicineDetailsUseCase.execute(args.medicineId)
        if (data != null) {
            medicineDetails.postValue(data)
        }
        _actionDataLoaded.sendAction()
    }

    fun deleteMedicine() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        deleteMedicineUseCase.execute(medicineId)

        _loadingInProgress.postValue(false)
        _actionMedicineDeleted.sendAction()
    }
}