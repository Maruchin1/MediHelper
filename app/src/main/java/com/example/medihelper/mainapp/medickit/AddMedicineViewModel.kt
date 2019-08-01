package com.example.medihelper.mainapp.medickit

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.io.File
import java.util.*

class AddMedicineViewModel : ViewModel() {
    private val TAG = AddMedicineViewModel::class.simpleName

    val selectedMedicineIdLive = MutableLiveData<Int>()
    val selectedMedicineLive: LiveData<Medicine>
    val medicineTypesListLive = AppRepository.getMedicineTypesLive()

    val nameLive = MutableLiveData<String>()
    val medicineTypeLive = MutableLiveData<MedicineType>()
    val capacityLive = MutableLiveData<String>()
    val currStateLive = MutableLiveData<String>()
    val expireDateLive = MutableLiveData<Date>()
    val commentsLive = MutableLiveData<String>()
    val photoFileLive = MutableLiveData<File>()

    init {
        selectedMedicineLive = Transformations.switchMap(selectedMedicineIdLive) { medicineId ->
            AppRepository.getMedicineByIdLive(medicineId)
        }
        selectedMedicineLive.observeForever { medicine ->
            if (medicine != null) {
                nameLive.value = medicine.name
                medicineTypesListLive.value?.let { typesList ->
                    typesList.find { medicineType ->
                        medicineType.medicineTypeID == medicine.medicineTypeID
                    }
                }
                capacityLive.value = medicine.packageSize?.toString()
                currStateLive.value = medicine.currState?.toString()
                expireDateLive.value = medicine.expireDate
                commentsLive.value = medicine.comments
                photoFileLive.value = medicine.photoFilePath?.let { photoFilePath ->
                    File(photoFilePath)
                }
            }
        }
    }

    fun setMedicineType(position: Int) {
        medicineTypesListLive.value?.let {
            medicineTypeLive.value = it[position]
        }
    }

    fun saveMedicine(): Boolean {
        Log.d(TAG, "onClickSaveNewMedicine")
        val name = nameLive.value
        val type = medicineTypeLive.value
        val capacity = capacityLive.value
        val currState = currStateLive.value
        val photoFilePath = photoFileLive.value?.let { photoFile ->
            AppRepository.createPhotoFileFromTemp(photoFile).absolutePath
        }
        val expireDate = expireDateLive.value
        val comments = commentsLive.value

        if (name == null) {
            return false
        }

        selectedMedicineLive.value?.let { medicineToUpdate ->
            medicineToUpdate.apply {
                this.name = name
                this.medicineTypeID = type?.medicineTypeID
                this.packageSize = capacity?.toFloat()
                this.currState = currState?.toFloat()
                this.photoFilePath = photoFilePath
                this.expireDate = expireDate
                this.comments = comments
            }
            AppRepository.updateMedicine(medicineToUpdate)
            return true
        }

        val newMedicine = Medicine(
            name = name,
            medicineTypeID = type?.medicineTypeID,
            packageSize = capacity?.toFloat(),
            currState = currState?.toFloat(),
            photoFilePath = photoFilePath,
            expireDate = expireDate,
            comments = comments
        )
        AppRepository.insertMedicine(newMedicine)
        return true
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
            nameLive,
            medicineTypeLive,
            capacityLive,
            currStateLive,
            expireDateLive,
            commentsLive,
            photoFileLive
        ).forEach { field ->
            field.value = null
        }
    }
}