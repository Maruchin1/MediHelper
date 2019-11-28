package com.maruchin.medihelper.presentation.feature.profiles

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.model.ProfileValidator
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorsUseCase
import com.maruchin.medihelper.domain.usecases.profile.SaveProfileUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.model.ProfileColorCheckbox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditPersonViewModel(
    private val getProfileColorsUseCase: GetProfileColorsUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    val profileName = MutableLiveData<String>()
    val selectedColor = MutableLiveData<String>()
    val profileColorCheckboxList: LiveData<List<ProfileColorCheckbox>>

    val formTitle: LiveData<String>
        get() = _formTitle
    val actionProfileSaved: LiveData<Boolean>
        get() = _actionProfileSaved
    val errorProfileName: LiveData<String>
        get() = _errorProfileName

    private val _formTitle = MutableLiveData<String>("Dodaj osobę")
    private val _actionProfileSaved = ActionLiveData()
    private val _errorProfileName = MutableLiveData<String>()

    private lateinit var profileColorList: List<String>
    private var editProfileId: String? = null

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
//            editProfileId = args.editPersonID
            _formTitle.postValue("Edytuj osobę")
//            val editPerson = personUseCases.getPersonById(args.editPersonID)
//            profileName.postValue(editPerson.name)
//            selectedColorId.postValue(editPerson.color)
        }
    }

    fun savePerson() = viewModelScope.launch {
        val params = SaveProfileUseCase.Params(
            profileId = editProfileId,
            name = profileName.value,
            color = selectedColor.value
        )
        val validator = saveProfileUseCase.execute(params)
        if (validator.noErrors) {
            _actionProfileSaved.sendAction()
        } else {
            postErrors(validator)
        }
    }

    private fun postErrors(validator: ProfileValidator) {
        val profileNameError = if (validator.emptyName) {
            "Podanie nazwy jest wyamgane"
        } else null

        _errorProfileName.postValue(profileNameError)
    }
}