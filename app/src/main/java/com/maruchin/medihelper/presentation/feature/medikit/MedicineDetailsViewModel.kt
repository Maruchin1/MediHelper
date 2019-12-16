package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.datetime.CalcDaysRemainUseCase
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.utils.PicturesRef
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
    private val calcDaysRemainUseCase: CalcDaysRemainUseCase,
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
        daysRemain = Transformations.switchMap(medicineDetails) {
            liveData {
                val days = calcDaysRemainUseCase.execute(it.expireDate)
                emit(days)
            }
        }
        stateData = Transformations.map(medicineDetails) { it.stateData }
        profileSimpleItemListAvailable = Transformations.map(medicineDetails) {
            !it.profileSimpleItemList.isNullOrEmpty()
        }
        profileSimpleItemList = Transformations.map(medicineDetails) { it.profileSimpleItemList }
        medicineStateAvailable = Transformations.map(stateData) { it != null }
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