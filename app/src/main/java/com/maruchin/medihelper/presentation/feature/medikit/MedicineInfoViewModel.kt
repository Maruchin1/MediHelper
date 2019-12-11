package com.maruchin.medihelper.presentation.feature.medikit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineInfoUseCase
import com.maruchin.medihelper.domain.usecases.medicines.SearchForMedicineInfoUseCase
import kotlinx.coroutines.launch

class MedicineInfoViewModel(
    private val searchForMedicineInfoUseCase: SearchForMedicineInfoUseCase,
    private val getMedicineInfoUseCase: GetMedicineInfoUseCase
) : ViewModel() {

    val searchResults: LiveData<List<MedicineInfoSearchResult>>
        get() = _searchResults
    val medicineInfo: LiveData<List<MedicineInfo>>
        get() = _medicineInfo

    private val _searchResults = MutableLiveData<List<MedicineInfoSearchResult>>()
    private val _medicineInfo = MutableLiveData<List<MedicineInfo>>()

    fun setArgs(args: MedicineInfoDialogArgs) = viewModelScope.launch {
        val searchResults = searchForMedicineInfoUseCase.execute(args.medicineName)
        _searchResults.postValue(searchResults)
    }

    fun getMedicineInfo(urlString: String) = viewModelScope.launch {
        val info = getMedicineInfoUseCase.execute(urlString)
        _medicineInfo.postValue(info)
    }
}