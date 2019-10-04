package com.example.medihelper.mainapp.medicines

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MedicineDetailsViewModel(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository
) : ViewModel() {

    val medicineNameLive: LiveData<String>
    val expireDateLive: LiveData<String>
    val daysRemainsLive: LiveData<String>
    val medicineUnitLive: LiveData<String>

    val medicineStateDataLive: LiveData<MedicineEntity.StateData>
    val selectedMedicineIDLive = MutableLiveData<Int>()
    val photoFileLive: LiveData<File>
    val commentsLive: LiveData<String>
    val commentsAvailableLive: LiveData<Boolean>
    val personItemListTakingMedicineLive: LiveData<List<PersonItem>>
    val personItemListTakingMedicineAvailableLive: LiveData<Boolean>
    private val medicineDetailsLive: LiveData<MedicineDetails>

    init {
        medicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) {
            medicineRepository.getDetailsLive(it)
        }
        medicineNameLive = Transformations.map(medicineDetailsLive) { it.medicineName }
        expireDateLive = Transformations.map(medicineDetailsLive) { it?.expireDate?.formatString }
        daysRemainsLive = Transformations.map(medicineDetailsLive) { daysRemainsString(it) }
        medicineUnitLive = Transformations.map(medicineDetailsLive) { it.medicineUnit }

        medicineStateDataLive = Transformations.map(medicineDetailsLive) {
            MedicineEntity.StateData(it.packageSize, it.currState)
        }
        personItemListTakingMedicineLive = Transformations.switchMap(selectedMedicineIDLive) {
            personRepository.getItemListLiveByMedicineID(it)
        }
        personItemListTakingMedicineAvailableLive = Transformations.map(personItemListTakingMedicineLive) {
            it != null && it.isNotEmpty()
        }

        photoFileLive = Transformations.map(medicineDetailsLive) {
            if (it.imageName != null) File(appFilesDir, it.imageName) else null
        }
        commentsLive = Transformations.map(medicineDetailsLive) { it?.additionalInfo }
        commentsAvailableLive = Transformations.map(commentsLive) { it != null && it.isNotEmpty() }


    }

    fun deleteMedicine() = GlobalScope.launch {
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

    private fun daysRemainsString(medicineDetails: MedicineDetails): String? {
        return medicineDetails.expireDate?.let { expireDate ->
            val currDate = AppDate.currDate()
            val daysBetween = AppDate.daysBetween(currDate, expireDate)
            "$daysBetween dni"
        }
    }
}