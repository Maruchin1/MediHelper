package com.example.medihelper.mainapp.medickit

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.R
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.io.File
import java.util.*

class AddMedicineViewModel : ViewModel() {
    private val TAG = AddMedicineViewModel::class.simpleName

    val defaultMedicineIdLive = MutableLiveData<Int>()
    val defaultMedicineLive: LiveData<Medicine>
    val medicineTypesListLive = Repository.getMedicineTypesLive()
    val tmpPhotoFileLive = MutableLiveData<File>()

    val medicineNameLive = MutableLiveData<String>()
    val medicineTypeLive = MutableLiveData<MedicineType>()
    val medicineCapacityLive = MutableLiveData<String>()
    val medicineCurrStateLive = MutableLiveData<String>()
    val medicineExpireDate = MutableLiveData<String>()

    init {
        defaultMedicineLive = Transformations.switchMap(defaultMedicineIdLive) {
            if (it != -1) {
                Repository.getMedicineByIdLive(it)
            } else {
                null
            }
        }
        defaultMedicineLive.observeForever {
            if (it != null) {
                val medicineType = medicineTypesListLive.value?.find { medicineType ->
                    medicineType.medicineTypeID == it.medicineTypeID }
                medicineNameLive.value = it.name
                medicineTypeLive.value = medicineType
                medicineCapacityLive.value = it.packageSize.toString()
                medicineCurrStateLive.value = it.currState.toString()
                medicineExpireDate.value = it.expireDate
                tmpPhotoFileLive.value = File(it.photoFilePath)
            }
        }
    }

    fun setMedicineType(position: Int) {
        medicineTypesListLive.value?.let {
            medicineTypeLive.value = it[position]
        }
    }

    fun saveNewMedicine(context: Context): Boolean {
        Log.d(TAG, "onClickSaveNewMedicine")
        val name = medicineNameLive.value
        val type = medicineTypeLive.value
        val capacity = medicineCapacityLive.value
        val currState = medicineCurrStateLive.value
        val photoFilePath = tmpPhotoFileLive.value?.let {
            Repository.createPhotoFileFromTemp(it).absolutePath
        } ?: ""
        val expireDate = medicineExpireDate.value

        val attributes = arrayOf(name, type, capacity, currState, expireDate)
        if (attributes.all { attribute -> attribute != null }) {
            val medicine = Medicine(
                name = name!!,
                medicineTypeID = type!!.medicineTypeID!!,
                packageSize = capacity!!.toFloat(),
                currState = currState!!.toFloat(),
                photoFilePath = photoFilePath,
                expireDate = expireDate!!
            )
            Repository.insertMedicine(medicine)
            return true
        } else {
            Toast.makeText(context, "Some atributes are null", Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun takePhotoIntent(activity: FragmentActivity): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                val photoFile = Repository.createTempPhotoFile()
                tmpPhotoFileLive.value = photoFile
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    "com.example.medihelper.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        }
    }

    fun showExpireDateDialogPicker(context: Context) {
        val calendar = Calendar.getInstance()
        val currYear = calendar.get(Calendar.YEAR)
        val currMonth = calendar.get(Calendar.MONTH)
        val currDay = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            context,
            R.style.DateDialogPicker,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val selectedMonth = if (month < 10) "0$month" else "$month"
                medicineExpireDate.value = "$selectedDay-$selectedMonth-$year"
            },
            currYear,
            currMonth,
            currDay
        ).show()
    }

    fun resetViewModel() {
        val fieldsLive = arrayOf(
            medicineNameLive,
            medicineTypeLive,
            medicineCapacityLive,
            medicineCurrStateLive,
            medicineExpireDate
        )
        fieldsLive.forEach { field ->
            field.value = null
        }
    }
}