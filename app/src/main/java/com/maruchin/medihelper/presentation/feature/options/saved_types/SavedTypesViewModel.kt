package com.maruchin.medihelper.presentation.feature.options.saved_types

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.usecases.saved_types.AddSavedTypeUseCase
import com.maruchin.medihelper.domain.usecases.saved_types.DeleteSavedTypeUseCase
import com.maruchin.medihelper.domain.usecases.saved_types.GetLiveSavedTypesUseCase
import kotlinx.coroutines.launch

abstract class SavedTypesViewModel(
    private val getLiveSavedTypesUseCase: GetLiveSavedTypesUseCase,
    private val deleteSavedTypeUseCase: DeleteSavedTypeUseCase,
    private val addSavedTypeUseCase: AddSavedTypeUseCase,
    private val typesName: String
) : ViewModel() {

    val types: LiveData<List<String>>
    val title: LiveData<String>

    init {
        types = getLiveTypes()
        title = getLiveTitle()
    }

    fun deleteType(type: String) = viewModelScope.launch {
        deleteSavedTypeUseCase.execute(type)
    }

    fun addType(type: String) = viewModelScope.launch {
        addSavedTypeUseCase.execute(type)
    }

    private fun getLiveTypes() = liveData {
        val source = getLiveSavedTypesUseCase.execute()
        emitSource(source)
    }

    private fun getLiveTitle() = liveData {
        emit(typesName)
    }
}