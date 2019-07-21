package com.example.medihelper.mainapp.medickit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.DateUtil
import com.example.medihelper.R
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.io.File

class MedicineDetailsViewModel : ViewModel() {
    val selectedMedicineID = MutableLiveData<Int>()
    val selectedMedicine: LiveData<Medicine>

    val stateAvailableLive: LiveData<Boolean>

    val photoLive: LiveData<File>
    val nameLive: LiveData<String>
    val stateWeightLive: LiveData<Float>
    val emptyWeightLive: LiveData<Float>
    val stateNumberStringLive: LiveData<String>
    val stateTextLive: LiveData<String>
    val stateColorResIdLive: LiveData<Int>
    val medicineTypeLive: LiveData<MedicineType>
    val expireDateLive: LiveData<String>
    val daysRemainsLive: LiveData<String>
    val comments: LiveData<String>

    init {
        selectedMedicine = Transformations.switchMap(selectedMedicineID) { medicineID ->
            Repository.getMedicineByIdLive(medicineID)
        }
        stateAvailableLive = Transformations.map(selectedMedicine) { medicine ->
            medicine.packageSize != null || medicine.currState != null
        }
        photoLive = Transformations.map(selectedMedicine) { medicine ->
            if (medicine.photoFilePath.isNullOrEmpty()) {
                null
            } else {
                File(medicine.photoFilePath)
            }
        }
        nameLive = Transformations.map(selectedMedicine) { medicine ->
            medicine.name
        }
        stateNumberStringLive = Transformations.map(selectedMedicine) { medicine ->
            stateNumberString(medicine)
        }
        medicineTypeLive = Transformations.switchMap(selectedMedicine) { medicine ->
            medicine.medicineTypeID?.let { medicineTypeID ->
                Repository.getMedicineTypeByIdLive(medicineTypeID)
            }
        }
        expireDateLive = Transformations.map(selectedMedicine) { medicine ->
            medicine.expireDate?.let { expireDate ->
                DateUtil.dateToString(expireDate)
            }
        }
        daysRemainsLive = Transformations.map(selectedMedicine) { medicine ->
            daysRemainsString(medicine)
        }
        comments = Transformations.map(selectedMedicine) { medicine ->
            medicine.comments
        }
        stateWeightLive = Transformations.map(selectedMedicine) { medicine ->
            stateWeight(medicine)
        }
        emptyWeightLive = Transformations.map(stateWeightLive) { state ->
            state?.let { 1 - it }
        }
        stateTextLive = Transformations.map(stateWeightLive) { state ->
            state?.let { stateText(it) }
        }
        stateColorResIdLive = Transformations.map(stateWeightLive) { state ->
            state?.let { stateColorResId(it) }
        }
    }

    fun deleteMedicine() = selectedMedicine.value?.let { Repository.deleteMedicine(it) }

    private fun stateWeight(medicine: Medicine): Float? {
        return medicine.currState?.let { currState ->
            medicine.packageSize?.let { packageSize ->
                return currState.div(packageSize)
            }
        }
    }

    private fun stateNumberString(medicine: Medicine): String {
        return "${medicine.currState}/${medicine.packageSize}"
    }

    private fun stateColorResId(stateWeight: Float): Int {
        val stateGoodLimit = KitViewModel.STATE_GOOD_LIMIT
        val stateMediumLimit = KitViewModel.STATE_MEDIUM_LIMIT
        return when {
            stateWeight >= stateGoodLimit -> R.color.colorStateGood
            stateWeight > stateMediumLimit -> R.color.colorStateMedium
            else -> R.color.colorStateSmall
        }
    }

    private fun stateText(stateWeight: Float): String {
        val stateGoodLimit = KitViewModel.STATE_GOOD_LIMIT
        val stateMediumLimit = KitViewModel.STATE_MEDIUM_LIMIT
        return when {
            stateWeight >= stateGoodLimit -> TEXT_STATE_GOOD
            stateWeight > stateMediumLimit -> TEXT_STATE_MEDIUM
            else -> TEXT_STATE_SMALL
        }
    }

    private fun daysRemainsString(medicine: Medicine): String? {
        return medicine.expireDate?.let { expireDate ->
            val currDate = DateUtil.getCurrCalendar().time
            val daysBetween = DateUtil.daysBetween(currDate, expireDate)
            "$daysBetween dni"
        }
    }

    companion object {
        const val TEXT_STATE_GOOD = "Duży zapas"
        const val TEXT_STATE_MEDIUM = "Średnia ilość"
        const val TEXT_STATE_SMALL = "Blisko końca"
    }
}