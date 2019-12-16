package com.maruchin.medihelper.presentation.feature.mediplan

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.datetime.GetCurrDateUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineNameUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileItemUseCase
import kotlinx.coroutines.launch

class AddEditMedicinePlanViewModel(
    private val getProfileItemUseCase: GetProfileItemUseCase,
    private val getMedicineNameUseCase: GetMedicineNameUseCase,
    private val getCurrDateUseCase: GetCurrDateUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String>
    val profileName: LiveData<String>
    val durationType = MutableLiveData<DurationType>(DurationType.ONCE)
    val startDate = MutableLiveData<AppDate>(getCurrDateUseCase.execute())
    val endDate = MutableLiveData<AppDate>()
    val intakeDays = MutableLiveData<IntakeDays>(IntakeDays.EVERYDAY)

    val formTitle: LiveData<String>
        get() = _formTitle
    val medicineName: LiveData<String>
        get() = _medicineName

    private val _formTitle = MutableLiveData<String>("Zaplanuj lek")
    private val _medicineName = MutableLiveData<String>()

    private val profileItem = MutableLiveData<ProfileItem>()

    init {
        colorPrimary = Transformations.map(profileItem) { it.color }
        profileName = Transformations.map(profileItem) { it.name }
    }

    fun setArgs(args: AddEditMedicinePlanFragmentArgs) = viewModelScope.launch {
        val profileItemValue = getProfileItemUseCase.execute(args.profileId)
        val medicineNameValue = getMedicineNameUseCase.execute(args.medicineId)

        profileItem.postValue(profileItemValue)
        _medicineName.postValue(medicineNameValue)
    }

    enum class DurationType {
        ONCE, PERIOD, CONTINUOUS
    }

    enum class IntakeDays {
        EVERYDAY, DAYS_OF_WEEK, INTERVAL, SEQUENCE
    }
}