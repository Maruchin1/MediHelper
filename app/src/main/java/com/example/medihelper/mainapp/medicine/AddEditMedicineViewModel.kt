package com.example.medihelper.mainapp.medicine

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.medihelper.localdata.type.AppExpireDate
import com.example.medihelper.localdata.pojo.MedicineEditData
import com.example.medihelper.service.MedicineService
import com.example.medihelper.service.FormValidatorService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class AddEditMedicineViewModel(
    private val medicineService: MedicineService,
    private val formValidatorService: FormValidatorService
) : ViewModel() {
    private val TAG = AddEditMedicineViewModel::class.simpleName

    val medicineUnitList = medicineService.getMedicineUnitList()

    val titleLive = MutableLiveData("Dodaj lek")
    val medicineNameLive = MutableLiveData<String>()
    val expireDateLive = MutableLiveData<AppExpireDate>()
    val medicineUnitLive = MutableLiveData<String>(medicineUnitList[0])
    val packageSizeLive = MutableLiveData<Float>()
    val currStateLive = MutableLiveData<Float>()
    val commentsLive = MutableLiveData<String>()
    val imageFileLive = MutableLiveData<File>()
    val errorMedicineNameLive = MutableLiveData<String>()
    val errorExpireDateLive = MutableLiveData<String>()
    val errorCurrStateLive = MutableLiveData<String>()
    private var editMedicineID: Int? = null

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        if (args.editMedicineID != -1) {
            editMedicineID = args.editMedicineID
            titleLive.postValue("Edytuj lek")
            medicineService.getDetails(args.editMedicineID).run {
                medicineNameLive.postValue(medicineName)
                expireDateLive.postValue(expireDate)
                medicineUnitLive.postValue(medicineUnit)
                packageSizeLive.postValue(packageSize)
                currStateLive.postValue(currState)
                commentsLive.postValue(additionalInfo)
                imageFileLive.postValue(imageName?.let { medicineService.getImageFile(it) })
            }
        }
    }

    fun saveMedicine(): Boolean {
        if (validateInputData()) {
            val editData = MedicineEditData(
                medicineId = editMedicineID ?: 0,
                medicineName = medicineNameLive.value!!,
                medicineUnit = medicineUnitLive.value!!,
                expireDate = expireDateLive.value!!,
                packageSize = packageSizeLive.value,
                currState = currStateLive.value,
                additionalInfo = commentsLive.value,
                imageName = imageFileLive.value?.let { imageFile ->
                    medicineService.saveTempFileAsPerma(medicineNameLive.value!!, imageFile)
                }
            )
            GlobalScope.launch {
                medicineService.save(editData)
            }
            return true
        }
        return false
    }

    fun isCameraPermissionGranted(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    fun askForCameraPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            requestCode
        )
    }

    fun capturePhoto(fragment: Fragment) {
        val captureIntent = medicineService.getTempImageCaptureIntent(imageFileLive)
        fragment.startActivity(captureIntent)
    }

    private fun validateInputData(): Boolean {
        val error = formValidatorService.isMedicineValid(
            medicineName = medicineNameLive.value,
            expireDate = expireDateLive.value,
            packageSize = packageSizeLive.value,
            currState = currStateLive.value
        )
        var isValid = true
        errorMedicineNameLive.value = if (error.emptyName) {
            isValid = false
            "Pole jest wymagane"
        } else null
        errorExpireDateLive.value = if (error.emptyExpireDate) {
            isValid = false
            "Pole jest wymagane"
        } else null
        errorCurrStateLive.value = if (error.currStateBiggerThanPackageSize) {
            isValid = false
            "Większe niż rozmiar opackowania"
        } else null
        return isValid
    }

    private fun requestCameraPermission(activity: Activity) {

    }
}