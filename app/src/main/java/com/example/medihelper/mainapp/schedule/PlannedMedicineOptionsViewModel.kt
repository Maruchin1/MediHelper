package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.AppDateTime
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import kotlinx.coroutines.launch
import java.io.File

class PlannedMedicineOptionsViewModel(
    private val plannedMedicineRepository: PlannedMedicineRepository,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    val medicineNameLive: LiveData<String>
    val medicineUnitLive: LiveData<String>
    val statusOfTakingLive: LiveData<String>
    val statusOfTakingColorIdLive: LiveData<Int>
    val plannedDateLive: LiveData<String>
    val plannedTimeLive: LiveData<String>
    val doseSizeLive: LiveData<String>
    val medicineImageFileLive: LiveData<File>
    val takeMedicineBtnText: LiveData<String>
    val takeMedicineBtnIcon: LiveData<Int>

    private val plannedMedicineIDLive = MutableLiveData<Int>()
    private val plannedMedicineDetailsLive: LiveData<PlannedMedicineDetails>

    init {
        plannedMedicineDetailsLive = Transformations.switchMap(plannedMedicineIDLive) { plannedMedicineID ->
            plannedMedicineRepository.getDetailsLive(plannedMedicineID)
        }

        medicineNameLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            plannedMedicine.medicineName
        }
        statusOfTakingLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            plannedMedicine.statusOfTaking.shortString
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
        doseSizeLive = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
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
        viewModelScope.launch {

        }
    }

    fun setArgs(args: PlannedMedicineOptionsDialogArgs) {
        plannedMedicineIDLive.value = args.plannedMedicineID
    }

    fun changePlannedMedicineStatus() = viewModelScope.launch {
        plannedMedicineDetailsLive.value?.let { plannedMedicineDetails ->
            val plannedMedicineEntity = plannedMedicineRepository.getEntity(plannedMedicineDetails.plannedMedicineID)
            val medicineEntity = medicineRepository.getEntity(plannedMedicineDetails.medicineID)

            if (plannedMedicineDetails.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
                plannedMedicineEntity.setMedicineTaken(false)
                medicineEntity.increaseCurrState(plannedMedicineDetails.plannedDoseSize)
            } else {
                plannedMedicineEntity.setMedicineTaken(true)
                medicineEntity.reduceCurrState(plannedMedicineDetails.plannedDoseSize)
            }

            plannedMedicineRepository.update(plannedMedicineEntity)
            medicineRepository.update(medicineEntity)
        }
    }
}