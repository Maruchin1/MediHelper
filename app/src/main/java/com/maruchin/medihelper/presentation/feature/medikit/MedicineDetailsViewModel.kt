package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase
) : ViewModel() {

    val medicineName: LiveData<String>
    val medicineUnit: LiveData<String>
    val expireDate: LiveData<AppExpireDate>
    val stateData: LiveData<MedicineStateData>
    val additionalInfoAvailable: LiveData<Boolean>
    val additionalInfo: LiveData<String>
    val profileSimpleItemListAvailable: LiveData<Boolean>
    val profileSimpleItemList: LiveData<List<ProfileSimpleItem>>

    val medicineId: String?
        get() = medicineDetails.value?.medicineId

    private val medicineDetails = MutableLiveData<MedicineDetails>()

    init {
        medicineName = Transformations.map(medicineDetails) { it.name }
        medicineUnit = Transformations.map(medicineDetails) { it.unit }
        expireDate = Transformations.map(medicineDetails) { it.expireDate }
        stateData = Transformations.map(medicineDetails) { it.stateData }
        additionalInfoAvailable = Transformations.map(medicineDetails) { !it.additionalInfo.isNullOrEmpty() }
        additionalInfo = Transformations.map(medicineDetails) { it.additionalInfo }
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
    }

    fun deleteMedicine() = GlobalScope.launch {
        medicineId?.let {
            deleteMedicineUseCase.execute(it)
        }
    }
}