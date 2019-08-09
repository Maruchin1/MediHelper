package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicineKitItem
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddMedicinePlanViewModel : ViewModel() {
    private val TAG = AddMedicinePlanViewModel::class.simpleName

    val medicineKitItemListLive = AppRepository.getMedicineKitItemListLive()

    val selectedMedicineIDLive = MutableLiveData<Int>()
    val medicineKitItemLive: LiveData<MedicineKitItem>

    val durationTypeLive = MutableLiveData<MedicinePlanEntity.DurationType>()
    val startDateLive = MutableLiveData<Date>()
    val endDateLive = MutableLiveData<Date>()

    val daysTypeLive = MutableLiveData<MedicinePlanEntity.DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<MedicinePlanEntity.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val doseHourListLive = MutableLiveData<ArrayList<MedicinePlanEntity.TimeOfTaking>>()

    private val selectedMedicineIDObserver = Observer<Int> { resetViewModel() }

    init {
        medicineKitItemLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            AppRepository.getMedicineKitItemLive(medicineID)
        }
        selectedMedicineIDLive.observeForever(selectedMedicineIDObserver)
    }

    override fun onCleared() {
        super.onCleared()
        selectedMedicineIDLive.removeObserver(selectedMedicineIDObserver)
    }

    fun saveScheduledMedicine() {
        //todo zrobić to porządniej i z walidacją danych
        val medicinePlan = MedicinePlanEntity(
            medicineID = medicineKitItemLive.value!!.medicineID,
            startDate = startDateLive.value!!,
            durationType = durationTypeLive.value!!,
            daysType = daysTypeLive.value!!,
            timeOfTakingList = doseHourListLive.value!!.toList()
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
        doseHourListLive.value?.let { doseHourList ->
            doseHourList.add(MedicinePlanEntity.TimeOfTaking())
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun removeTimeOfTaking(timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList.remove(timeOfTaking)
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun updateTimeOfTaking(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList[position] = timeOfTaking
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun getTimeOfTakingDisplayData(timeOfTaking: MedicinePlanEntity.TimeOfTaking): TimeOfTakingDisplayData {
        return TimeOfTakingDisplayData(
            timeOfTakingRef = timeOfTaking,
            time = AppDateTimeUtil.timeToString(timeOfTaking.time),
            doseSize = timeOfTaking.doseSize.toString(),
            medicineTypeName = medicineKitItemLive.value?.typeName ?: "--"
        )
    }

    fun getMedicineDisplayData(medicineKitItem: MedicineKitItem): MedicineDisplayData {
        return MedicineDisplayData(
            medicineID = medicineKitItem.medicineID,
            medicineName = medicineKitItem.medicineName,
            medicineState = "${medicineKitItem.currState}/${medicineKitItem.packageSize} ${medicineKitItem.typeName ?: "--"}",
            medicineImageFile = medicineKitItem.photoFilePath?.let { File(it) }
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
        daysOfWeekLive.value = MedicinePlanEntity.DaysOfWeek()
        intervalOfDaysLive.value = 0
        doseHourListLive.value = arrayListOf(MedicinePlanEntity.TimeOfTaking())
    }

    data class TimeOfTakingDisplayData(
        val timeOfTakingRef: MedicinePlanEntity.TimeOfTaking,
        val time: String,
        val doseSize: String,
        val medicineTypeName: String
    )

    data class MedicineDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val medicineState: String,
        val medicineImageFile: File?
    )
}