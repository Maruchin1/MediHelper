package com.example.medihelper.mainapp.medickit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.R
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import java.io.File

class MedicineDetailsViewModel : ViewModel() {

    val selectedMedicineIDLive = MutableLiveData<Int>()
    val medicineDetailsLive: LiveData<MedicineDetails>

    val stateAvailableLive: LiveData<Boolean>

    val photoLive: LiveData<File>
    val medicineNameLive: LiveData<String>
    val stateWeightLive: LiveData<Float>
    val emptyWeightLive: LiveData<Float>
    val stateNumberStringLive: LiveData<String>
    val stateTextLive: LiveData<String>
    val stateColorResIdLive: LiveData<Int>
    val typeNameLive: LiveData<String>
    val expireDateLive: LiveData<String>
    val daysRemainsLive: LiveData<String>
    val comments: LiveData<String>

    init {
        medicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            AppRepository.getMedicineDetailsLive(medicineID)
        }
        stateAvailableLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.packageSize != null || medicineDetails?.currState != null
        }
        photoLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails.photoFilePath?.let { File(it) }
        }
        medicineNameLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.medicineName
        }
        stateNumberStringLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            "${medicineDetails.currState}/${medicineDetails.packageSize}"
        }
        typeNameLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails.medicineUnit
        }
        expireDateLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.expireDate?.let { expireDate ->
                AppDateTimeUtil.dateToString(expireDate)
            }
        }
        daysRemainsLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.let { daysRemainsString(it) }
        }
        comments = Transformations.map(medicineDetailsLive) { medicine ->
            medicine?.comments
        }
        stateWeightLive = Transformations.map(medicineDetailsLive) { medicine ->
            medicine?.let { stateWeight(it) }
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

    fun deleteMedicine() = medicineDetailsLive.value?.let { AppRepository.deleteMedicine(it.medicineID) }

    private fun stateWeight(medicineDetails: MedicineDetails): Float? {
        return medicineDetails.currState?.let { currState ->
            medicineDetails.packageSize?.let { packageSize ->
                currState.div(packageSize)
            }
        }
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

    private fun daysRemainsString(medicineDetails: MedicineDetails): String? {
        return medicineDetails.expireDate?.let { expireDate ->
            val currDate = AppDateTimeUtil.getCurrCalendar().time
            val daysBetween = AppDateTimeUtil.daysBetween(currDate, expireDate)
            "$daysBetween dni"
        }
    }

    companion object {
        const val TEXT_STATE_GOOD = "Duży zapas"
        const val TEXT_STATE_MEDIUM = "Średnia ilość"
        const val TEXT_STATE_SMALL = "Blisko końca"
    }
}