package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineUnitsUseCase
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch
import java.io.File

class AddEditMedicineViewModel(
    private val getMedicineUnitsUseCase: GetMedicineUnitsUseCase,
    private val saveMedicineUseCase: SaveMedicineUseCase
) : ViewModel() {

    val medicineName = MutableLiveData<String>()
    val medicineUnit = MutableLiveData<String>("tabletki")
    val expireDate = MutableLiveData<AppExpireDate>()
    val packageSize = MutableLiveData<Float>()
    val currState = MutableLiveData<Float>()
    val additionalInfo = MutableLiveData<String>()

    val formTitle: LiveData<String>
        get() = _formTitle
    val imageFile: LiveData<File>
        get() = _imageFile
    val actionMedicineSaved: LiveData<Boolean>
        get() = _actionMedicineSaved
    val errorMedicineName: LiveData<String>
        get() = _errorMedicineName
    val errorExpireDate: LiveData<String>
        get() = _errorExpireDate
    val errorCurrState: LiveData<String>
        get() = _errorCurrState

    private val _formTitle = MutableLiveData<String>("Dodaj lek")
    private val _imageFile = MutableLiveData<File>()
    private val _actionMedicineSaved = ActionLiveData()
    private val _errorMedicineName = MutableLiveData<String>()
    private val _errorExpireDate = MutableLiveData<String>()
    private val _errorCurrState = MutableLiveData<String>()

    private val medicineUnitList = MutableLiveData<List<String>>()
    private var editMedicineId: String? = null

    init {
        viewModelScope.launch {
            medicineUnitList.postValue(getMedicineUnitsUseCase.execute())
        }
    }

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        if (args.editMedicineId != null) {
//            editMedicineId = args.editMedicineId
            _formTitle.postValue("Edytuj lek")
//            val editMedicine = medicineUseCases.getMedicineById(args.editMedicineId)
//            setMedicineData(editMedicine)
        } else {
//            setEmptyMedicineData()
        }
    }

    fun saveMedicine() = viewModelScope.launch {
        val params = SaveMedicineUseCase.Params(
            medicineId = editMedicineId,
            name = medicineName.value,
            unit = medicineUnit.value,
            expireDate = expireDate.value,
            packageSize = packageSize.value,
            currState = currState.value,
            additionalInfo = additionalInfo.value
        )
        val result = saveMedicineUseCase.execute(params)
        if (result.noErrors) {
            _actionMedicineSaved.sendAction()
        } else {
            postErrors(result)
        }
    }

    fun capturePhoto() {
//        medicineUseCases.captureMedicinePhoto(_imageFile)
    }

    private fun postErrors(validationErrors: SaveMedicineUseCase.ValidationErrors) {
        val medicineNameError = if (validationErrors.emptyName) {
            "Pole jest wymagane"
        } else null
        val expireDateError = if (validationErrors.emptyExpireDate) {
            "Pole jest wymagene"
        } else null
        val currStateError = if (validationErrors.currStateBiggerThanPackageSize) {
            "Większe niż rozmiar opakowania"
        } else null

        _errorMedicineName.postValue(medicineNameError)
        _errorExpireDate.postValue(expireDateError)
        _errorCurrState.postValue(currStateError)
    }

    private fun setMedicineData(medicine: Medicine) {
        medicineName.postValue(medicine.name)
        medicineUnit.postValue(medicine.unit)
        expireDate.postValue(medicine.expireDate)
        packageSize.postValue(medicine.packageSize)
        currState.postValue(medicine.currState)
        additionalInfo.postValue(medicine.additionalInfo)
//        _imageFile.postValue(medicine.image)
    }

//    private fun setEmptyMedicineData() {
//        medicineName.postValue(null)
//        medicineUnit.postValue(medicineUnitList[0])
//        expireDate.postValue(null)
//        packageSize.postValue(null)
//        currState.postValue(null)
//        additionalInfo.postValue(null)
//        _imageFile.postValue(null)
//    }
}