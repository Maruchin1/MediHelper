package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.model.MedicineValidator
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineEditDataUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineUnitsUseCase
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.device.camera.DeviceCamera
import kotlinx.coroutines.launch
import java.io.File

class AddEditMedicineViewModel(
    private val getMedicineUnitsUseCase: GetMedicineUnitsUseCase,
    private val getMedicineEditDataUseCase: GetMedicineEditDataUseCase,
    private val saveMedicineUseCase: SaveMedicineUseCase,
    private val deviceCamera: DeviceCamera
) : ViewModel() {

    val medicineUnitList: LiveData<List<String>>

    val medicineName = MutableLiveData<String>()
    val medicineUnit = MutableLiveData<String>()
    val expireDate = MutableLiveData<AppExpireDate>()
    val packageSize = MutableLiveData<Float>(0f)
    val currState = MediatorLiveData<Float>()
    val additionalInfo = MutableLiveData<String>()

    val formTitle: LiveData<String>
        get() = _formTitle
    val imageFile: LiveData<File>
        get() = _imageFile
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionMedicineSaved: LiveData<Boolean>
        get() = _actionMedicineSaved
    val errorMedicineName: LiveData<String>
        get() = _errorMedicineName
    val errorMedicineUnit: LiveData<String>
        get() = _errorMedicineUnit
    val errorExpireDate: LiveData<String>
        get() = _errorExpireDate
    val errorCurrState: LiveData<String>
        get() = _errorCurrState

    private val _formTitle = MutableLiveData<String>("Dodaj lek")
    private val _imageFile = MediatorLiveData<File>()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionMedicineSaved = ActionLiveData()
    private val _errorMedicineName = MutableLiveData<String>()
    private val _errorMedicineUnit = MutableLiveData<String>()
    private val _errorExpireDate = MutableLiveData<String>()
    private val _errorCurrState = MutableLiveData<String>()

    private var editMedicineId: String? = null

    init {
        medicineUnitList = liveData {
            val list = getMedicineUnitsUseCase.execute()
            emit(list)
        }
        currState.addSource(packageSize) { currState.postValue(it) }
        _imageFile.addSource(deviceCamera.resultFile) { _imageFile.postValue(it) }
    }

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        if (args.editMedicineId != null) {
            _formTitle.postValue("Edytuj lek")
            editMedicineId = args.editMedicineId
            getMedicineEditDataUseCase.execute(args.editMedicineId)?.let { editData ->
                setEditData(editData)
            }
        }
    }

    fun saveMedicine() = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        val params = SaveMedicineUseCase.Params(
            medicineId = editMedicineId,
            name = medicineName.value,
            unit = medicineUnit.value,
            expireDate = expireDate.value,
            packageSize = packageSize.value,
            currState = currState.value,
            additionalInfo = additionalInfo.value,
            pictureFile = imageFile.value
        )
        val validator = saveMedicineUseCase.execute(params)
        _loadingInProgress.postValue(false)

        if (validator.noErrors) {
            _actionMedicineSaved.sendAction()
        } else {
            postErrors(validator)
        }
    }

    private fun postErrors(validator: MedicineValidator) {
        val medicineNameError = if (validator.emptyName) {
            "Pole jest wymagane"
        } else null
        val medicineUnitError = if (validator.emptyUnit) {
            "Pole jest wymagane"
        } else null
        val expireDateError = if (validator.emptyExpireDate) {
            "Pole jest wymagene"
        } else null
        val currStateError = if (validator.currStateBiggerThanPackageSize) {
            "Większe niż rozmiar opakowania"
        } else null

        _errorMedicineName.postValue(medicineNameError)
        _errorMedicineUnit.postValue(medicineUnitError)
        _errorExpireDate.postValue(expireDateError)
        _errorCurrState.postValue(currStateError)
    }

    private fun setEditData(editData: MedicineEditData) {
        medicineName.postValue(editData.name)
        medicineUnit.postValue(editData.unit)
        expireDate.postValue(editData.expireDate)
        packageSize.postValue(editData.packageSize)
        currState.postValue(editData.currState)
        additionalInfo.postValue(editData.additionalInfo)
    }
}