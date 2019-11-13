package com.example.medihelper.mainapp.medicineplan

import android.util.Log
import androidx.lifecycle.*
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.localdata.pojo.MedicineDetails
import com.example.medihelper.localdata.pojo.MedicinePlanEditData
import com.example.medihelper.localdata.pojo.TimeDoseEditData
import com.example.medihelper.localdata.type.*
import com.example.medihelper.service.*
import kotlinx.coroutines.*


class AddEditMedicinePlanViewModel(
    private val personService: PersonService,
    private val medicineService: MedicineService,
    private val medicinePlanService: MedicinePlanService,
    private val dateTimeService: DateTimeService,
    private val formValidatorService: FormValidatorService
) : ViewModel() {
    private val TAG = AddEditMedicinePlanViewModel::class.simpleName

    val titleLive = MutableLiveData("Zaplanuj lek")

    val selectedPersonItemLive = personService.getCurrPersonItemLive()
    val colorPrimaryLive: LiveData<Int>

    val selectedMedicineIDLive = MutableLiveData<Int>()
    val selectedMedicineAvailableLive: LiveData<Boolean>
    val selectedMedicineNameLive: LiveData<String>
    val selectedMedicineUnitLive: LiveData<String>
    val selectedMedicineExpireDateLive: LiveData<AppExpireDate>

    val durationTypeLive = MutableLiveData<DurationType>()
    val startDateLive = MutableLiveData<AppDate>()
    val endDateLive = MutableLiveData<AppDate>()

    val daysTypeLive = MutableLiveData<DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val timeDoseListLive = MutableLiveData<MutableList<TimeDoseEditData>>()

    val errorGlobalMessageLive = MutableLiveData<String>()
    val errorStartDateLive = MutableLiveData<String>()
    val errorEndDateLive = MutableLiveData<String>()

    private val selectedMedicineDetailsLive: LiveData<MedicineDetails>
    private var editMedicinePlanID: Int? = null

    init {
        Log.i(TAG, "init")
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResId
        }
        selectedMedicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            medicineID?.let { medicineService.getDetailsLive(medicineID) }
        }
        selectedMedicineAvailableLive = Transformations.map(selectedMedicineDetailsLive) { it != null }
        selectedMedicineNameLive = Transformations.map(selectedMedicineDetailsLive) { it.medicineName }
        selectedMedicineUnitLive = Transformations.map(selectedMedicineDetailsLive) { it.medicineUnit }
        selectedMedicineExpireDateLive = Transformations.map(selectedMedicineDetailsLive) { it.expireDate }

        selectedMedicineIDLive.postValue(null)
        durationTypeLive.postValue(DurationType.ONCE)
        startDateLive.postValue(dateTimeService.getCurrDate())
        endDateLive.postValue(null)
        daysTypeLive.postValue(null)
        daysOfWeekLive.postValue(DaysOfWeek())
        intervalOfDaysLive.postValue(1)
        timeDoseListLive.postValue(arrayListOf(TimeDoseEditData()))
    }

    fun setArgs(args: AddEditMedicinePlanFragmentArgs) = viewModelScope.launch {
        Log.d(TAG, "medicinePlanId = ${args.editMedicinePlanID}")
        if (args.editMedicinePlanID != -1) {
            editMedicinePlanID = args.editMedicinePlanID
            titleLive.postValue("Edytuj plan")
            medicinePlanService.getEditData(args.editMedicinePlanID).run {
                selectedMedicineIDLive.postValue(medicineId)
                personService.selectCurrPerson(personId)

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

                timeDoseListLive.postValue(timeDoseList.toMutableList())
            }
        }
    }

    fun addTimeOfTaking() {
        timeDoseListLive.value?.let { timeOfTakingList ->
            timeOfTakingList.add(TimeDoseEditData())
            timeDoseListLive.value = timeDoseListLive.value
        }
    }

    fun removeTimeOfTaking(timeDoseEditData: TimeDoseEditData) {
        timeDoseListLive.value?.let { doseHourList ->
            doseHourList.remove(timeDoseEditData)
            timeDoseListLive.value = timeDoseListLive.value
        }
    }

    fun updateTimeOfTaking(position: Int, timeDoseEditData: TimeDoseEditData) {
        timeDoseListLive.value?.let { doseHourList ->
            doseHourList[position] = timeDoseEditData
            timeDoseListLive.value = timeDoseListLive.value
        }
    }

    fun getTimeOfTakingDisplayData(timeDoseEditData: TimeDoseEditData) = TimeDoseDisplayData(
        timeDoseRef = timeDoseEditData,
        time = timeDoseEditData.time.formatString,
        doseSize = timeDoseEditData.doseSize.toString(),
        medicineUnit = selectedMedicineDetailsLive.value?.medicineUnit ?: "--"
    )

    fun saveMedicinePlan(): Boolean {
        if (validateInputData()) {
            val editData = MedicinePlanEditData(
                medicinePlanId = editMedicinePlanID ?: 0,
                medicineId = selectedMedicineIDLive.value!!,
                personId = selectedPersonItemLive.value!!.personId,
                durationType = durationTypeLive.value!!,
                startDate = startDateLive.value!!,
                daysType = daysTypeLive.value,
                timeDoseList = timeDoseListLive.value!!
            )
            if (durationTypeLive.value == DurationType.PERIOD) {
                editData.endDate = endDateLive.value
            }
            if (durationTypeLive.value != DurationType.ONCE) {
                editData.daysType = daysTypeLive.value
                when (daysTypeLive.value) {
                    DaysType.DAYS_OF_WEEK -> editData.daysOfWeek = daysOfWeekLive.value
                    DaysType.INTERVAL_OF_DAYS -> editData.intervalOfDays = intervalOfDaysLive.value
                }
            }
            GlobalScope.launch {
                medicinePlanService.save(editData)
            }
            return true
        }
        return false
    }

    private fun validateInputData(): Boolean {
        val error = formValidatorService.isMedicinePlanValid(
            medicineId = selectedMedicineIDLive.value,
            startDate = startDateLive.value,
            endDate = endDateLive.value,
            durationType = durationTypeLive.value,
            daysType = daysTypeLive.value,
            daysOfWeek = daysOfWeekLive.value
        )
        var isValid = true
        errorGlobalMessageLive.value = if (error.emptyMedicine) {
            isValid = false
            "Nie wybrano leku"
        } else null
        errorStartDateLive.value = if (error.emptyStartDate) {
            isValid = false
            "Pole wymagane"
        } else null
        errorEndDateLive.value = if (error.emptyEndDate) {
            isValid = false
            "Pole wymagane"
        } else null
        arrayOf(errorStartDateLive, errorEndDateLive).forEach {
            it.value = if (error.incorrectDatesOrder) {
                isValid = false
                "Zła kolejnosć dat"
            } else null
        }
        errorGlobalMessageLive.value = if (error.emptyDaysOfWeek) {
            isValid = false
            "Niew wybrano dni tygodnia"
        } else null
        return isValid
    }

    data class TimeDoseDisplayData(
        val timeDoseRef: TimeDoseEditData,
        val time: String,
        val doseSize: String,
        val medicineUnit: String
    )
}