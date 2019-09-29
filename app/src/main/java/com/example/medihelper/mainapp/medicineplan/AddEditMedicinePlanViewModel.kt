package com.example.medihelper.mainapp.medicineplan

import android.util.Log
import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.services.MedicineSchedulerService
import com.example.medihelper.services.PersonProfileService
import kotlinx.coroutines.*


class AddEditMedicinePlanViewModel(
    private val medicineRepository: MedicineRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val medicineSchedulerService: MedicineSchedulerService,
    private val personProfileService: PersonProfileService
) : ViewModel() {
    private val TAG = AddEditMedicinePlanViewModel::class.simpleName

    val selectedPersonItemLive = personProfileService.getCurrPersonItemLive()
    val colorPrimaryLive: LiveData<Int>

    val selectedMedicineIDLive = MutableLiveData<Int>()
    val selectedMedicineAvailableLive: LiveData<Boolean>
    val selectedMedicineName: LiveData<String>
    val selectedMedicineExpireDate: LiveData<String>
    val selectedMedicineUnitLive: LiveData<String>

    val durationTypeLive = MutableLiveData<MedicinePlanEntity.DurationType>()
    val startDateLive = MutableLiveData<AppDate>()
    val endDateLive = MutableLiveData<AppDate>()

    val daysTypeLive = MutableLiveData<MedicinePlanEntity.DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<MedicinePlanEntity.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val timeOfTakingListLive = MutableLiveData<MutableList<MedicinePlanEntity.TimeOfTaking>>()

    val errorMessageLive = MutableLiveData<String>()
    val errorStartDateLive = MutableLiveData<String>()
    val errorEndDateLive = MutableLiveData<String>()

    private val selectedMedicineDetailsLive: LiveData<MedicineDetails>
    private var editMedicinePlanID: Int? = null

    init {
        Log.i(TAG, "init")
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
        selectedMedicineAvailableLive = Transformations.map(selectedMedicineIDLive) { medicineID ->
            medicineID != null
        }
        selectedMedicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            medicineID?.let { medicineRepository.getDetailsLive(medicineID) }
        }
        selectedMedicineName = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            medicineDetails.medicineName
        }
        selectedMedicineExpireDate = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            medicineDetails.expireDate?.formatString
        }
        selectedMedicineUnitLive = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            medicineDetails.medicineUnit
        }

        selectedMedicineIDLive.postValue(null)
        durationTypeLive.postValue(MedicinePlanEntity.DurationType.ONCE)
        startDateLive.postValue(AppDate.currDate())
        endDateLive.postValue(null)
        daysTypeLive.postValue(MedicinePlanEntity.DaysType.NONE)
        daysOfWeekLive.postValue(MedicinePlanEntity.DaysOfWeek())
        intervalOfDaysLive.postValue(1)
        timeOfTakingListLive.postValue(arrayListOf(MedicinePlanEntity.TimeOfTaking()))
    }

    fun setArgs(args: AddEditMedicinePlanFragmentArgs) = viewModelScope.launch {
        Log.d(TAG, "medicinePlanID = ${args.editMedicinePlanID}")
        if (args.editMedicinePlanID != -1) {
            editMedicinePlanID = args.editMedicinePlanID
            medicinePlanRepository.getEntity(args.editMedicinePlanID).run {
                selectedMedicineIDLive.postValue(medicineID)
                personProfileService.selectCurrPerson(personID)

                durationTypeLive.postValue(durationType)
                startDateLive.postValue(startDate)
                endDateLive.postValue(endDate)

                daysTypeLive.postValue(daysType)
                if (daysOfWeek != null) {
                    daysOfWeekLive.postValue(daysOfWeek)
                }
                if (intervalOfDays != null) {
                    intervalOfDaysLive.postValue(intervalOfDays)
                }

                timeOfTakingListLive.postValue(timeOfTakingList.toMutableList())
            }
        }
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
            time = timeOfTaking.time.formatString,
            doseSize = timeOfTaking.doseSize.toString(),
            medicineTypeName = selectedMedicineDetailsLive.value?.medicineUnit ?: "--"
        )
    }

    fun saveMedicinePlan(): Boolean {
        if (validateInputData()) {
            if (editMedicinePlanID != null) {
                GlobalScope.launch {
                    val existingMedicinePlanEntity = medicinePlanRepository.getEntity(editMedicinePlanID!!)
                    val updatedMedicinePlanEntity = existingMedicinePlanEntity.copy(
                        medicineID = selectedMedicineIDLive.value!!,
                        personID = selectedPersonItemLive.value!!.personID,
                        startDate = startDateLive.value!!,
                        durationType = durationTypeLive.value!!,
                        daysType = daysTypeLive.value!!,
                        timeOfTakingList = timeOfTakingListLive.value!!
                    )
                    if (durationTypeLive.value == MedicinePlanEntity.DurationType.PERIOD) {
                        updatedMedicinePlanEntity.endDate = endDateLive.value
                    }
                    when (daysTypeLive.value) {
                        MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> updatedMedicinePlanEntity.daysOfWeek = daysOfWeekLive.value
                        MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> updatedMedicinePlanEntity.intervalOfDays = intervalOfDaysLive.value
                    }
                    medicinePlanRepository.update(updatedMedicinePlanEntity)
                    medicineSchedulerService.updatePlannedMedicines(updatedMedicinePlanEntity.medicinePlanID)
                }
            } else {
                GlobalScope.launch {
                    val newMedicinePlanEntity = MedicinePlanEntity(
                        medicineID = selectedMedicineIDLive.value!!,
                        personID = selectedPersonItemLive.value!!.personID,
                        startDate = startDateLive.value!!,
                        durationType = durationTypeLive.value!!,
                        daysType = daysTypeLive.value!!,
                        timeOfTakingList = timeOfTakingListLive.value!!
                    )
                    if (durationTypeLive.value == MedicinePlanEntity.DurationType.PERIOD) {
                        newMedicinePlanEntity.endDate = endDateLive.value
                    }
                    when (daysTypeLive.value) {
                        MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> newMedicinePlanEntity.daysOfWeek = daysOfWeekLive.value
                        MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> newMedicinePlanEntity.intervalOfDays = intervalOfDaysLive.value
                    }
                    val insertedMedicinePlanID = medicinePlanRepository.insert(newMedicinePlanEntity)
                    medicineSchedulerService.addPlannedMedicines(insertedMedicinePlanID)
                }
            }
            return true
        }
        return false
    }

    private fun validateInputData(): Boolean {
        var inputDataValid = true
        if (selectedMedicineIDLive.value == null) {
            errorMessageLive.postValue("Nie wybrano leku")
            inputDataValid = false
        }
        if (durationTypeLive.value == MedicinePlanEntity.DurationType.PERIOD) {
            if (startDateLive.value == null) {
                errorStartDateLive.value = "Pole wymagane"
                inputDataValid = false
            } else {
                errorStartDateLive.value = null
            }
            if (endDateLive.value == null) {
                errorEndDateLive.value = "Pole wymagane"
                inputDataValid = false
            } else {
                errorEndDateLive.value = null
            }
            if (startDateLive.value != null && endDateLive.value != null) {
                if (startDateLive.value!! >= endDateLive.value!!) {
                    arrayOf(errorStartDateLive, errorEndDateLive).forEach { it.value = "Zła kolejność dat" }
                    inputDataValid = false
                } else {
                    arrayOf(errorStartDateLive, errorEndDateLive).forEach { it.value = null }
                }
            }
        }
        if (daysTypeLive.value == MedicinePlanEntity.DaysType.DAYS_OF_WEEK) {
            daysOfWeekLive.value?.run {
                val daysArray = arrayOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                if (daysArray.none { daySelected -> daySelected }) {
                    errorMessageLive.postValue("Nie wybrano dni tygodnia")
                    inputDataValid = false
                }
            }
        }
        return inputDataValid
    }

    data class TimeOfTakingDisplayData(
        val timeOfTakingRef: MedicinePlanEntity.TimeOfTaking,
        val time: String,
        val doseSize: String,
        val medicineTypeName: String
    )
}