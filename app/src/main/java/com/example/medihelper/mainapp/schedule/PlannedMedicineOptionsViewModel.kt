package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.*
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.R
import com.example.medihelper.localdata.pojo.PlannedMedicineDetails
import com.example.medihelper.domain.entities.StatusOfTaking
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
    val statusOfTakingLive: LiveData<StatusOfTaking>
    val plannedDateLive: LiveData<AppDate>
    val plannedTimeLive: LiveData<AppTime>
    val doseSizeLive: LiveData<String>
    val takeMedicineBtnText: LiveData<String>
    val takeMedicineBtnIcon: LiveData<Int>

    val medicineID: Int?
        get() = plannedMedicineDetailsLive.value?.medicineId

    private val plannedMedicineIDLive = MutableLiveData<Int>()
    private val plannedMedicineDetailsLive: LiveData<PlannedMedicineDetails>

    init {
        colorPrimaryLive = Transformations.map(personService.getCurrPersonItemLive()) { it.personColorResId }
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
            if (plannedMedicine.statusOfTaking == StatusOfTaking.TAKEN) {
                "Anuluj przyjecie leku"
            } else {
                "Przyjmij lek"
            }
        }
        takeMedicineBtnIcon = Transformations.map(plannedMedicineDetailsLive) { plannedMedicine ->
            if (plannedMedicine.statusOfTaking == StatusOfTaking.TAKEN) {
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
            if (details.statusOfTaking == StatusOfTaking.TAKEN) {
                plannedMedicineService.changeMedicineTaken(details.plannedMedicineId, false)
                medicineService.increaseCurrState(details.medicineId, details.plannedDoseSize)
            } else {
                plannedMedicineService.changeMedicineTaken(details.plannedMedicineId, true)
                medicineService.reduceCurrState(details.medicineId, details.plannedDoseSize)
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