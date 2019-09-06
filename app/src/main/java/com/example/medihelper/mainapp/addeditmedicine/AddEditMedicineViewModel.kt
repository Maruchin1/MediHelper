package com.example.medihelper.mainapp.addeditmedicine

import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.MedicineEntity
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class AddEditMedicineViewModel : ViewModel() {
    private val TAG = AddEditMedicineViewModel::class.simpleName

    val medicineUnitList = AppRepository.getMedicineUnitList()

    val medicineNameLive = MutableLiveData<String>()
    val expireDateLive = MutableLiveData<Date>()
    val medicineUnitLive = MutableLiveData<String>()
    val packageSizeLive = MutableLiveData<Float>()
    val currStateLive = MutableLiveData<Float>()
    val commentsLive = MutableLiveData<String>()
    val photoFileLive = MutableLiveData<File>()
    val errorMedicineNameLive = MutableLiveData<String>()
    val errorExpireDateLive = MutableLiveData<String>()
    val errorCurrStateLive = MutableLiveData<String>()
    private var editMedicineID: Int? = null

    fun setArgs(args: AddEditMedicineFragmentArgs) = viewModelScope.launch {
        if (args.editMedicineID != -1) {
            editMedicineID = args.editMedicineID
            AppRepository.getMedicineDetails(args.editMedicineID).run {
                medicineNameLive.postValue(medicineName)
                expireDateLive.postValue(expireDate)
                medicineUnitLive.postValue(medicineUnit)
                packageSizeLive.postValue(packageSize)
                currStateLive.postValue(currState)
                commentsLive.postValue(comments)
                photoFileLive.postValue(photoFilePath?.let { File(it) })
            }
        }
    }

    fun setMedicineType(position: Int) {
        medicineUnitLive.value = medicineUnitList[position]
    }

    fun saveMedicine(): Boolean {
        if (validateInputData()) {
            val medicineEntity = MedicineEntity(
                medicineName = medicineNameLive.value!!,
                expireDate = expireDateLive.value!!,
                medicineUnit = medicineUnitLive.value!!,
                packageSize = packageSizeLive.value,
                currState = currStateLive.value,
                comments = commentsLive.value,
                photoFilePath = photoFileLive.value?.let { photoFile ->
                    AppRepository.createPhotoFileFromTemp(photoFile).absolutePath
                }
            )
            viewModelScope.launch {
                if (editMedicineID != null) {
                    AppRepository.updateMedicine(medicineEntity.copy(medicineID = editMedicineID!!))
                } else {
                    AppRepository.insertMedicine(medicineEntity)
                }
            }
            return true
        }
        return false
    }

    fun takePhotoIntent(activity: FragmentActivity): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                val photoFile = AppRepository.createTempPhotoFile()
                photoFileLive.value = photoFile
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    "com.example.medihelper.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        }
    }

    fun resetViewModel() {
        arrayOf(
            medicineNameLive,
            medicineUnitLive,
            packageSizeLive,
            currStateLive,
            expireDateLive,
            commentsLive,
            photoFileLive
        ).forEach { field ->
            field.value = null
        }
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