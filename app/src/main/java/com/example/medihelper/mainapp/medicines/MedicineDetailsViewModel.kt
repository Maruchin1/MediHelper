package com.example.medihelper.mainapp.medicines

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.R
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import kotlinx.coroutines.launch
import java.io.File

class MedicineDetailsViewModel(
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository
) : ViewModel() {

    val selectedMedicineIDLive = MutableLiveData<Int>()
    val photoFileLive: LiveData<File>
    val medicineNameLive: LiveData<String>
    val medicineUnitLive: LiveData<String>
    val stateWeightLive: LiveData<Float>
    val emptyWeightLive: LiveData<Float>
    val stateNumberStringLive: LiveData<String>
    val stateTextLive: LiveData<String>
    val stateColorResIdLive: LiveData<Int>
    val typeNameLive: LiveData<String>
    val expireDateLive: LiveData<String>
    val daysRemainsLive: LiveData<String>
    val commentsLive: LiveData<String>
    val commentsAvailableLive: LiveData<Boolean>
    val stateAvailableLive: LiveData<Boolean>
    val personItemListTakingMedicineLive: LiveData<List<PersonItem>>
    val personItemListTakingMedicineAvailableLive: LiveData<Boolean>
    private val medicineDetailsLive: LiveData<MedicineDetails>

    init {
        medicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            medicineRepository.getDetailsLive(medicineID)
        }
        stateAvailableLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.packageSize != null || medicineDetails?.currState != null
        }
        photoFileLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails.photoFilePath?.let { File(it) }
        }
        medicineNameLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.medicineName
        }
        medicineUnitLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails.medicineUnit
        }
        stateNumberStringLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            "${medicineDetails.currState}/${medicineDetails.packageSize}"
        }
        typeNameLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails.medicineUnit
        }
        expireDateLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.expireDate?.formatString
        }
        daysRemainsLive = Transformations.map(medicineDetailsLive) { medicineDetails ->
            medicineDetails?.let { daysRemainsString(it) }
        }
        commentsLive = Transformations.map(medicineDetailsLive) { medicine ->
            medicine?.comments
        }
        commentsAvailableLive = Transformations.map(commentsLive) { comments ->
            comments != null && comments.isNotEmpty()
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
        personItemListTakingMedicineLive = Transformations.switchMap(selectedMedicineIDLive) { medicineID ->
            personRepository.getItemListLiveByMedicineID(medicineID)
        }
        personItemListTakingMedicineAvailableLive = Transformations.map(personItemListTakingMedicineLive) { personItemList ->
            personItemList != null && personItemList.isNotEmpty()
        }
    }

    fun deleteMedicine() = viewModelScope.launch {
        medicineDetailsLive.value?.let { medicineDetails ->
            medicineRepository.delete(medicineDetails.medicineID)
        }
    }

    fun setArgs(args: MedicineDetailsFragmentArgs) {
        selectedMedicineIDLive.value = args.medicineID
    }

    fun takeMedicineDose(doseSize: Float) = viewModelScope.launch {
        selectedMedicineIDLive.value?.let { medicineID ->
            val medicineEntity = medicineRepository.getEntity(medicineID)
            medicineEntity.reduceCurrState(doseSize)
            medicineRepository.update(medicineEntity)
        }
    }

    private fun stateWeight(medicineDetails: MedicineDetails): Float? {
        return medicineDetails.currState?.let { currState ->
            medicineDetails.packageSize?.let { packageSize ->
                currState.div(packageSize)
            }
        }
    }

    private fun stateColorResId(stateWeight: Float): Int {
        val stateGoodLimit = MedicinesViewModel.STATE_GOOD_LIMIT
        val stateMediumLimit = MedicinesViewModel.STATE_MEDIUM_LIMIT
        return when {
            stateWeight >= stateGoodLimit -> R.color.colorStateGood
            stateWeight > stateMediumLimit -> R.color.colorStateMedium
            else -> R.color.colorStateSmall
        }
    }

    private fun stateText(stateWeight: Float): String {
        val stateGoodLimit = MedicinesViewModel.STATE_GOOD_LIMIT
        val stateMediumLimit = MedicinesViewModel.STATE_MEDIUM_LIMIT
        return when {
            stateWeight >= stateGoodLimit -> TEXT_STATE_GOOD
            stateWeight > stateMediumLimit -> TEXT_STATE_MEDIUM
            else -> TEXT_STATE_SMALL
        }
    }

    private fun daysRemainsString(medicineDetails: MedicineDetails): String? {
        return medicineDetails.expireDate?.let { expireDate ->
            val currDate = AppDate.currDate()
            val daysBetween = AppDate.daysBetween(currDate, expireDate)
            "$daysBetween dni"
        }
    }

    companion object {
        const val TEXT_STATE_GOOD = "Duży zapas"
        const val TEXT_STATE_MEDIUM = "Średnia ilość"
        const val TEXT_STATE_SMALL = "Blisko końca"
    }
}