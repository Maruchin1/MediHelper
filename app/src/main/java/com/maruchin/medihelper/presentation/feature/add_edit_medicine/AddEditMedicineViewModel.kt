package com.maruchin.medihelper.presentation.feature.add_edit_medicine

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineEditDataUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineUnitsUseCase
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.device.camera.DeviceCamera
import com.maruchin.medihelper.domain.model.MedicineErrors
import com.maruchin.medihelper.presentation.utils.PicturesStorageRef
import kotlinx.coroutines.launch
import java.io.File

class AddEditMedicineViewModel(
    private val getMedicineUnitsUseCase: GetMedicineUnitsUseCase,
    private val getMedicineEditDataUseCase: GetMedicineEditDataUseCase,
    private val saveMedicineUseCase: SaveMedicineUseCase,
    private val deviceCamera: DeviceCamera,
    private val picturesStorageRef: PicturesStorageRef
) : ViewModel() {

    val medicineUnitList: LiveData<List<String>>

    val formTitle: LiveData<String>
    val medicineName = MutableLiveData<String>()
    val medicineUnit = MutableLiveData<String>()
    val expireDate = MutableLiveData<AppExpireDate>()
    val currState = MutableLiveData<Float>(0f)
    val packageSize = MutableLiveData<Float>(0f)
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
    val errorCurrState: LiveData<String>
        get() = _errorCurrState

    private val _pictureRef = MutableLiveData<StorageReference>()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionMedicineSaved = ActionLiveData()
    private val _errorMedicineName = MutableLiveData<String>()
    private val _errorMedicineUnit = MutableLiveData<String>()
    private val _errorCurrState = MutableLiveData<String>()

    private var editMedicineId = MutableLiveData<String>(null)

    init {
        formTitle = getLiveFormTitle()
        medicineUnitList = getLiveMedicineUnits()
        pictureFile = getLivePictureFile()
    }

    fun initViewModel(editMedicineId: String?) = viewModelScope.launch {
        if (editMedicineId != null) {
            loadAndSetEditData(editMedicineId)
        }
    }

    fun saveMedicine() = viewModelScope.launch {
        notifySavingInProgress()
        val params = getSaveUseCaseParams()
        val errors = saveMedicineUseCase.execute(params)
        notifySavingNotInProgress()
        checkSavingErrors(errors)
    }

    override fun onCleared() {
        deviceCamera.reset()
        super.onCleared()
    }

    private fun getLiveFormTitle(): LiveData<String> {
        return Transformations.map(editMedicineId) {
            if (it == null) {
                "Dodaj lek"
            } else {
                "Edytuj lek"
            }
        }
    }

    private fun getLiveMedicineUnits(): LiveData<List<String>> {
        return liveData {
            val list = getMedicineUnitsUseCase.execute()
            emit(list)
        }
    }

    private fun getLivePictureFile(): LiveData<File> {
        return deviceCamera.resultFile
    }

    private suspend fun loadAndSetEditData(medicineId: String) {
        editMedicineId.postValue(medicineId)
        val editData = getMedicineEditDataUseCase.execute(medicineId)
        setEditData(editData)
    }

    private fun setEditData(editData: MedicineEditData) {
        medicineName.postValue(editData.name)
        medicineUnit.postValue(editData.unit)
        expireDate.postValue(editData.expireDate)
        packageSize.postValue(editData.state.packageSize)
        currState.postValue(editData.state.currState)
        if (editData.pictureName != null) {
            val editPictureRef = picturesStorageRef.getPictureRef(editData.pictureName)
            _pictureRef.postValue(editPictureRef)
        }
    }

    private fun getSaveUseCaseParams(): SaveMedicineUseCase.Params {
        return SaveMedicineUseCase.Params(
            medicineId = editMedicineId.value,
            name = medicineName.value,
            unit = medicineUnit.value,
            expireDate = expireDate.value,
            packageSize = packageSize.value,
            currState = currState.value,
            oldPictureName = pictureRef.value?.name,
            newPictureFile = pictureFile.value
        )
    }

    private fun notifySavingInProgress() {
        _loadingInProgress.postValue(true)
    }

    private fun notifySavingNotInProgress() {
        _loadingInProgress.postValue(false)
    }

    private fun checkSavingErrors(errors: MedicineErrors) {
        if (errors.noErrors) {
            notifyMedicineSaved()
        } else {
            postErrors(errors)
        }
    }

    private fun notifyMedicineSaved() {
        _actionMedicineSaved.sendAction()
    }

    private fun postErrors(errors: MedicineErrors) {
        val medicineNameError = if (errors.emptyName) {
            "Pole jest wymagane"
        } else null
        val medicineUnitError = if (errors.emptyUnit) {
            "Pole jest wymagane"
        } else null
        val currStateError = if (errors.currStateBiggerThanPackageSize) {
            "Większe niż rozmiar opakowania"
        } else null

        _errorMedicineName.postValue(medicineNameError)
        _errorMedicineUnit.postValue(medicineUnitError)
        _errorCurrState.postValue(currStateError)
    }
}