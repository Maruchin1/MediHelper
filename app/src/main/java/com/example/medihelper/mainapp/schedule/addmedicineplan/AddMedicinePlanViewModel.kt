package com.example.medihelper.mainapp.schedule.addmedicineplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTime
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import java.util.*
import kotlin.collections.ArrayList

class AddMedicinePlanViewModel : ViewModel() {
    private val TAG = AddMedicinePlanViewModel::class.simpleName

    val selectedPersonItemLive = AppRepository.getSelectedPersonItemLive()
    val colorPrimaryLive: LiveData<Int>

    val selectedMedicineIDLive = MutableLiveData<Int>()
    val selectedMedicineName: LiveData<String>
    val selectedMedicineCurrState: LiveData<String>
    val selectedMedicineExpireDate: LiveData<String>

    val durationTypeLive = MutableLiveData<MedicinePlanEntity.DurationType>()
    val startDateLive = MutableLiveData<Date>()
    val endDateLive = MutableLiveData<Date>()

    val daysTypeLive = MutableLiveData<MedicinePlanEntity.DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<MedicinePlanEntity.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val timeOfTakingListLive = MutableLiveData<ArrayList<MedicinePlanEntity.TimeOfTaking>>()

    private val selectedMedicineDetailsLive: LiveData<MedicineDetails>

    init {
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
        selectedMedicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            AppRepository.getMedicineDetailsLive(medicineID)
        }
        selectedMedicineName = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            medicineDetails.medicineName
        }
        selectedMedicineCurrState = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            "${medicineDetails.currState}/${medicineDetails.packageSize} ${medicineDetails.medicineUnit}"
        }
        selectedMedicineExpireDate = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            medicineDetails.expireDate?.let { AppDateTime.dateToString(it) }
        }
        loadDefaultData()
    }

    fun saveMedicinePlan() {
        //todo zrobić to porządniej i z walidacją danych
        val medicinePlan = MedicinePlanEntity(
            medicineID = selectedMedicineDetailsLive.value!!.medicineID,
            personID = selectedPersonItemLive.value!!.personID,
            startDate = startDateLive.value!!,
            durationType = durationTypeLive.value!!,
            daysType = daysTypeLive.value!!,
            timeOfTakingList = timeOfTakingListLive.value!!.toList()
        )
        if (durationTypeLive.value == MedicinePlanEntity.DurationType.PERIOD) {
            medicinePlan.endDate = endDateLive.value
        }
        when (daysTypeLive.value) {
            MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> medicinePlan.daysOfWeek = daysOfWeekLive.value
            MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> medicinePlan.intervalOfDays = intervalOfDaysLive.value
        }
        AppRepository.insertMedicinePlan(medicinePlan)
    }

    fun addDoseHour() {
        timeOfTakingListLive.value?.let { doseHourList ->
            doseHourList.add(MedicinePlanEntity.TimeOfTaking())
            timeOfTakingListLive.value = timeOfTakingListLive.value
        }
    }

    fun removeTimeOfTaking(timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        timeOfTakingListLive.value?.let { doseHourList ->
            doseHourList.remove(timeOfTaking)
            timeOfTakingListLive.value = timeOfTakingListLive.value
        }
    }

    fun updateTimeOfTaking(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        timeOfTakingListLive.value?.let { doseHourList ->
            doseHourList[position] = timeOfTaking
            timeOfTakingListLive.value = timeOfTakingListLive.value
        }
    }

    fun getTimeOfTakingDisplayData(timeOfTaking: MedicinePlanEntity.TimeOfTaking): TimeOfTakingDisplayData {
        return TimeOfTakingDisplayData(
            timeOfTakingRef = timeOfTaking,
            time = AppDateTime.timeToString(timeOfTaking.time),
            doseSize = timeOfTaking.doseSize.toString(),
            medicineTypeName = selectedMedicineDetailsLive.value?.medicineUnit ?: "--"
        )
    }

    private fun loadDefaultData() {
        durationTypeLive.value = MedicinePlanEntity.DurationType.ONCE
        startDateLive.value = AppDateTime.getCurrCalendar().time
        endDateLive.value = null
        daysTypeLive.value = MedicinePlanEntity.DaysType.NONE
        daysOfWeekLive.value = MedicinePlanEntity.DaysOfWeek()
        intervalOfDaysLive.value = 1
        timeOfTakingListLive.value = arrayListOf(MedicinePlanEntity.TimeOfTaking())
    }

    data class TimeOfTakingDisplayData(
        val timeOfTakingRef: MedicinePlanEntity.TimeOfTaking,
        val time: String,
        val doseSize: String,
        val medicineTypeName: String
    )
}