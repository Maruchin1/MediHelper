package com.example.medihelper.presentation.feature.plans

import androidx.lifecycle.*
import com.example.medihelper.custom.FieldMutableLiveData
import com.example.medihelper.domain.entities.*
import com.example.medihelper.domain.usecases.DateTimeUseCases
import com.example.medihelper.domain.usecases.MedicinePlanUseCases
import com.example.medihelper.domain.usecases.MedicineUseCases
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.localdata.pojo.TimeDoseEditData
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanFragmentArgs
import com.example.medihelper.presentation.model.MedicinePlanForm
import com.example.medihelper.presentation.model.MedicinePlanFormError
import com.example.medihelper.presentation.model.MedicineShortInfo
import com.example.medihelper.service.FormValidatorService
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
    val selectedMedicineShortInfo: LiveData<MedicineShortInfo>

    val formTitle: LiveData<String>
        get() = _formTitle
    val medicinePlanForm: MutableLiveData<MedicinePlanForm>
        get() = _medicinePlanForm
    val timeDoseList: LiveData<MutableList<TimeDose>>
        get() = _timeDoseList
    val medicinePlanFormError: LiveData<MedicinePlanFormError>
        get() = _medicinePlanFormError

    private val _formTitle = MutableLiveData<String>("Zaplanuj lek")
    private val _medicinePlanForm = FieldMutableLiveData<MedicinePlanForm>()
    private val _timeDoseList = MutableLiveData<MutableList<TimeDose>>()
    private val _medicinePlanFormError = MutableLiveData<MedicinePlanFormError>()

    private val selectedPerson: LiveData<Person> = personUseCases.getCurrPersonLive()
    private val selectedMedicine: LiveData<Medicine>
    private var editMedicinePlanId: Int? = null

    init {
        colorPrimaryId = Transformations.map(selectedPerson) { it.colorId }
        selectedMedicine = Transformations.switchMap(selectedMedicineId) { medicineId ->
            medicineId?.let { medicineUseCases.getMedicineLiveById(it) }
        }
        selectedMedicineShortInfo = Transformations.map(selectedMedicine) { MedicineShortInfo(it) }
    }

    fun setArgs(args: AddEditMedicinePlanFragmentArgs) = viewModelScope.launch {
        if (args.editMedicinePlanID != -1) {
            editMedicinePlanId = args.editMedicinePlanID
            _formTitle.postValue("Edytuj plan")
            val editMedicinePlan = medicinePlanUseCases.getMedicinePlanById(args.editMedicinePlanID)
            val editMedicinePlanForm = MedicinePlanForm(editMedicinePlan)
            _medicinePlanForm.postValue(editMedicinePlanForm)
            _timeDoseList.postValue(editMedicinePlan.timeDoseList.toMutableList())
            selectedMedicineId.postValue(editMedicinePlan.medicineId)
        } else {
            _medicinePlanForm.postValue(getEmptyForm())
            _timeDoseList.postValue(
                mutableListOf(
                    TimeDose(
                        time = AppTime(8, 0),
                        doseSize = 1f
                    )
                )
            )
            selectedMedicineId.postValue(null)
        }
    }

    fun addTimeOfTaking() {
        _timeDoseList.value?.let { timeOfTakingList ->
            timeOfTakingList.add(TimeDoseEditData())
            _timeDoseList.value = _timeDoseList.value
        }
    }

    fun removeTimeOfTaking(timeDose: TimeDose) {
        _timeDoseList.value?.let { doseHourList ->
            doseHourList.remove(timeDose)
            _timeDoseList.value = _timeDoseList.value
        }
    }

    fun updateTimeOfTaking(position: Int, timeDose: TimeDose) {
        _timeDoseList.value?.let { doseHourList ->
            doseHourList[position] = timeDose
            _timeDoseList.value = _timeDoseList.value
        }
    }

    fun saveMedicinePlan(): Boolean {
        if (isFormValid()) {
            val inputData = MedicinePlanInputData(
                medicineId = selectedMedicineId.value!!,
                personId = selectedPerson.value?.personId!!,
                durationType = _medicinePlanForm.value?.durationType!!,
                startDate = _medicinePlanForm.value?.startDate!!,
                endDate = _medicinePlanForm.value?.endDate,
                daysType = _medicinePlanForm.value?.daysType,
                daysOfWeek = _medicinePlanForm.value?.daysOfWeek,
                intervalOfDays = _medicinePlanForm.value?.intervalOfDays,
                timeDoseList = _timeDoseList.value!!
            )
            GlobalScope.launch {  }
        }
    }

    private fun isFormValid(): Boolean {
        val validator = FormValidatorService()

        val error = validator.isMedicinePlanValid(
            medicineId = selectedMedicineId.value,
            startDate = _medicinePlanForm.value?.startDate,
            endDate = _medicinePlanForm.value?.endDate,
            durationType = _medicinePlanForm.value?.durationType,
            daysType = _medicinePlanForm.value?.daysType,
            daysOfWeek = _medicinePlanForm.value?.daysOfWeek
        )
        var isValid = true
        var globalError = if (error.emptyMedicine) {
            isValid = false
            "Nie wybrano leku"
        } else null
        var startDateError = if (error.emptyStartDate) {
            isValid = false
            "Pole wymagane"
        } else null
        var endDateError = if (error.emptyEndDate) {
            isValid = false
            "Pole wymagane"
        } else null
        val startEndDateError = if (error.incorrectDatesOrder) {
            isValid = false
            "Zła kolejnosć dat"
        } else null
        globalError = if (error.emptyDaysOfWeek) {
            isValid = false
            "Niew wybrano dni tygodnia"
        } else null

        _medicinePlanFormError.postValue(
            MedicinePlanFormError(globalError, startDateError, endDateError)
        )

        return isValid

    }

    private fun getEmptyForm() = MedicinePlanForm(
        _durationType = DurationType.ONCE,
        _startDate = dateTimeUseCases.getCurrDate(),
        _endDate = null,
        _daysType = null,
        _daysOfWeek = DaysOfWeek(
            monday = false,
            tuesday = false,
            wednesday = false,
            thursday = false,
            friday = false,
            saturday = false,
            sunday = false
        ),
        _intervalOfDays = 1
    )
}