package com.example.medihelper.mainapp.medicines

import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.services.MedicineImageService
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class AddEditMedicineViewModel(
    private val appFileDir: File,
    private val medicineRepository: MedicineRepository,
    private val medicineImageService: MedicineImageService,
    sharedPrefService: SharedPrefService
) : ViewModel() {
    private val TAG = AddEditMedicineViewModel::class.simpleName

    val medicineUnitList = sharedPrefService.getMedicineUnitList()

    val titleLive = MutableLiveData("Dodaj lek")
    val medicineNameLive = MutableLiveData<String>()
    val expireDateLive = MutableLiveData<AppDate>()
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
            medicineRepository.getDetails(args.editMedicineID).run {
                medicineNameLive.postValue(medicineName)
                expireDateLive.postValue(expireDate)
                medicineUnitLive.postValue(medicineUnit)
                packageSizeLive.postValue(packageSize)
                currStateLive.postValue(currState)
                commentsLive.postValue(additionalInfo)
                imageFileLive.postValue(imageName?.let { File(appFileDir, it) })
            }
        }
    }

    fun saveMedicine(): Boolean {
        if (validateInputData()) {
            if (editMedicineID != null) {
                GlobalScope.launch {
                    val existingMedicineEntity = medicineRepository.getEntity(editMedicineID!!)
                    val updatedMedicineEntity = existingMedicineEntity.copy(
                        medicineName = medicineNameLive.value!!,
                        medicineUnit = medicineUnitLive.value!!,
                        expireDate = expireDateLive.value!!,
                        packageSize = packageSizeLive.value,
                        currState = currStateLive.value,
                        additionalInfo = commentsLive.value,
                        imageName = imageFileLive.value?.let { imageFile ->
                            medicineImageService.saveTmpFile(medicineNameLive.value!!, imageFile)
                        }
                    )
                    medicineRepository.update(updatedMedicineEntity)
                }
            } else {
                GlobalScope.launch {
                    val newMedicineEntity = MedicineEntity(
                        medicineName = medicineNameLive.value!!,
                        expireDate = expireDateLive.value!!,
                        medicineUnit = medicineUnitLive.value!!,
                        packageSize = packageSizeLive.value,
                        currState = currStateLive.value,
                        additionalInfo = commentsLive.value,
                        imageName = imageFileLive.value?.let { imageFile ->
                            medicineImageService.saveTmpFile(medicineNameLive.value!!, imageFile)
                        }
                    )
                    medicineRepository.insert(newMedicineEntity)
                }
            }
            return true
        }
        return false
    }

    fun takePhotoIntent(activity: FragmentActivity): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                val imageFile = medicineImageService.createTempImageFile()
                imageFileLive.value = imageFile
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    "com.example.medihelper.fileprovider",
                    imageFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
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