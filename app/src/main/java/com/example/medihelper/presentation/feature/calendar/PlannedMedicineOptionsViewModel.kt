package com.example.medihelper.presentation.feature.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.R
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.example.medihelper.domain.entities.StatusOfTaking
import com.example.medihelper.domain.usecases.MedicineUseCases
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.domain.usecases.PlannedMedicineUseCases
import com.example.medihelper.presentation.model.PlannedMedicineOptionsData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlannedMedicineOptionsViewModel(
    private val personUseCases: PersonUseCases,
    private val plannedMedicineUseCases: PlannedMedicineUseCases,
    private val medicineUseCases: MedicineUseCases
) : ViewModel() {

    val colorPrimaryId: LiveData<Int>
    val optionsData: LiveData<PlannedMedicineOptionsData>
    val takeMedicineBtnText: LiveData<String>
    val takeMedicineBtnIcon: LiveData<Int>

    val plannedMedicineId: LiveData<Int>
        get() = _plannedMedicineId

    private val _plannedMedicineId = MutableLiveData<Int>()

    private val plannedMedicineWithMedicine: LiveData<PlannedMedicineWithMedicine>

    init {
        colorPrimaryId = Transformations.map(personUseCases.getCurrPersonLive()) { it.colorId }
        plannedMedicineWithMedicine = Transformations.switchMap(plannedMedicineId) {
            plannedMedicineUseCases.getPlannedMedicineWithMedicineLiveById(it)
        }
        optionsData = Transformations.map(plannedMedicineWithMedicine) { PlannedMedicineOptionsData(it) }
        takeMedicineBtnText = Transformations.map(plannedMedicineWithMedicine) {
            if (it.statusOfTaking == StatusOfTaking.TAKEN) {
                "Anuluj przyjecie leku"
            } else {
                "Przyjmij lek"
            }
        }
        takeMedicineBtnIcon = Transformations.map(plannedMedicineWithMedicine) {
            if (it.statusOfTaking == StatusOfTaking.TAKEN) {
                R.drawable.round_close_black_24
            } else {
                R.drawable.baseline_check_white_24
            }
        }
    }

    fun setArgs(args: PlannedMedicineOptionsDialogArgs) {
        _plannedMedicineId.value = args.plannedMedicineID
    }

    fun changePlannedMedicineStatus() = GlobalScope.launch {
        plannedMedicineWithMedicine.value?.let {
            if (it.statusOfTaking == StatusOfTaking.TAKEN) {
                plannedMedicineUseCases.changeMedicineTaken(it.plannedMedicineId, false)
                medicineUseCases.increaseMedicineCurrState(it.medicine.medicineId, it.plannedDoseSize)
            } else {
                plannedMedicineUseCases.changeMedicineTaken(it.plannedMedicineId, true)
                medicineUseCases.reduceMedicineCurrState(it.medicine.medicineId, it.plannedDoseSize)
            }
        }
    }
}