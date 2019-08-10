package com.example.medihelper.mainapp.schedule

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTime
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import java.io.File

class PlannedMedicineOptionsViewModel : ViewModel() {
    private val TAG = PlannedMedicineOptionsViewModel::class.simpleName

    val plannedMedicineIDLive = MutableLiveData<Int>()
    val plannedMedicineDetailsLive: LiveData<PlannedMedicineDetails>

    val medicineNameLive: LiveData<String>
    val medicineUnitLive: LiveData<String>
    val statusOfTakingLive: LiveData<String>
    val statusOfTakingColorIdLive: LiveData<Int>
    val plannedDateLive: LiveData<String>
    val plannedTimeLive: LiveData<String>
    val dozeSizeLive: LiveData<String>
    val medicineImageFileLive: LiveData<File>

    val takeMedicineBtnText: LiveData<String>
    val takeMedicineBtnIcon: LiveData<Int>

    init {
        plannedMedicineDetailsLive = Transformations.switchMap(plannedMedicineIDLive) { plannedMedicineID ->
            AppRepository.getPlannedMedicineDetailsLive(plannedMedicineID)
        }

        medicineNameLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            plannedMedicine.medicineName
        }
        statusOfTakingLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            when (plannedMedicine.statusOfTaking) {
                PlannedMedicineEntity.StatusOfTaking.WAITING -> "Oczekujacy na przyjęcie"
                PlannedMedicineEntity.StatusOfTaking.TAKEN -> "Przyjęty o godzinie 00:00"
                PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN -> "Nieprzyjety o zaplanowanej godzinie"
            }
        }
        statusOfTakingColorIdLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            when (plannedMedicine.statusOfTaking) {
                PlannedMedicineEntity.StatusOfTaking.WAITING -> R.color.colorDarkerGray
                PlannedMedicineEntity.StatusOfTaking.TAKEN -> R.color.colorStateGood
                PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN -> R.color.colorStateSmall
            }
        }
        plannedDateLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            plannedMedicine?.let { AppDateTime.dateToString(it.plannedDate) }
        }
        plannedTimeLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            plannedMedicine?.let { AppDateTime.timeToString(it.plannedTime) }
        }
        dozeSizeLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            plannedMedicine.plannedDoseSize.toString()
        }
        medicineUnitLive = Transformations.map(plannedMedicineDetailsLive) { medicineType ->
            medicineType.medicineUnit
        }
        medicineImageFileLive = Transformations.map(plannedMedicineDetailsLive) { medicine ->
            medicine?.photoFilePath?.let { File(it) }
        }
        takeMedicineBtnText = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            if (plannedMedicine.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
                "Anuluj przyjecie leku"
            } else {
                "Przyjmij lek"
            }
        }
        takeMedicineBtnIcon = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            if (plannedMedicine.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
                R.drawable.round_close_black_24
            } else {
                R.drawable.baseline_check_white_24
            }
        }
    }

    fun changePlannedMedicineStatus() = AsyncTask.execute {
        plannedMedicineDetailsLive.value?.plannedMedicineID?.let { plannedMedicineID ->
            AppRepository.getPlannedMedicine(plannedMedicineID).let { plannedMedicineEntity ->
                if (plannedMedicineEntity.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
                    plannedMedicineEntity.setMedicineTaken(false)
                } else {
                    plannedMedicineEntity.setMedicineTaken(true)
                }
                AppRepository.updatePlannedMedicine(plannedMedicineEntity)
            }
        }
    }
}