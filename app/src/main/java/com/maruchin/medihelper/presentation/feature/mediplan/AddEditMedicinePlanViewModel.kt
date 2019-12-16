package com.maruchin.medihelper.presentation.feature.mediplan

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.datetime.GetCurrDateUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineNameUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileItemUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class AddEditMedicinePlanViewModel(
    private val getProfileItemUseCase: GetProfileItemUseCase,
    private val getMedicineNameUseCase: GetMedicineNameUseCase,
    private val getCurrDateUseCase: GetCurrDateUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String>
    val profileName: LiveData<String>
    val durationType = MutableLiveData<DurationType>()
    val startDate = MutableLiveData<AppDate>()
    val endDate = MutableLiveData<AppDate>()
    val intakeDaysType = MutableLiveData<IntakeDaysType>()
    val daysOfWeek = MutableLiveData<IntakeDays.DaysOfWeek>()

    val formTitle: LiveData<String>
        get() = _formTitle
    val medicineName: LiveData<String>
        get() = _medicineName
    val interval: LiveData<IntakeDays.Interval>
        get() = _interval
    val sequence: LiveData<IntakeDays.Sequence>
        get() = _sequence
    val actionDataLoaded: LiveData<Boolean>
        get() = _actionDataLoaded

    private val _formTitle = MutableLiveData<String>("Zaplanuj lek")
    private val _medicineName = MutableLiveData<String>()
    private val _interval = MutableLiveData<IntakeDays.Interval>()
    private val _sequence = MutableLiveData<IntakeDays.Sequence>()
    private val _actionDataLoaded = ActionLiveData()

    private val profileItem = MutableLiveData<ProfileItem>()

    init {
        colorPrimary = Transformations.map(profileItem) { it.color }
        profileName = Transformations.map(profileItem) { it.name }
        loadDefaultData()
    }

    fun setInterval(value: Int) {
        _interval.value = IntakeDays.Interval(daysCount = value)
    }

    fun setIntakeDays(value: Int) {
        val currSequence = _sequence.value
        _sequence.value = currSequence?.copy(intakeCount = value)
    }

    fun setNoIntakeDays(value: Int) {
        val currSequence = _sequence.value
        _sequence.value = currSequence?.copy(notIntakeCount = value)
    }

    fun setArgs(args: AddEditMedicinePlanFragmentArgs) = viewModelScope.launch {
        val profileItemValue = getProfileItemUseCase.execute(args.profileId)
        val medicineNameValue = getMedicineNameUseCase.execute(args.medicineId)

        profileItem.postValue(profileItemValue)
        _medicineName.postValue(medicineNameValue)
        _actionDataLoaded.sendAction()
    }

    private fun loadDefaultData() {
        durationType.postValue(DurationType.ONCE)
        val currDate = getCurrDateUseCase.execute()
        startDate.postValue(currDate)
        endDate.postValue(currDate.apply { addDays(1) })
        intakeDaysType.postValue(IntakeDaysType.EVERYDAY)
        daysOfWeek.postValue(
            IntakeDays.DaysOfWeek(
                monday = false,
                tuesday = false,
                wednesday = false,
                thursday = false,
                friday = false,
                saturday = false,
                sunday = false
            )
        )
        _interval.postValue(IntakeDays.Interval(daysCount = 2))
        _sequence.postValue(IntakeDays.Sequence(intakeCount = 21, notIntakeCount = 7))
    }

    enum class DurationType {
        ONCE, PERIOD, CONTINUOUS
    }

    enum class IntakeDaysType {
        EVERYDAY, DAYS_OF_WEEK, INTERVAL, SEQUENCE
    }
}