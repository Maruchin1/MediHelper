package com.maruchin.medihelper.presentation.feature.profiles

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.model.ProfileValidator
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorsUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileEditDataUseCase
import com.maruchin.medihelper.domain.usecases.profile.SaveProfileUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class AddEditProfileViewModel(
    private val getProfileColorsUseCase: GetProfileColorsUseCase,
    private val getProfileEditDataUseCase: GetProfileEditDataUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    val formTitle: LiveData<String>
    val profileName = MutableLiveData<String>()
    val selectedColor = MutableLiveData<String>()
    val colorCheckboxList: LiveData<List<ColorCheckbox>>

    val actionProfileSaved: LiveData<Boolean>
        get() = _actionProfileSaved
    val errorProfileName: LiveData<String>
        get() = _errorProfileName

    private val _actionProfileSaved = ActionLiveData()
    private val _errorProfileName = MutableLiveData<String>()

    private lateinit var profileColorList: List<String>
    private var editProfileId = MutableLiveData<String>()

    init {
        formTitle = Transformations.map(editProfileId) {
            if (it == null) {
                "Dodaj profil"
            } else {
                "Edytuj profil"
            }
        }
        viewModelScope.launch {
            profileColorList = getProfileColorsUseCase.execute()
            selectedColor.postValue(profileColorList[0])
        }
        colorCheckboxList = Transformations.map(selectedColor) { selectedColor ->
            profileColorList.map { color ->
                ColorCheckbox(
                    color = color,
                    selected = color == selectedColor
                )
            }
        }
    }

    fun setArgs(args: AddEditProfileFragmentArgs) = viewModelScope.launch {
        editProfileId.postValue(args.editProfileId)
        if (args.editProfileId != null) {
            val editData = getProfileEditDataUseCase.execute(args.editProfileId)
            if (editData != null) {
                profileName.postValue(editData.name)
                selectedColor.postValue(editData.color)
            }
        }
    }

    fun saveProfile() = viewModelScope.launch {
        val params = SaveProfileUseCase.Params(
            profileId = editProfileId.value,
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

    data class ColorCheckbox(
        val color: String,
        val selected: Boolean
    )
}