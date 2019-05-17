package com.example.medihelper.mainapp.medickit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.R
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MedicineDetailsViewModel : ViewModel() {
    val selectedMedicineID = MutableLiveData<Int>()
    val selectedMedicine: LiveData<Medicine>

    val photoLive: LiveData<File>
    val nameLive: LiveData<String>
    val stateWeightLive: LiveData<Float>
    val emptyWeightLive: LiveData<Float>
    val stateNumberStringLive: LiveData<String>
    val stateTextLive: LiveData<String>
    val stateColorResIdLive: LiveData<Int>
    val medicineTypeLive: LiveData<MedicineType>
    val expireDateLive: LiveData<String>
    val daysRemainsLive: LiveData<String>
    val comments: LiveData<String>

    init {
        selectedMedicine = Transformations.switchMap(selectedMedicineID) { medicineID ->
            Repository.getMedicineByIdLive(medicineID)
        }
        photoLive = Transformations.map(selectedMedicine) { medicine ->
            File(medicine.photoFilePath)
        }
        nameLive = Transformations.map(selectedMedicine) { medicine ->
            medicine.name
        }
        stateNumberStringLive = Transformations.map(selectedMedicine) { medicine ->
            stateNumberString(medicine)
        }
        medicineTypeLive = Transformations.switchMap(selectedMedicine) {
            Repository.getMedicineTypeByIdLive(it.medicineTypeID)
        }
        expireDateLive = Transformations.map(selectedMedicine) {
            it.expireDate
        }
        daysRemainsLive = Transformations.map(selectedMedicine) {
            daysRemainsString(it)
        }
        comments = Transformations.map(selectedMedicine) {
            it.comments
        }
        stateWeightLive = Transformations.map(selectedMedicine) {
            stateWeight(it)
        }
        emptyWeightLive = Transformations.map(stateWeightLive) {
            1 - it
        }
        stateTextLive = Transformations.map(stateWeightLive) {
            stateText(it)
        }
        stateColorResIdLive = Transformations.map(stateWeightLive) {
            stateColorResId(it)
        }
    }

    fun deleteMedicine() = selectedMedicine.value?.let { Repository.deleteMedicine(it) }

    private fun stateWeight(medicine: Medicine) = medicine.currState.div(medicine.packageSize)

    private fun stateNumberString(medicine: Medicine): String {
        return "${medicine.currState}/${medicine.packageSize}"
    }

    private fun stateColorResId(stateWeight: Float): Int {
        val stateGoodLimit = KitViewModel.STATE_GOOD_LIMIT
        val stateMediumLimit = KitViewModel.STATE_MEDIUM_LIMIT
        return when {
            stateWeight >= stateGoodLimit -> R.color.colorStateGood
            stateWeight > stateMediumLimit -> R.color.colorStateMedium
            else -> R.color.colorStateSmall
        }
    }

    private fun stateText(stateWeight: Float): String {
        val stateGoodLimit = KitViewModel.STATE_GOOD_LIMIT
        val stateMediumLimit = KitViewModel.STATE_MEDIUM_LIMIT
        return when {
            stateWeight >= stateGoodLimit -> TEXT_STATE_GOOD
            stateWeight > stateMediumLimit -> TEXT_STATE_MEDIUM
            else -> TEXT_STATE_SMALL
        }
    }

    private fun daysRemainsString(medicine: Medicine): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dateString = medicine.expireDate
        val date = dateFormat.parse(dateString)

        val currCalendar = Calendar.getInstance()
        val calendar = Calendar.getInstance().apply {
            time = date
        }

        val daysBetween = daysBetween(currCalendar.time, calendar.time)
        return "$daysBetween dni"
    }

    private fun daysBetween(date1: Date, date2: Date): Int {
        return ((date2.time - date1.time) / (1000 * 60 * 60 * 24)).toInt()
    }

    companion object {
        const val TEXT_STATE_GOOD = "Duży zapas"
        const val TEXT_STATE_MEDIUM = "Średnia ilość"
        const val TEXT_STATE_SMALL = "Blisko końca"
    }
}