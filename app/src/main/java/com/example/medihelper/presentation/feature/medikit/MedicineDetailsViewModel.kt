package com.example.medihelper.presentation.feature.medikit

import androidx.lifecycle.*
import com.example.medihelper.domain.entities.AppMode
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.entities.Person
import com.example.medihelper.domain.usecases.MedicineUseCases
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.domain.usecases.ServerConnectionUseCases
import com.example.medihelper.presentation.model.MedicineDetails
import com.example.medihelper.presentation.model.PersonItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MedicineDetailsViewModel(
    private val personUseCases: PersonUseCases,
    private val serverConnectionUseCases: ServerConnectionUseCases,
    private val medicineUseCases: MedicineUseCases
) : ViewModel() {

    val colorPrimary: LiveData<Int>
    val appModeConnected: LiveData<Boolean>
    val medicineDetails: LiveData<MedicineDetails>
    val personItemListTakingMedicine: LiveData<List<PersonItem>>
    val personItemListTakingMedicineAvailable: LiveData<Boolean>

    val selectedMedicineId: LiveData<Int>
        get() = _selectedMedicineId

    private val _selectedMedicineId = MutableLiveData<Int>()

    private val selectedMedicine: LiveData<Medicine>
    private val personListTakingMedicine: LiveData<List<Person>>

    init {
        colorPrimary = personUseCases.getMainPersonColorLive()
        appModeConnected = Transformations.map(serverConnectionUseCases.getAppModeLive()) { it == AppMode.CONNECTED }

        selectedMedicine = Transformations.switchMap(_selectedMedicineId) { medicineUseCases.getMedicineLiveById(it) }
        medicineDetails = Transformations.map(selectedMedicine) { MedicineDetails(it) }
        personListTakingMedicine = Transformations.switchMap(_selectedMedicineId) {
            personUseCases.getPersonListLiveByMedicineId(it)
        }
        personItemListTakingMedicine = Transformations.map(personListTakingMedicine) { personList ->
            personList.map { PersonItem(it) }
        }
        personItemListTakingMedicineAvailable = Transformations.map(personListTakingMedicine) { !it.isNullOrEmpty() }
    }

    fun setArgs(args: MedicineDetailsFragmentArgs) {
        _selectedMedicineId.value = args.medicineID
    }

    fun deleteMedicine() = GlobalScope.launch {
        medicineDetails.value?.let {
            medicineUseCases.deleteMedicineById(it.medicineId)
        }
    }

    fun takeMedicineDose(doseSize: Float) = viewModelScope.launch {
        selectedMedicineId.value?.let {
            medicineUseCases.reduceMedicineCurrState(it, doseSize)
        }
    }
}