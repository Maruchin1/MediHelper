package com.example.medihelper.mainapp.medicine

import android.app.Activity
import androidx.lifecycle.*
import com.example.medihelper.localdata.type.AppExpireDate
import com.example.medihelper.localdata.pojo.MedicineEditData
import com.example.medihelper.service.MedicineService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class AddEditMedicineViewModel(
    private val medicineService: MedicineService
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

    fun capturePhoto(activity: Activity) {
        val captureIntent = medicineService.getTempImageCaptureIntent(imageFileLive)
        activity.startActivity(captureIntent)
    }

    private fun validateInputData(): Boolean {
        var inputDataValid = true
        if (medicineNameLive.value.isNullOrEmpty()) {
            errorMedicineNameLive.value = "Pole jest wymagane"
            inputDataValid = false
        } else {
            errorMedicineNameLive.value = null
        }
        if (expireDateLive.value == null) {
            errorExpireDateLive.value = "Pole jest wymagane"
            inputDataValid = false
        } else {
            errorExpireDateLive.value = null
        }
        if (packageSizeLive.value != null &&
            currStateLive.value != null &&
            currStateLive.value!! > packageSizeLive.value!!
        ) {
            errorCurrStateLive.value = "Większe niż rozmiar opakowania"
            inputDataValid = false
        } else {
            errorCurrStateLive.value = null
        }
        return inputDataValid
    }
}