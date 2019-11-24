package com.example.medihelper.presentation.feature.medikit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.domain.entities.AppExpireDate
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.entities.MedicineInputData
import com.example.medihelper.domain.usecases.MedicineUseCases
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class AddEditMedicineViewModel(
    private val medicineUseCases: MedicineUseCases
) : ViewModel() {

    val medicineName = MutableLiveData<String>()
    val medicineUnit = MutableLiveData<String>()
    val expireDate = MutableLiveData<AppExpireDate>()
    val packageSize = MutableLiveData<Float>()
    val currState = MutableLiveData<Float>()
    val additionalInfo = MutableLiveData<String>()

    val formTitle: LiveData<String>
        get() = _formTitle
    val imageFile: LiveData<File>
        get() = _imageFile
    val errorMedicineName: LiveData<String>
        get() = _errorMedicineName
    val errorExpireDate: LiveData<String>
        get() = _errorExpireDate
    val errorCurrState: LiveData<String>
        get() = _errorCurrState

    private val _formTitle = MutableLiveData<String>("Dodaj lek")
    private val _imageFile = MutableLiveData<File>()
    private val _errorMedicineName = MutableLiveData<String>()
    private val _errorExpireDate = MutableLiveData<String>()
    private val _errorCurrState = MutableLiveData<String>()

    private val medicineUnitList: List<String> = medicineUseCases.getMedicineUnitList()
    private var editMedicineId: Int? = null

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        if (args.editMedicineId != -1) {
            editMedicineId = args.editMedicineId
            _formTitle.postValue("Edytuj lek")
            val editMedicine = medicineUseCases.getMedicineById(args.editMedicineId)
            setMedicineData(editMedicine)
        } else {
            setEmptyMedicineData()
        }
    }

    fun saveMedicine(): Boolean {
        if (isFormValid()) {
            val medicineId = editMedicineId
            if (medicineId == null) {
                GlobalScope.launch {
                    medicineUseCases.addNewMedicine(getMedicineInputData())
                }
            } else {
                GlobalScope.launch {
                    medicineUseCases.updateMedicine(medicineId, getMedicineInputData())
                }
            }
            return true
        }
        return false
    }

    fun capturePhoto() {
        medicineUseCases.captureMedicinePhoto(_imageFile)
    }

    private fun isFormValid(): Boolean {
        val medicineName = medicineName.value
        val expireDate = expireDate.value
        val packageSize = packageSize.value
        val currState = currState.value

        val medicineNameError = if (medicineName.isNullOrEmpty()) {
            "Pole jest wymagane"
        } else null
        val expireDateError = if (expireDate == null) {
            "Pole jest wymagene"
        } else null
        val currStateError = if (packageSize != null && currState != null && currState > packageSize) {
            "Większe niż rozmiar opakowania"
        } else null

        _errorMedicineName.postValue(medicineNameError)
        _errorExpireDate.postValue(expireDateError)
        _errorCurrState.postValue(currStateError)

        return arrayOf(medicineNameError, expireDateError, currStateError).all { it == null }
    }

    private fun getMedicineInputData() = MedicineInputData(
        name = medicineName.value!!,
        unit = medicineUnit.value!!,
        expireDate = expireDate.value!!,
        packageSize = packageSize.value,
        currState = currState.value,
        additionalInfo = additionalInfo.value,
        image = _imageFile.value
    )

    private fun setMedicineData(medicine: Medicine) {
        medicineName.postValue(medicine.name)
        medicineUnit.postValue(medicine.unit)
        expireDate.postValue(medicine.expireDate)
        packageSize.postValue(medicine.packageSize)
        currState.postValue(medicine.currState)
        additionalInfo.postValue(medicine.additionalInfo)
        _imageFile.postValue(medicine.image)
    }

    private fun setEmptyMedicineData() {
        medicineName.postValue(null)
        medicineUnit.postValue(medicineUnitList[0])
        expireDate.postValue(null)
        packageSize.postValue(null)
        currState.postValue(null)
        additionalInfo.postValue(null)
        _imageFile.postValue(null)
    }
}