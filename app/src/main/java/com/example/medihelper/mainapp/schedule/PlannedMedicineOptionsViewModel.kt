package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.MedicineType
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import java.io.File
import java.sql.Time
import java.util.*

class PlannedMedicineOptionsViewModel : ViewModel() {
    private val TAG = PlannedMedicineOptionsViewModel::class.simpleName

    private val plannedMedicineIdLive = MutableLiveData<Int>()
    private val plannedMedicineLive: LiveData<PlannedMedicine>
    private val medicinePlanLive: LiveData<MedicinePlan>
    private val medicineLive: LiveData<Medicine>
    private val medicineTypeLive: LiveData<MedicineType>

    val medicineNameLive: LiveData<String>
    val statusOfTakingLive: LiveData<String>
    val statusOfTakingColorIdLive: LiveData<Int>
    val plannedDateLive: LiveData<String>
    val plannedTimeLive: LiveData<String>
    val dozeSizeLive: LiveData<String>
    val medicineTypeNameLive: LiveData<String>
    val medicineImageFileLive: LiveData<File>

    val takeMedicineBtnText: LiveData<String>
    val takeMedicineBtnIcon: LiveData<Int>

    init {
        plannedMedicineLive = Transformations.switchMap(plannedMedicineIdLive) { plannedMedicineId ->
            AppRepository.getPlannedMedicineByIdLive(plannedMedicineId)
        }
        medicinePlanLive = Transformations.switchMap(plannedMedicineLive) { plannedMedicine ->
            AppRepository.getMedicinePlanByIdLive(plannedMedicine.medicinePlanID)
        }
        medicineLive = Transformations.switchMap(medicinePlanLive) { medicinePlan ->
            AppRepository.getMedicineByIdLive(medicinePlan.medicineID)
        }
        medicineTypeLive = Transformations.switchMap(medicineLive) { medicine ->
            medicine.medicineTypeID?.let { AppRepository.getMedicineTypeByIdLive(it) }
        }

        medicineNameLive = Transformations.map(medicineLive) { medicine ->
            medicine.name
        }
        statusOfTakingLive = Transformations.map(plannedMedicineLive) { plannedMedicine ->
            when (plannedMedicine.statusOfTaking) {
                PlannedMedicine.StatusOfTaking.WAITING -> "Oczekujacy na przyjęcie"
                PlannedMedicine.StatusOfTaking.TAKEN -> "Przyjęty o godzinie 00:00"
                PlannedMedicine.StatusOfTaking.NOT_TAKEN -> "Nieprzyjety o zaplanowanej godzinie"
            }
        }
        statusOfTakingColorIdLive = Transformations.map(plannedMedicineLive) { plannedMedicine ->
            when (plannedMedicine.statusOfTaking) {
                PlannedMedicine.StatusOfTaking.WAITING -> R.color.colorDarkerGray
                PlannedMedicine.StatusOfTaking.TAKEN -> R.color.colorStateGood
                PlannedMedicine.StatusOfTaking.NOT_TAKEN -> R.color.colorStateSmall
            }
        }
        plannedDateLive = Transformations.map(plannedMedicineLive) { plannedMedicine ->
            plannedMedicine?.let { AppDateTimeUtil.dateToString(it.plannedDate) }
        }
        plannedTimeLive = Transformations.map(plannedMedicineLive) { plannedMedicine ->
            plannedMedicine?.let { AppDateTimeUtil.timeToString(it.plannedTime) }
        }
        dozeSizeLive = Transformations.map(plannedMedicineLive) { plannedMedicine ->
            plannedMedicine.plannedDoseSize.toString()
        }
        medicineTypeNameLive = Transformations.map(medicineTypeLive) { medicineType ->
            medicineType.typeName
        }
        medicineImageFileLive = Transformations.map(medicineLive) { medicine ->
            medicine?.photoFilePath?.let { File(it) }
        }
        takeMedicineBtnText = Transformations.map(plannedMedicineLive) { plannedMedicine ->
            if (plannedMedicine.statusOfTaking == PlannedMedicine.StatusOfTaking.TAKEN) {
                "Anuluj przyjecie leku"
            } else {
                "Przyjmij lek"
            }
        }
        takeMedicineBtnIcon = Transformations.map(plannedMedicineLive) { plannedMedicine ->
            if (plannedMedicine.statusOfTaking == PlannedMedicine.StatusOfTaking.TAKEN) {
                R.drawable.round_close_black_24
            } else {
                R.drawable.baseline_check_white_24
            }
        }
    }

    fun setPlannedMedicineId(plannedMedicineId: Int?) {
        plannedMedicineIdLive.value = plannedMedicineId
    }

    fun changePlannedMedicineStatus() {
        plannedMedicineLive.value?.let { plannedMedicine ->
            if (plannedMedicine.statusOfTaking == PlannedMedicine.StatusOfTaking.TAKEN) {
                plannedMedicine.setMedicineTaken(false)
            } else {
                plannedMedicine.setMedicineTaken(true)
            }
            AppRepository.updatePlannedMedicine(plannedMedicine)
        }
    }


}