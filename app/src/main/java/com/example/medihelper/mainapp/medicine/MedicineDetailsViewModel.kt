package com.example.medihelper.mainapp.medicine

import androidx.lifecycle.*
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
    private val personService: PersonService,
    private val medicineService: MedicineService,
    private val serverApiService: ServerApiService
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val isAppModeConnected: LiveData<Boolean>

    val medicineDetailsLive: LiveData<MedicineDetails>
    val medicineStateDataLive: LiveData<MedicineEntity.StateData>
    val selectedMedicineIDLive = MutableLiveData<Int>()
    val photoFileLive: LiveData<File>
    val additionalInfoAvailableLive: LiveData<Boolean>
    val personItemListTakingMedicineLive: LiveData<List<PersonItem>>
    val personItemListTakingMedicineAvailableLive: LiveData<Boolean>
    private val mainPersonItemLive = personService.getMainPersonItemLive()

    init {
        colorPrimaryLive = Transformations.map(mainPersonItemLive) { it.personColorResID }
        isAppModeConnected = Transformations.map(serverApiService.getAppModeLive()) {
            it == AppMode.CONNECTED
        }
        medicineDetailsLive = Transformations.switchMap(selectedMedicineIDLive) {
            medicineService.getDetailsLive(it)
        }

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
            it.imageName?.let { imageName ->
                medicineService.getImageFile(imageName)
            }
        }
        additionalInfoAvailableLive = Transformations.map(medicineDetailsLive) {
            it.additionalInfo != null && it.additionalInfo.isNotEmpty()
        }
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