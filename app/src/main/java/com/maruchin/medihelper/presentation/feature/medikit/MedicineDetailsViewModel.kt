package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.utils.PicturesRef
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
    private val picturesRef: PicturesRef
) : ViewModel() {

    val medicinePicture: LiveData<StorageReference?>
    val medicineName: LiveData<String>
    val medicineUnit: LiveData<String>
    val expireDate: LiveData<AppExpireDate>
    val stateData: LiveData<MedicineStateData>
    val profileSimpleItemListAvailable: LiveData<Boolean>
    val profileSimpleItemList: LiveData<List<ProfileSimpleItem>>

    val actionDataLoaded: LiveData<Boolean>
        get() = _actionDataLoaded

    private val _actionDataLoaded = ActionLiveData()

    val medicineId: String?
        get() = medicineDetails.value?.medicineId

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
        stateData = Transformations.map(medicineDetails) { it.stateData }
        profileSimpleItemListAvailable = Transformations.map(medicineDetails) {
            !it.profileSimpleItemList.isNullOrEmpty()
        }
        profileSimpleItemList = Transformations.map(medicineDetails) { it.profileSimpleItemList }
    }

    fun setArgs(args: MedicineDetailsFragmentArgs) = viewModelScope.launch {
        val medicineId = args.medicineId
        val result = getMedicineDetailsUseCase.execute(medicineId)
        if (result != null) {
            medicineDetails.postValue(result)
        }
        _actionDataLoaded.sendAction()
    }

    fun deleteMedicine() = GlobalScope.launch {
        medicineId?.let {
            deleteMedicineUseCase.execute(it)
        }
    }
}