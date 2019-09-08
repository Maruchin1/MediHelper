package com.example.medihelper.mainapp.addeditmedicineplan

import android.util.Log
import androidx.lifecycle.*
import com.example.medihelper.AppDateTime
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.AppRepository
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.MedicineItem
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class AddEditMedicinePlanViewModel : ViewModel() {
    private val TAG = AddEditMedicinePlanViewModel::class.simpleName

    val selectedPersonItemLive = AppRepository.getSelectedPersonItemLive()
    val colorPrimaryLive: LiveData<Int>
    val medicineItemListLive = AppRepository.getMedicineItemListLive()

    val selectedMedicineIDLive = MutableLiveData<Int>()
    val selectedMedicineAvailableLive: LiveData<Boolean>
    val selectedMedicineName: LiveData<String>
    val selectedMedicineCurrState: LiveData<String>
    val selectedMedicineExpireDate: LiveData<String>

    val durationTypeLive = MutableLiveData<MedicinePlanEntity.DurationType>()
    val startDateLive = MutableLiveData<Date>()
    val endDateLive = MutableLiveData<Date>()

    val daysTypeLive = MutableLiveData<MedicinePlanEntity.DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<MedicinePlanEntity.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val timeOfTakingListLive = MutableLiveData<MutableList<MedicinePlanEntity.TimeOfTaking>>()

    val errorShowMessageAction = ActionLiveData<String>()
    val errorStartDateLive = MutableLiveData<String>()
    val errorEndDateLive = MutableLiveData<String>()

    private val selectedMedicineDetailsLive: LiveData<MedicineDetails>
    private var editMedicinePlanID: Int? = null

    init {
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
        selectedMedicineAvailableLive = Transformations.map(selectedMedicineIDLive) { medicineID ->
            medicineID != null
        }
        selectedMedicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            medicineID?.let { AppRepository.getMedicineDetailsLive(medicineID) }
        }
        selectedMedicineName = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            medicineDetails.medicineName
        }
        selectedMedicineCurrState = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            "${medicineDetails.currState}/${medicineDetails.packageSize} ${medicineDetails.medicineUnit}"
        }
        selectedMedicineExpireDate = Transformations.map(selectedMedicineDetailsLive) { medicineDetails ->
            AppDateTime.dateToString(medicineDetails.expireDate)
        }
    }

    fun setArgs(args: AddEditMedicinePlanFragmentArgs) = viewModelScope.launch {
        Log.d(TAG, "medicinePlanID = ${args.editMedicinePlanID}")
        if (args.editMedicinePlanID != -1) {
            editMedicinePlanID = args.editMedicinePlanID
            AppRepository.getMedicinePlanEntity(args.editMedicinePlanID).run {
                selectedMedicineIDLive.postValue(medicineID)
                AppRepository.setSelectedPerson(personID)

                durationTypeLive.postValue(durationType)
                startDateLive.postValue(startDate)
                endDateLive.postValue(endDate)

                daysTypeLive.postValue(daysType)
                daysOfWeekLive.postValue(daysOfWeek)
                intervalOfDaysLive.postValue(intervalOfDays)

                timeOfTakingListLive.postValue(timeOfTakingList.toMutableList())
            }
        } else {
            selectedMedicineIDLive.postValue(null)

            durationTypeLive.postValue(MedicinePlanEntity.DurationType.ONCE)
            startDateLive.postValue(AppDateTime.getCurrCalendar().time)
            endDateLive.postValue(null)

            daysTypeLive.postValue(MedicinePlanEntity.DaysType.NONE)
            daysOfWeekLive.postValue(MedicinePlanEntity.DaysOfWeek())
            intervalOfDaysLive.postValue(1)

            timeOfTakingListLive.postValue(arrayListOf(MedicinePlanEntity.TimeOfTaking()))
        }
    }

    fun saveMedicinePlan(): Boolean {
        if (validateInputData()) {
            val medicinePlanEntity = MedicinePlanEntity(
                medicineID = selectedMedicineIDLive.value!!,
                personID = selectedPersonItemLive.value!!.personID,
                startDate = startDateLive.value!!,
                durationType = durationTypeLive.value!!,
                daysType = daysTypeLive.value!!,
                timeOfTakingList = timeOfTakingListLive.value!!
            )
            if (durationTypeLive.value == MedicinePlanEntity.DurationType.PERIOD) {
                medicinePlanEntity.endDate = endDateLive.value
            }
            when (daysTypeLive.value) {
                MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> medicinePlanEntity.daysOfWeek = daysOfWeekLive.value
                MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> medicinePlanEntity.intervalOfDays = intervalOfDaysLive.value
            }
            viewModelScope.launch {
                if (editMedicinePlanID != null) {
                    AppRepository.updateMedicinePlan(medicinePlanEntity.copy(medicinePlanID = editMedicinePlanID!!))
                } else {
                    AppRepository.insertMedicinePlan(medicinePlanEntity)
                }
            }
            return true
        }
        return false
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

    fun getMedicineDisplayData(medicineItem: MedicineItem): MedicineItemDisplayData {
        return MedicineItemDisplayData(
            medicineID = medicineItem.medicineID,
            medicineName = medicineItem.medicineName,
            medicineState = "${medicineItem.currState}/${medicineItem.packageSize} ${medicineItem.medicineUnit}",
            medicineImageFile = medicineItem.photoFilePath?.let { File(it) }
        )
    }

    private fun validateInputData(): Boolean {
        var inputDataValid = true
        if (selectedMedicineIDLive.value == null) {
            errorShowMessageAction.sendAction("Nie wybrano leku")
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
                if (AppDateTime.compareDates(startDateLive.value!!, endDateLive.value!!) != 2) {
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
                    errorShowMessageAction.sendAction("Nie wybrano dni tygodnia")
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

    data class MedicineItemDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val medicineState: String,
        val medicineImageFile: File?
    )
}