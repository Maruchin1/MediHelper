package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.utils.MedicineValidator
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineEditDataUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineUnitsUseCase
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.device.camera.DeviceCamera
import com.maruchin.medihelper.presentation.utils.PicturesRef
import kotlinx.coroutines.launch
import java.io.File

class AddEditMedicineViewModel(
    private val getMedicineUnitsUseCase: GetMedicineUnitsUseCase,
    private val getMedicineEditDataUseCase: GetMedicineEditDataUseCase,
    private val saveMedicineUseCase: SaveMedicineUseCase,
    private val deviceCamera: DeviceCamera,
    private val picturesRef: PicturesRef
) : ViewModel() {

    val medicineUnitList: LiveData<List<String>>

    val formTitle: LiveData<String>
    val medicineName = MutableLiveData<String>()
    val medicineUnit = MutableLiveData<String>()
    val expireDate = MutableLiveData<AppExpireDate>()
    val packageSize = MutableLiveData<Float>(0f)
    val currState = MutableLiveData<Float>(0f)
    val pictureFile: LiveData<File>

    val pictureRef: LiveData<StorageReference>
        get() = _pictureRef
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

    private val _pictureRef = MutableLiveData<StorageReference>()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionMedicineSaved = ActionLiveData()
    private val _errorMedicineName = MutableLiveData<String>()
    private val _errorMedicineUnit = MutableLiveData<String>()
    private val _errorExpireDate = MutableLiveData<String>()
    private val _errorCurrState = MutableLiveData<String>()

    private var editMedicineId = MutableLiveData<String>()

    init {
        formTitle = Transformations.map(editMedicineId) {
            if (it == null) {
                "Dodaj lek"
            } else {
                "Edytuj lek"
            }
        }
        medicineUnitList = liveData {
            val list = getMedicineUnitsUseCase.execute()
            emit(list)
        }
        pictureFile = deviceCamera.resultFile
    }

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        editMedicineId.postValue(args.editMedicineId)
        if (args.editMedicineId != null) {
            getMedicineEditDataUseCase.execute(args.editMedicineId)?.let { editData ->
                setEditData(editData)
            }
        }
    }

    fun saveMedicine() = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        val params = SaveMedicineUseCase.Params(
            medicineId = editMedicineId.value,
            name = medicineName.value,
            unit = medicineUnit.value,
            expireDate = expireDate.value,
            packageSize = packageSize.value,
            currState = currState.value,
            pictureName = pictureRef.value?.name,
            pictureFile = pictureFile.value
        )
        val errors = saveMedicineUseCase.execute(params)
        _loadingInProgress.postValue(false)

        if (errors.noErrors) {
            _actionMedicineSaved.sendAction()
        } else {
            postErrors(errors)
        }
    }

    private fun postErrors(errors: MedicineValidator.Errors) {
        val medicineNameError = if (errors.emptyName) {
            "Pole jest wymagane"
        } else null
        val medicineUnitError = if (errors.emptyUnit) {
            "Pole jest wymagane"
        } else null
        val expireDateError = if (errors.emptyExpireDate) {
            "Pole jest wymagene"
        } else null
        val currStateError = if (errors.currStateBiggerThanPackageSize) {
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
        editData.pictureName?.let {
            _pictureRef.postValue(picturesRef.get(it))
        }
    }
}