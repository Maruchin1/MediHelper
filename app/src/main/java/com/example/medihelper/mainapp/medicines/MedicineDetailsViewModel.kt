package com.example.medihelper.mainapp.medicines

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entity.MedicineEntity
import com.example.medihelper.localdatabase.pojo.MedicineDetails
import com.example.medihelper.localdatabase.pojo.PersonItem
import com.example.medihelper.service.AppMode
import com.example.medihelper.service.MedicineService
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MedicineDetailsViewModel(
    private val appFilesDir: File,
    private val personService: PersonService,
    private val medicineService: MedicineService,
    private val serverApiService: ServerApiService
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
    private val mainPersonItemLive = personService.getMainPersonItemLive()

    init {
        colorPrimaryLive = Transformations.map(mainPersonItemLive) { it.personColorResID }
        isAppModeConnected = Transformations.map(serverApiService.getAppModeLive()) {
            it == AppMode.CONNECTED
        }
        medicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) {
            medicineService.getDetailsLive(it)
        }
        medicineNameLive = Transformations.map(medicineDetailsLive) { it.medicineName }
        expireDateLive = Transformations.map(medicineDetailsLive) { it?.expireDate }
        medicineUnitLive = Transformations.map(medicineDetailsLive) { it.medicineUnit }

        medicineStateDataLive = Transformations.map(medicineDetailsLive) {
            MedicineEntity.StateData(it.packageSize, it.currState)
        }
        personItemListTakingMedicineLive = Transformations.switchMap(selectedMedicineIDLive) {
            personService.getItemListLiveByMedicineID(it)
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
            medicineService.delete(medicineDetails.medicineID)
        }
    }

    fun setArgs(args: MedicineDetailsFragmentArgs) {
        selectedMedicineIDLive.value = args.medicineID
    }

    fun takeMedicineDose(doseSize: Float) = viewModelScope.launch {
        selectedMedicineIDLive.value?.let { medicineID ->
            medicineService.reduceCurrState(medicineID, doseSize)
        }
    }
}