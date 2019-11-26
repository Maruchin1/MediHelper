package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.presentation.model.MedicineDetails
import com.maruchin.medihelper.presentation.model.PersonItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase
) : ViewModel() {

    val medicineDetails: LiveData<MedicineDetails>
        get() = _medicineDetails
    val medicineId: String?
        get() = _medicineDetails.value?.medicineId

    private val _medicineDetails = MutableLiveData<MedicineDetails>()

    fun setArgs(args: MedicineDetailsFragmentArgs) = viewModelScope.launch {
        val medicineId = args.medicineId
        val result = getMedicineDetailsUseCase.execute(medicineId)
        if (result != null) {
            _medicineDetails.postValue(MedicineDetails(result))
        }
    }

    fun deleteMedicine() = GlobalScope.launch {
        _medicineDetails.value?.let {
//            medicineUseCases.deleteMedicineById(it.medicineId)
        }
    }

    fun takeMedicineDose(doseSize: Float) = viewModelScope.launch {
        _medicineDetails.value?.let {

        }
//        selectedMedicineId.value?.let {
//            medicineUseCases.reduceMedicineCurrState(it, doseSize)
//        }
    }
}