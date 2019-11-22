package com.example.medihelper.presentation.feature.medikit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.presentation.framework.FieldMutableLiveData
import com.example.medihelper.domain.usecases.MedicineUseCases
import com.example.medihelper.presentation.model.MedicineForm
import com.example.medihelper.presentation.model.MedicineFormError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class AddEditMedicineViewModel(
    private val medicineUseCases: MedicineUseCases
) : ViewModel() {

    val formModel: MutableLiveData<MedicineForm>
        get() = _formModel
    val formTitle: LiveData<String>
        get() = _formTitle
    val formErrorModel: LiveData<MedicineFormError>
        get() = _formErrorModel
    val imageFile: LiveData<File>
        get() = _imageFile

    private val _formTitle = MutableLiveData<String>("Dodaj lek")
    private val _formErrorModel =
        FieldMutableLiveData<MedicineFormError>()
    private var _formModel = FieldMutableLiveData<MedicineForm>()
    private val _imageFile = MutableLiveData<File>()

    private val medicineUnitList: List<String> = medicineUseCases.getMedicineUnitList()
    private var editMedicineId: Int? = null

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        if (args.editMedicineId != -1) {
            editMedicineId = args.editMedicineId
            _formTitle.postValue("Edytuj lek")
            val editMedicine = medicineUseCases.getMedicineById(args.editMedicineId)
            val editMedicineForm = MedicineForm(editMedicine)
            _formModel.postValue(editMedicineForm)
            _imageFile.postValue(editMedicine.image)
        } else {
            _formModel.postValue(getEmptyForm())
        }
    }

    fun saveMedicine(): Boolean {
        if (isFormValid()) {
            val medicineId = editMedicineId
            val inputData = formModel.value!!.toInputData(_imageFile.value)
            if (medicineId == null) {
                GlobalScope.launch {
                    medicineUseCases.addNewMedicine(inputData)
                }
            } else {
                GlobalScope.launch {
                    medicineUseCases.updateMedicine(medicineId, inputData)
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
        val medicineName = _formModel.value?.name
        val expireDate = _formModel.value?.expireDate
        val packageSize = _formModel.value?.packageSize
        val currState = _formModel.value?.currState

        val medicineNameError = if (medicineName.isNullOrEmpty()) {
            "Pole jest wymagane"
        } else null
        val expireDateError = if (expireDate == null) {
            "Pole jest wymagene"
        } else null
        val currStateError = if (packageSize != null && currState != null && currState > packageSize) {
            "Większe niż rozmiar opakowania"
        } else null

        formErrorModel.value?.apply {
            errorMedicineName = medicineNameError
            errorExpireDate = expireDateError
            errorCurrState = currStateError
        }

        return arrayOf(medicineNameError, expireDateError, currStateError).all { it == null }
    }

    private fun getEmptyForm() = MedicineForm(
        _name = null,
        _unit = medicineUnitList[0],
        _expireDate = null,
        _packageSize = null,
        _currState = null,
        _additionalInfo = null
    )
}