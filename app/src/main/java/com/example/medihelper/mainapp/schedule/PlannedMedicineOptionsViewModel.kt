package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojo.PlannedMedicineDetails
import com.example.medihelper.service.MedicineService
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.PlannedMedicineService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlannedMedicineOptionsViewModel(
    private val personService: PersonService,
    private val plannedMedicineService: PlannedMedicineService,
    private val medicineService: MedicineService
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
        colorPrimaryLive = Transformations.map(personService.getCurrPersonItemLive()) { it.personColorResID }
        plannedMedicineDetailsLive = Transformations.switchMap(plannedMedicineIDLive) { plannedMedicineID ->
            plannedMedicineService.getDetailsLive(plannedMedicineID)
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
            val plannedMedicineEntity = plannedMedicineService.getEntity(plannedMedicineDetails.plannedMedicineID)
            val medicineEntity = medicineService.getEntity(plannedMedicineDetails.medicineID)

            if (plannedMedicineEntity.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
                plannedMedicineEntity.statusOfTaking = plannedMedicineService.getStatusByTaken(plannedMedicineEntity, false)
                medicineEntity.increaseCurrState(plannedMedicineDetails.plannedDoseSize)
            } else {
                plannedMedicineEntity.statusOfTaking = plannedMedicineService.getStatusByTaken(plannedMedicineEntity, true)
                medicineEntity.reduceCurrState(plannedMedicineDetails.plannedDoseSize)
            }

            plannedMedicineService.update(plannedMedicineEntity)
            medicineService.update(medicineEntity)
        }
    }

//    fun setAlarm() {
//        val plannedMedicineID = plannedMedicineIDLive.value
//        val plannedMedicineDetails = plannedMedicineDetailsLive.value
//        if (plannedMedicineID != null && plannedMedicineDetails != null) {
//            alarmService.setPlannedMedicineAlarm(
//                plannedMedicineID =  plannedMedicineID,
//                date =  dateTimeService.getCurrDate(),
//                time =  AppTime(Date().time + 2000)
//            )
//        }
//    }
}