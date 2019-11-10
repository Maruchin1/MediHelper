package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.custom.AppDate
import com.example.medihelper.custom.AppTime
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
        plannedMedicineDetailsLive.value?.let { details ->
            if (details.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
                plannedMedicineService.changeMedicineTaken(details.plannedMedicineID, false)
                medicineService.increaseCurrState(details.medicineID, details.plannedDoseSize)
            } else {
                plannedMedicineService.changeMedicineTaken(details.plannedMedicineID, true)
                medicineService.reduceCurrState(details.medicineID, details.plannedDoseSize)
            }
        }
    }

//    fun setAlarm() {
//        val plannedMedicineId = plannedMedicineIDLive.value
//        val plannedMedicineDetails = plannedMedicineDetailsLive.value
//        if (plannedMedicineId != null && plannedMedicineDetails != null) {
//            alarmService.setPlannedMedicineAlarm(
//                plannedMedicineId =  plannedMedicineId,
//                date =  dateTimeService.getCurrDate(),
//                time =  AppTime(Date().time + 2000)
//            )
//        }
//    }
}