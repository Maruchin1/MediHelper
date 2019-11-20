package com.example.medihelper.presentation.feature.plans

import androidx.lifecycle.*
import com.example.medihelper.custom.FieldMutableLiveData
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
    val selectedMedicineId = MutableLiveData<Int>()
    val selectedMedicineAvailable: LiveData<Boolean>
    val selectedMedicineShortInfo: LiveData<MedicineShortInfo>
    val selectedPersonName: LiveData<String>

    val formTitle: LiveData<String>
        get() = _formTitle
    val medicinePlanForm: MutableLiveData<MedicinePlanForm>
        get() = _medicinePlanForm
    val daysOfWeekFormItem: MutableLiveData<DaysOfWeekFormItem>
        get() = _daysOfWeekFormItem
    val timeDoseList: LiveData<MutableList<TimeDoseFormItem>>
        get() = _timeDoseFormItemList
    val medicinePlanFormError: LiveData<MedicinePlanFormError>
        get() = _medicinePlanFormError

    private val _formTitle = MutableLiveData<String>("Zaplanuj lek")
    private val _medicinePlanForm = FieldMutableLiveData<MedicinePlanForm>()
    private val _daysOfWeekFormItem = FieldMutableLiveData<DaysOfWeekFormItem>()
    private val _timeDoseFormItemList = MutableLiveData<MutableList<TimeDoseFormItem>>()
    private val _medicinePlanFormError = MutableLiveData<MedicinePlanFormError>()

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
            val editMedicinePlanForm = MedicinePlanForm(editMedicinePlan)
            _medicinePlanForm.postValue(editMedicinePlanForm)
            _timeDoseFormItemList.postValue(editMedicinePlan.timeDoseList.map {
                TimeDoseFormItem(timeDose = it, medicineUnit = selectedMedicineShortInfo.value?.unit)
            }.toMutableList())
            _daysOfWeekFormItem.postValue(editMedicinePlan.daysOfWeek?.let { DaysOfWeekFormItem(it) })
            selectedMedicineId.postValue(editMedicinePlan.medicineId)
        } else {
            _medicinePlanForm.postValue(getEmptyForm())
            _timeDoseFormItemList.postValue(
                mutableListOf(
                    TimeDoseFormItem(
                        time = AppTime(8, 0),
                        doseSize = 1f,
                        medicineUnit = selectedMedicineShortInfo.value?.unit ?: "--"
                    )
                )
            )
            selectedMedicineId.postValue(null)
        }
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
                durationType = _medicinePlanForm.value?.durationType!!,
                startDate = _medicinePlanForm.value?.startDate!!,
                endDate = _medicinePlanForm.value?.endDate,
                daysType = _medicinePlanForm.value?.daysType,
                daysOfWeek = _daysOfWeekFormItem.value?.toDaysOfWeek(),
                intervalOfDays = _medicinePlanForm.value?.intervalOfDays,
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
        val durationType = _medicinePlanForm.value?.durationType
        val startDate = _medicinePlanForm.value?.startDate
        val endDate = _medicinePlanForm.value?.endDate
        val daysType = _medicinePlanForm.value?.daysType
        val daysOfWeek = _daysOfWeekFormItem.value

        //todo nie ma walidaacji kolejności dat
        val medicineError = if (medicineId == null) {
            "Nie wybrano leku"
        } else null
        val startDateError = if (startDate == null) {
            "Pole jest wymagane"
        } else null
        val endDateError = if (durationType == DurationType.PERIOD && endDate == null) {
            "Pole jest wymagane"
        } else null
        val daysOfWeekError = if (daysType == DaysType.DAYS_OF_WEEK && daysOfWeek == null) {
            "Nie wybrano dni tygodnia"
        } else null

        val formError = MedicinePlanFormError(
            globalError = medicineError ?: daysOfWeekError,
            startDateError = startDateError,
            endDateError = endDateError
        )
        _medicinePlanFormError.postValue(formError)

        return arrayOf(medicineError, startDateError, endDateError, daysOfWeekError).all { it == null }
    }

    private fun getEmptyForm() = MedicinePlanForm(
        _durationType = DurationType.ONCE,
        _startDate = dateTimeUseCases.getCurrDate(),
        _endDate = null,
        _daysType = null,
        _intervalOfDays = 1
    )
}