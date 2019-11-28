package com.maruchin.medihelper.presentation.feature.profiles

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorsUseCase
import com.maruchin.medihelper.presentation.model.ProfileColorCheckbox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditPersonViewModel(
    private val getProfileColorsUseCase: GetProfileColorsUseCase
) : ViewModel() {

    val personName = MutableLiveData<String>()
    val selectedColor = MutableLiveData<String>()
    val profileColorCheckboxList: LiveData<List<ProfileColorCheckbox>>

    val formTitle: LiveData<String>
        get() = _formTitle
    val errorProfileName: LiveData<String>
        get() = _errorProfileName

    private val _formTitle = MutableLiveData<String>("Dodaj osobę")
    private val _errorProfileName = MutableLiveData<String>()

    private lateinit var profileColorList: List<String>
    private var editPersonId: Int? = null

    init {
        viewModelScope.launch {
            profileColorList = getProfileColorsUseCase.execute()
            selectedColor.postValue(profileColorList[0])
        }
        profileColorCheckboxList = Transformations.map(selectedColor) { selectedColor ->
            profileColorList.map { color ->
                ProfileColorCheckbox(
                    color = color,
                    selected = color == selectedColor
                )
            }
        }
    }

    fun setArgs(args: AddEditPersonFragmentArgs) = viewModelScope.launch {
        if (args.editPersonID != -1) {
            editPersonId = args.editPersonID
            _formTitle.postValue("Edytuj osobę")
//            val editPerson = personUseCases.getPersonById(args.editPersonID)
//            personName.postValue(editPerson.name)
//            selectedColorId.postValue(editPerson.color)
        }
    }

    //todo napisac mapper do tych wszystkich mapowań, nie robić w viewModelach ani w modelach
    fun savePerson(): Boolean {
        if (isFormValid()) {
            val personId = editPersonId
//            val inputData = PersonInputData(
//                name = personName.value!!,
//                color = selectedColorId.value!!
//            )
            if (personId == null) {
                GlobalScope.launch {
//                    personUseCases.addNewPerson(inputData)
                }
            } else {
                GlobalScope.launch {
//                    personUseCases.updatePerson(profileId, inputData)
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