package com.maruchin.medihelper.presentation.feature.personsprofiles

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.PersonInputData
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.domain.usecases.ServerConnectionUseCases
import com.maruchin.medihelper.presentation.model.PersonColorCheckboxData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditPersonViewModel(
    private val personUseCases: PersonUseCases,
    private val serverConnectionUseCases: ServerConnectionUseCases
) : ViewModel() {

    val personColorCheckboxDataList: LiveData<List<PersonColorCheckboxData>>
    val personName = MutableLiveData<String>()
    val selectedColorId = MutableLiveData<Int>()

    val formTitle: LiveData<String>
        get() = _formTitle
    val personNameError: LiveData<String>
        get() = _personNameError

    private val _formTitle = MutableLiveData<String>("Dodaj osobę")
    private val _personNameError = MutableLiveData<String>()

    private val personColorIdList: List<Int> = personUseCases.getColorIdList()
    private var editPersonId: Int? = null

    init {
        personColorCheckboxDataList = Transformations.map(selectedColorId) { selectedColorId ->
            personColorIdList.map { colorId ->
                PersonColorCheckboxData(
                    colorId = colorId,
                    selected = colorId == selectedColorId
                )
            }
        }
    }

    fun setArgs(args: AddEditPersonFragmentArgs) = viewModelScope.launch {
        if (args.editPersonID != -1) {
            editPersonId = args.editPersonID
            _formTitle.postValue("Edytuj osobę")
            val editPerson = personUseCases.getPersonById(args.editPersonID)
            personName.postValue(editPerson.name)
            selectedColorId.postValue(editPerson.colorId)
        } else {
            personName.postValue(null)
            selectedColorId.postValue(personColorIdList[0])
        }
    }

    //todo napisac mapper do tych wszystkich mapowań, nie robić w viewModelach ani w modelach
    fun savePerson(): Boolean {
        if (isFormValid()) {
            val personId = editPersonId
            val inputData = PersonInputData(
                name = personName.value!!,
                colorId = selectedColorId.value!!
            )
            if (personId == null) {
                GlobalScope.launch {
                    personUseCases.addNewPerson(inputData)
                    serverConnectionUseCases.enqueueServerSync()
                }
            } else {
                GlobalScope.launch {
                    personUseCases.updatePerson(personId, inputData)
                }
            }
            return true
        }
        return false
    }

    private fun isFormValid(): Boolean {
        val name = personName.value

        val nameError = if (name.isNullOrEmpty()) {
            "Pole jest wymagane"
        } else null

        personName.postValue(nameError)

        return nameError == null
    }
}