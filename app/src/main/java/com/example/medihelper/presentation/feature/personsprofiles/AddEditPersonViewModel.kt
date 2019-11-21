package com.example.medihelper.presentation.feature.personsprofiles

import androidx.lifecycle.*
import com.example.medihelper.presentation.framework.FieldMutableLiveData
import com.example.medihelper.domain.entities.PersonInputData
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.presentation.model.PersonColorCheckboxData
import com.example.medihelper.presentation.model.PersonForm
import com.example.medihelper.presentation.model.PersonFormError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditPersonViewModel(
    private val personUseCases: PersonUseCases
) : ViewModel() {

    val personColorCheckboxDataList: LiveData<List<PersonColorCheckboxData>>

    val formTitle: LiveData<String>
        get() = _formTitle
    val personForm: MutableLiveData<PersonForm>
        get() = _personForm
    val personFormError: LiveData<PersonFormError>
        get() = _personFormError

    private val _formTitle = MutableLiveData<String>("Dodaj osobę")
    private val _personFormError = FieldMutableLiveData<PersonFormError>()
    private var _personForm = FieldMutableLiveData<PersonForm>()

    private val personColorIdList: List<Int> = personUseCases.getColorIdList()
    private var editPersonId: Int? = null

    init {
        personColorCheckboxDataList = Transformations.map(_personForm) { form ->
            personColorIdList.map { colorId ->
                PersonColorCheckboxData(
                    colorId = colorId,
                    selected = colorId == form.colorId
                )
            }
        }
    }

    fun setArgs(args: AddEditPersonFragmentArgs) = viewModelScope.launch {
        if (args.editPersonID != -1) {
            editPersonId = args.editPersonID
            _formTitle.postValue("Edytuj osobę")
            val editPerson = personUseCases.getPersonById(args.editPersonID)
            val editPersonForm = PersonForm(_name = editPerson.name, _colorId = editPerson.colorId)
            _personForm.postValue(editPersonForm)
        } else {
            val cleanForm = PersonForm(_name = null, _colorId = personColorIdList[0])
            _personForm.postValue(cleanForm)
        }
    }

    //todo napisac mapper do tych wszystkich mapowań, nie robić w viewModelach ani w modelach
    fun savePerson(): Boolean {
        if (isFormValid()) {
            val personId = editPersonId
            val inputData = PersonInputData(
                name = personForm.value!!.name!!,
                colorId = personForm.value!!.colorId
            )
            if (personId == null) {
                GlobalScope.launch {
                    personUseCases.addNewPerson(inputData)
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
        val name = _personForm.value?.name

        val nameError = if (name.isNullOrEmpty()) {
            "Pole jest wymagane"
        } else null

        _personFormError.value?.errorName = nameError

        return nameError == null
    }
}