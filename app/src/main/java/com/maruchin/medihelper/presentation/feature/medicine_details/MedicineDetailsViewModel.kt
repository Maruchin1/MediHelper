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

    // Theoretically this property is unnecessary, but there is a bug with DataBinding and TextView
    // textColor property. Only separate LiveData with colorId works properly.
    val stateColor: LiveData<Int>

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

    init {
        stateColor = getLiveStateColor()
    }

    fun initViewModel(medicineId: String) = viewModelScope.launch {
        _medicineId = medicineId
        loadAndPostData()
        _actionDataLoaded.sendAction()
    }

    fun deleteMedicine() = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        deleteMedicineUseCase.execute(_medicineId)
        _loadingInProgress.postValue(false)
        _actionMedicineDeleted.sendAction()
    }

    private fun getLiveStateColor() = Transformations.map(_data) { data ->
        data.state.stateColorId
    }

    private suspend fun loadAndPostData() {
        val details = getMedicineDetailsUseCase.execute(medicineId)
        val data = MedicineDetailsData.fromDomainModel(details, picturesStorageRef)
        _data.postValue(data)
    }
}