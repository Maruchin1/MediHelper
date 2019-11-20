package com.example.medihelper.presentation.feature.medikit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.domain.usecases.MedicineUseCases
import com.example.medihelper.presentation.model.MedicineForm
import com.example.medihelper.presentation.model.MedicineFormError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditMedicineViewModel(
    private val medicineUseCases: MedicineUseCases
) : ViewModel() {

    val formModel: MutableLiveData<MedicineForm>
        get() = _formModel
    val formTitle: LiveData<String>
        get() = _formTitle
    val formErrorModel: LiveData<MedicineFormError>
        get() = _formErrorModel

    private val _formTitle = MutableLiveData<String>("Dodaj lek")
    private val _formErrorModel = FieldMutableLiveData<MedicineFormError>()
    private var _formModel = FieldMutableLiveData<MedicineForm>()

    private var editMedicineId: Int? = null

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        if (args.editMedicineId != -1) {
            editMedicineId = args.editMedicineId
            _formTitle.postValue("Edytuj lek")
            val editMedicine = medicineUseCases.getMedicine(args.editMedicineId)
            val editMedicineForm = MedicineForm(editMedicine)
            _formModel.postValue(editMedicineForm)
        }
    }

    fun saveMedicine(): Boolean {
        if (isFormValid()) {
            val medicineId = editMedicineId
            val inputData = formModel.value!!.toInpuData()
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

    //todo wyodrębnić te trzy do pakietu device
    fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun askForCameraPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            requestCode
        )
    }

    fun capturePhoto(fragment: Fragment) {
//        val captureIntent = medicineService.getTempImageCaptureIntent(imageFileLive)
//        fragment.startActivity(captureIntent)
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
}