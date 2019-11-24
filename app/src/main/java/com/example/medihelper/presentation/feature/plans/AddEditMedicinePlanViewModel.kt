package com.example.medihelper.presentation.feature.plans

import androidx.lifecycle.*
import com.example.medihelper.domain.entities.*
import com.example.medihelper.domain.usecases.DateTimeUseCases
import com.example.medihelper.domain.usecases.MedicinePlanUseCases
import com.example.medihelper.domain.usecases.MedicineUseCases
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.presentation.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditMedicinePlanViewModel(
    private val personUseCases: PersonUseCases,
    private val medicineUseCases: MedicineUseCases,
    private val medicinePlanUseCases: MedicinePlanUseCases,
    private val dateTimeUseCases: DateTimeUseCases
) : ViewModel() {

    val colorPrimaryId: LiveData<Int>
    val selectedPersonName: LiveData<String>
    val selectedMedicineId = MutableLiveData<Int>()
    val selectedMedicineAvailable: LiveData<Boolean>
    val selectedMedicineShortInfo: LiveData<MedicineShortInfo>
    val durationType = MutableLiveData<DurationType>()
    val startDate = MutableLiveData<AppDate>()
    val endDate = MutableLiveData<AppDate>()
    val daysType = MutableLiveData<DaysType>()
    val daysOfWeek = MutableLiveData<DaysOfWeekFormItem>()
    val intervalOfDays = MutableLiveData<Int>()

    val formTitle: LiveData<String>
        get() = _formTitle
    val timeDoseList: LiveData<MutableList<TimeDoseFormItem>>
        get() = _timeDoseFormItemList
    val errorGlobal: LiveData<String>
        get() = _errorGlobal
    val errorStartDate: LiveData<String>
        get() = _errorStartDate
    val errorEndDate: LiveData<String>
        get() = _errorEndDate

    private val _formTitle = MutableLiveData<String>("Zaplanuj lek")
    private val _timeDoseFormItemList = MutableLiveData<MutableList<TimeDoseFormItem>>()
    private val _errorGlobal = MutableLiveData<String>()
    private val _errorStartDate = MutableLiveData<String>()
    private val _errorEndDate = MutableLiveData<String>()

    private val selectedPerson: LiveData<Person> = personUseCases.getCurrPersonLive()
    private val selectedMedicine: LiveData<Medicine>
    private var editMedicinePlanId: Int? = null

    init {
        colorPrimaryId = Transformations.map(selectedPerson) { it.colorId }
        selectedMedicine = Transformations.switchMap(selectedMedicineId) { medicineId ->
            medicineId?.let { medicineUseCases.getMedicineLiveById(it) }
        }
        selectedMedicineAvailable = Transformations.map(selectedMedicine) { it != null }
        selectedMedicineShortInfo = Transformations.map(selectedMedicine) { MedicineShortInfo(it) }
        selectedPersonName = Transformations.map(selectedPerson) { it.name }
    }

    fun setArgs(args: AddEditMedicinePlanFragmentArgs) = viewModelScope.launch {
        if (args.editMedicinePlanID != -1) {
            editMedicinePlanId = args.editMedicinePlanID
            _formTitle.postValue("Edytuj plan")
            val editMedicinePlan = medicinePlanUseCases.getMedicinePlanById(args.editMedicinePlanID)
            setMedicinePlanData(editMedicinePlan)
        } else {
            setEmptyMedicinePlanData()
        }
    }

    fun refreshTimeDoseList(medicineUnit: String) {
        val currList = _timeDoseFormItemList.value
        val refreshedList = currList?.map {
            TimeDoseFormItem(
                time = it.time,
                doseSize = it.doseSize,
                medicineUnit = medicineUnit
            )
        }
        _timeDoseFormItemList.value = refreshedList?.toMutableList()
    }

    fun addTimeOfTaking() {
        _timeDoseFormItemList.value?.let { timeOfTakingList ->
            timeOfTakingList.add(
                TimeDoseFormItem(
                    time = AppTime(8, 0),
                    doseSize = 1f,
                    medicineUnit = selectedMedicineShortInfo.value?.unit ?: "--"
                )
            )
            _timeDoseFormItemList.value = _timeDoseFormItemList.value
        }
    }

    fun removeTimeOfTaking(timeDoseFormItem: TimeDoseFormItem) {
        _timeDoseFormItemList.value?.let { doseHourList ->
            doseHourList.remove(timeDoseFormItem)
            _timeDoseFormItemList.value = _timeDoseFormItemList.value
        }
    }

    fun updateTimeOfTaking(position: Int, timeDoseFormItem: TimeDoseFormItem) {
        _timeDoseFormItemList.value?.let { doseHourList ->
            doseHourList[position] = timeDoseFormItem
            _timeDoseFormItemList.value = _timeDoseFormItemList.value
        }
    }

    fun saveMedicinePlan(): Boolean {
        if (isFormValid()) {
            val medicinePlanId = editMedicinePlanId
            val inputData = MedicinePlanInputData(
                medicineId = selectedMedicineId.value!!,
                personId = selectedPerson.value?.personId!!,
                durationType = durationType.value!!,
                startDate = startDate.value!!,
                endDate = endDate.value,
                daysType = daysType.value,
                daysOfWeek = daysOfWeek.value?.toDaysOfWeek(),
                intervalOfDays = intervalOfDays.value,
                timeDoseList = _timeDoseFormItemList.value!!.map { it.toTimeDose() }
            )
            if (medicinePlanId == null) {
                GlobalScope.launch {
                    medicinePlanUseCases.addNewMedicinePlan(inputData)
                }
            } else {
                GlobalScope.launch {
                    medicinePlanUseCases.updateMedicinePlan(medicinePlanId, inputData)
                }
            }
            return true
        }
        return false
    }

    private fun isFormValid(): Boolean {
        val medicineId = selectedMedicineId.value
        val durationType = durationType.value
        val startDate = startDate.value
        val endDate = endDate.value
        val daysType = daysType.value
        val daysOfWeek = daysOfWeek.value

        val medicineError = if (medicineId == null) {
            "Nie wybrano leku"
        } else null
        val startDateError = if (startDate == null) {
            "Pole jest wymagane"
        } else null
        val endDateError = if (durationType == DurationType.PERIOD && endDate == null) {
            "Pole jest wymagane"
        } else null
        val daysOfWeekError = if (
            daysType == DaysType.DAYS_OF_WEEK &&
            (daysOfWeek == null || !daysOfWeek.isAnySelected())
        ) {
            "Nie wybrano dni tygodnia"
        } else null
        val datesOrderError = if (
            startDate != null &&
            endDate != null &&
            startDate > endDate
        ) {
            "Zła kolejność dat"
        } else null

        _errorGlobal.postValue(medicineError)
        _errorStartDate.postValue(startDateError)
        _errorEndDate.postValue(endDateError)
        if (medicineError == null) {
            _errorGlobal.postValue(daysOfWeekError)
        }
        if (startDateError == null && endDateError == null) {
            _errorStartDate.postValue(datesOrderError)
            _errorEndDate.postValue(datesOrderError)
        }

        return arrayOf(medicineError, startDateError, endDateError, daysOfWeekError, datesOrderError).all { it == null }
    }

    private fun setMedicinePlanData(medicinePlan: MedicinePlan) {
        selectedMedicineId.postValue(medicinePlan.medicineId)
        durationType.postValue(medicinePlan.durationType)
        startDate.postValue(medicinePlan.startDate)
        endDate.postValue(medicinePlan.endDate)
        daysType.postValue(medicinePlan.daysType)
        daysOfWeek.postValue(medicinePlan.daysOfWeek?.let { DaysOfWeekFormItem(it) })
        intervalOfDays.postValue(medicinePlan.intervalOfDays)
        _timeDoseFormItemList.postValue(medicinePlan.timeDoseList.map {
            TimeDoseFormItem(it, selectedMedicine.value?.unit)
        }.toMutableList())
    }

    private fun setEmptyMedicinePlanData() {
        selectedMedicineId.postValue(null)
        durationType.postValue(DurationType.ONCE)
        startDate.postValue(dateTimeUseCases.getCurrDate())
        endDate.postValue(null)
        daysType.postValue(null)
        daysOfWeek.postValue(DaysOfWeekFormItem())
        intervalOfDays.postValue(1)
        _timeDoseFormItemList.postValue(
            mutableListOf(
                TimeDoseFormItem(
                    time = AppTime(8, 0),
                    doseSize = 1f,
                    medicineUnit = selectedMedicine.value?.unit ?: "--"
                )
            )
        )
    }
}