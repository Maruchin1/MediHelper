package com.example.medihelper.mainapp.medicines

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entity.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MedicineDetailsViewModel(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val isAppModeConnected: LiveData<Boolean>

    val medicineNameLive: LiveData<String>
    val expireDateLive: LiveData<AppDate>
    val medicineUnitLive: LiveData<String>

    val medicineStateDataLive: LiveData<MedicineEntity.StateData>
    val selectedMedicineIDLive = MutableLiveData<Int>()
    val photoFileLive: LiveData<File>
    val commentsLive: LiveData<String>
    val commentsAvailableLive: LiveData<Boolean>
    val personItemListTakingMedicineLive: LiveData<List<PersonItem>>
    val personItemListTakingMedicineAvailableLive: LiveData<Boolean>
    private val medicineDetailsLive: LiveData<MedicineDetails>
    private val mainPersonItemLive = personRepository.getMainPersonItemLive()

    init {
        colorPrimaryLive = Transformations.map(mainPersonItemLive) { it.personColorResID }
        isAppModeConnected = Transformations.map(sharedPrefService.getAppModeLive()) {
            it == SharedPrefService.AppMode.CONNECTED
        }
        medicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) {
            medicineRepository.getDetailsLive(it)
        }
        medicineNameLive = Transformations.map(medicineDetailsLive) { it.medicineName }
        expireDateLive = Transformations.map(medicineDetailsLive) { it?.expireDate }
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
}