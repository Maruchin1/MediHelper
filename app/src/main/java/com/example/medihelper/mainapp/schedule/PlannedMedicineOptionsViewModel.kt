package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.services.AlarmService
import com.example.medihelper.services.DateTimeService
import com.example.medihelper.services.PersonProfileService
import com.example.medihelper.services.StatusOfTakingService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class PlannedMedicineOptionsViewModel(
    private val plannedMedicineRepository: PlannedMedicineRepository,
    private val medicineRepository: MedicineRepository,
    private val personProfileService: PersonProfileService,
    private val alarmService: AlarmService,
    private val statusOfTakingService: StatusOfTakingService,
    private val dateTimeService: DateTimeService
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val medicineNameLive: LiveData<String>
    val medicineUnitLive: LiveData<String>
    val statusOfTakingLive: LiveData<PlannedMedicineEntity.StatusOfTaking>
    val plannedDateLive: LiveData<AppDate>
    val plannedTimeLive: LiveData<AppTime>
    val doseSizeLive: LiveData<String>
    val takeMedicineBtnText: LiveData<String>
    val takeMedicineBtnIcon: LiveData<Int>

    val medicineID: Int?
        get() = plannedMedicineDetailsLive.value?.medicineID

    private val plannedMedicineIDLive = MutableLiveData<Int>()
    private val plannedMedicineDetailsLive: LiveData<PlannedMedicineDetails>

    init {
        colorPrimaryLive = Transformations.map(personProfileService.getCurrPersonItemLive()) { it.personColorResID }
        plannedMedicineDetailsLive = Transformations.switchMap(plannedMedicineIDLive) { plannedMedicineID ->
            plannedMedicineRepository.getDetailsLive(plannedMedicineID)
        }

        medicineNameLive = Transformations.map(plannedMedicineDetailsLive) { it.medicineName }
        statusOfTakingLive = Transformations.map(plannedMedicineDetailsLive) { it.statusOfTaking }
        plannedDateLive = Transformations.map(plannedMedicineDetailsLive) { it?.plannedDate }
        plannedTimeLive = Transformations.map(plannedMedicineDetailsLive) { it?.plannedTime }
        doseSizeLive = Transformations.map(plannedMedicineDetailsLive) { it.plannedDoseSize.toString() }
        medicineUnitLive = Transformations.map(plannedMedicineDetailsLive) { it.medicineUnit }
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

    fun setArgs(args: PlannedMedicineOptionsDialogArgs) {
        plannedMedicineIDLive.value = args.plannedMedicineID
    }

    fun changePlannedMedicineStatus() = GlobalScope.launch {
        plannedMedicineDetailsLive.value?.let { plannedMedicineDetails ->
            val plannedMedicineEntity = plannedMedicineRepository.getEntity(plannedMedicineDetails.plannedMedicineID)
            val medicineEntity = medicineRepository.getEntity(plannedMedicineDetails.medicineID)

            if (plannedMedicineEntity.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
                plannedMedicineEntity.statusOfTaking = statusOfTakingService.getStatusByTaken(plannedMedicineEntity, false)
                medicineEntity.increaseCurrState(plannedMedicineDetails.plannedDoseSize)
            } else {
                plannedMedicineEntity.statusOfTaking = statusOfTakingService.getStatusByTaken(plannedMedicineEntity, true)
                medicineEntity.reduceCurrState(plannedMedicineDetails.plannedDoseSize)
            }

            plannedMedicineRepository.update(plannedMedicineEntity)
            medicineRepository.update(medicineEntity)
        }
    }

    fun setAlarm() {
        val plannedMedicineID = plannedMedicineIDLive.value
        val plannedMedicineDetails = plannedMedicineDetailsLive.value
        if (plannedMedicineID != null && plannedMedicineDetails != null) {
            alarmService.setPlannedMedicineAlarm(
                plannedMedicineID =  plannedMedicineID,
                date =  dateTimeService.getCurrDate(),
                time =  AppTime(Date().time + 2000)
            )
        }
    }
}