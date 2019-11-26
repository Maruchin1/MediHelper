package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.StatusOfTaking
import com.maruchin.medihelper.domain.usecases.MedicineUseCases
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.domain.usecases.PlannedMedicineUseCases
import com.maruchin.medihelper.presentation.framework.map
import com.maruchin.medihelper.presentation.framework.switchMap
import com.maruchin.medihelper.presentation.model.PlannedMedicineOptionsData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlannedMedicineOptionsViewModel(
    private val personUseCases: PersonUseCases,
    private val plannedMedicineUseCases: PlannedMedicineUseCases,
    private val medicineUseCases: MedicineUseCases
) : ViewModel() {

    val colorPrimaryId: LiveData<Int> = MutableLiveData(R.color.colorPrimary)
    val optionsData: LiveData<PlannedMedicineOptionsData> = MutableLiveData()
    val takeMedicineBtnText: LiveData<String> = MutableLiveData()
    val takeMedicineBtnIcon: LiveData<Int> = MutableLiveData()

    private val plannedMedicineId = MutableLiveData<Int>()
//    private val plannedMedicineWithMedicine: LiveData<PlannedMedicineWithMedicine>

    init {
//        colorPrimaryId = personUseCases.getCurrPersonLive().map { it.color }
//        plannedMedicineWithMedicine = plannedMedicineId.switchMap {
//            plannedMedicineUseCases.getPlannedMedicineWithMedicineLiveById(it)
//        }
//        optionsData = plannedMedicineWithMedicine.map { PlannedMedicineOptionsData(it) }
//        takeMedicineBtnText = plannedMedicineWithMedicine.map {
//            if (it.statusOfTaking == StatusOfTaking.TAKEN) {
//                "Anuluj przyjecie leku"
//            } else {
//                "Przyjmij lek"
//            }
//        }
//        takeMedicineBtnIcon = plannedMedicineWithMedicine.map {
//            if (it.statusOfTaking == StatusOfTaking.TAKEN) {
//                R.drawable.round_close_black_24
//            } else {
//                R.drawable.baseline_check_white_24
//            }
//        }
    }

    fun setArgs(args: PlannedMedicineOptionsDialogArgs) {
        plannedMedicineId.value = args.plannedMedicineID
    }

    fun getMedicineId(): Int? {
//        plannedMedicineWithMedicine.value?.medicine?.medicineId
        return 1
    }

    fun changePlannedMedicineStatus() = GlobalScope.launch {
//        plannedMedicineWithMedicine.value?.let {
//            if (it.statusOfTaking == StatusOfTaking.TAKEN) {
//                plannedMedicineUseCases.changeMedicineTaken(it.plannedMedicineId, false)
//                medicineUseCases.increaseMedicineCurrState(it.medicine.medicineId, it.plannedDoseSize)
//            } else {
//                plannedMedicineUseCases.changeMedicineTaken(it.plannedMedicineId, true)
//                medicineUseCases.reduceMedicineCurrState(it.medicine.medicineId, it.plannedDoseSize)
//            }
//        }
    }
}