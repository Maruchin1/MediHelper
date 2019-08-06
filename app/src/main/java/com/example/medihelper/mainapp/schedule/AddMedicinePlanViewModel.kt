package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.MedicineType
import java.util.*
import kotlin.collections.ArrayList

class AddMedicinePlanViewModel : ViewModel() {
    private val TAG = AddMedicinePlanViewModel::class.simpleName

    val medicineListLive = AppRepository.getMedicineListLive()
    val medicineTypeListLive = AppRepository.getMedicineTypeListLive()

    val selectedMedicineLive = MutableLiveData<Medicine>()
    val selectedMedicineNameLive: LiveData<String>
    val selectedMedicineTypeLive: LiveData<MedicineType>
    val selectedMedicineStateLive: LiveData<String>

    val durationTypeLive = MutableLiveData<MedicinePlan.DurationType>()
    val startDateLive = MutableLiveData<Date>()
    val endDateLive = MutableLiveData<Date>()

    val daysTypeLive = MutableLiveData<MedicinePlan.DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<MedicinePlan.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val doseHourListLive = MutableLiveData<ArrayList<MedicinePlan.TimeOfTaking>>()

    private val selectedMedicineObserver = Observer<Medicine> { resetViewModel() }

    init {
        selectedMedicineNameLive = Transformations.map(selectedMedicineLive) { medicine ->
            medicine?.name
        }
        selectedMedicineTypeLive = Transformations.map(selectedMedicineLive) { medicine ->
            medicine?.medicineTypeID?.let { medicineTypeID ->
                findMedicineType(medicineTypeID)
            }
        }
        selectedMedicineStateLive = Transformations.map(selectedMedicineLive) { medicine ->
            "${medicine?.currState}/${medicine?.packageSize}"
        }
        selectedMedicineLive.observeForever(selectedMedicineObserver)
    }

    override fun onCleared() {
        super.onCleared()
        selectedMedicineLive.removeObserver(selectedMedicineObserver)
    }

    fun saveScheduledMedicine() {
        //todo zrobić to porządniej i z walidacją danych
        val medicinePlan = MedicinePlan(
            medicineID = selectedMedicineLive.value!!.medicineID,
            startDate = startDateLive.value!!,
            durationType = durationTypeLive.value!!,
            daysType = daysTypeLive.value!!,
            timeOfTakingList = doseHourListLive.value!!.toList()
        )
        if (durationTypeLive.value == MedicinePlan.DurationType.PERIOD) {
            medicinePlan.endDate = endDateLive.value
        }
        when (daysTypeLive.value) {
            MedicinePlan.DaysType.DAYS_OF_WEEK -> medicinePlan.daysOfWeek = daysOfWeekLive.value
            MedicinePlan.DaysType.INTERVAL_OF_DAYS -> medicinePlan.intervalOfDays = intervalOfDaysLive.value
        }
        AppRepository.insertMedicinePlan(medicinePlan)
    }

    fun addDoseHour() {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList.add(MedicinePlan.TimeOfTaking())
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun removeDoseHour(timeOfTaking: MedicinePlan.TimeOfTaking) {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList.remove(timeOfTaking)
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun updateDoseHour(position: Int, timeOfTaking: MedicinePlan.TimeOfTaking) {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList[position] = timeOfTaking
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun getTimeOfTakingDisplayData(timeOfTaking: MedicinePlan.TimeOfTaking): TimeOfTakingDisplayData {
        return TimeOfTakingDisplayData(
            time = AppDateTimeUtil.timeToString(timeOfTaking.time),
            doseSize = timeOfTaking.doseSize.toString(),
            medicineTypeName = selectedMedicineTypeLive.value?.typeName ?: "--"
        )
    }

    fun getMedicineDisplayData(medicine: Medicine): MedicineDisplayData {
        val medicineType = medicineTypeListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicine.medicineTypeID
        }
        return MedicineDisplayData(
            medicineName = medicine.name,
            medicineState = "${medicine.currState}/${medicine.packageSize} ${medicineType?.typeName ?: "--"}"
        )
    }

    private fun resetViewModel() {
        arrayOf(
            durationTypeLive,
            startDateLive,
            endDateLive,
            daysTypeLive
        ).forEach { field ->
            field.value = null
        }
        daysOfWeekLive.value = MedicinePlan.DaysOfWeek()
        intervalOfDaysLive.value = 0
        doseHourListLive.value = arrayListOf(MedicinePlan.TimeOfTaking())
    }

    private fun findMedicineType(medicineTypeID: Int): MedicineType? {
        return medicineTypeListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }
    }

    data class TimeOfTakingDisplayData(
        val time: String,
        val doseSize: String,
        val medicineTypeName: String
    )

    data class MedicineDisplayData(
        val medicineName: String,
        val medicineState: String
    )
}