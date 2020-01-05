package com.maruchin.medihelper.presentation.feature.medicine_details

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.utils.PicturesStorageRef
import kotlinx.coroutines.launch

class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
    private val picturesStorageRef: PicturesStorageRef
) : ViewModel() {

    val data: LiveData<MedicineDetailsData>
        get() = _data
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionDataLoaded: LiveData<Boolean>
        get() = _actionDataLoaded
    val actionMedicineDeleted: LiveData<Boolean>
        get() = _actionMedicineDeleted
    val medicineId: String
        get() = _medicineId

    private val _data = MutableLiveData<MedicineDetailsData>()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionDataLoaded = ActionLiveData()
    private val _actionMedicineDeleted = ActionLiveData()
    private lateinit var _medicineId: String

    fun initViewModel(medicineId: String) = viewModelScope.launch {
        setGlobalMedicineId(medicineId)
        loadAndPostData()
        notifyDataLoaded()
    }

    fun deleteMedicine() = viewModelScope.launch {
        notifyDeletingInProgress()
        deleteMedicineUseCase.execute(_medicineId)
        notifyMedicineDeleted()
    }

    private fun setGlobalMedicineId(medicineId: String) {
        _medicineId = medicineId
    }

    private suspend fun loadAndPostData() {
        val data = getData(_medicineId)
        _data.postValue(data)
    }

    private fun notifyDataLoaded() {
        _actionDataLoaded.sendAction()
    }

    private fun notifyDeletingInProgress() {
        _loadingInProgress.postValue(true)
    }

    private fun notifyMedicineDeleted() {
        _loadingInProgress.postValue(false)
        _actionMedicineDeleted.sendAction()
    }

    private suspend fun getData(medicineId: String): MedicineDetailsData {
        val medicineDetails = getMedicineDetailsUseCase.execute(medicineId)
        return MedicineDetailsData.fromDomainModel(medicineDetails, picturesStorageRef)
    }
}