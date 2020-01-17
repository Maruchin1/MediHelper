package com.maruchin.medihelper.presentation.feature.options.saved_types

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.usecases.settings.GetLiveSavedTypesUseCase

abstract class SavedTypesViewModel(
    private val getLiveSavedTypesUseCase: GetLiveSavedTypesUseCase,
    private val typesName: String
) : ViewModel() {

    val types: LiveData<List<String>>
    val title: LiveData<String>

    init {
        types = getLiveTypes()
        title = getLiveTitle()
    }

    fun deleteType(type: String) {

    }

    private fun getLiveTypes() = liveData {
        val source = getLiveSavedTypesUseCase.execute()
        emitSource(source)
    }

    private fun getLiveTitle() = liveData {
        emit(typesName)
    }
}