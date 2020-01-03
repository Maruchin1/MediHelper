package com.maruchin.medihelper.presentation.feature.profiles

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.model.ProfileEditData
import com.maruchin.medihelper.domain.model.ProfileErrors
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorsUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileEditDataUseCase
import com.maruchin.medihelper.domain.usecases.profile.SaveProfileUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.presentation.model.ColorCheckboxData
import kotlinx.coroutines.launch

class AddEditProfileViewModel(
    private val getProfileColorsUseCase: GetProfileColorsUseCase,
    private val getProfileEditDataUseCase: GetProfileEditDataUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    val formTitle: LiveData<String>
    val colorCheckboxDataList: LiveData<List<ColorCheckboxData>>
    val profileName = MutableLiveData<String>()
    val selectedColor = MutableLiveData<String>()

    val actionProfileSaved: LiveData<Boolean>
        get() = _actionProfileSaved
    val errorProfileName: LiveData<String>
        get() = _errorProfileName

    private val _actionProfileSaved = ActionLiveData()
    private val _errorProfileName = MutableLiveData<String>()

    private lateinit var profileColorList: List<String>
    private var editProfileId = MutableLiveData<String>()

    init {
        formTitle = getLiveFormTitle()
        colorCheckboxDataList = getLiveCheckboxList()
        viewModelScope.launch {
            loadProfileColors()
            setDefaultProfileColor()
        }
    }

    fun initViewModel(editProfileId: String?) = viewModelScope.launch {
        if (editProfileId != null) {
            loadAndSetEditData(editProfileId)
        }
    }

    fun saveProfile() = viewModelScope.launch {
        val params = SaveProfileUseCase.Params(
            profileId = editProfileId.value,
            name = profileName.value,
            color = selectedColor.value
        )
        val errors = saveProfileUseCase.execute(params)
        if (errors.noErrors) {
            _actionProfileSaved.sendAction()
        } else {
            postErrors(errors)
        }
    }

    private fun getLiveFormTitle(): LiveData<String> {
        return Transformations.map(editProfileId) {
            if (it == null) {
                "Dodaj profil"
            } else {
                "Edytuj profil"
            }
        }
    }

    private fun getLiveCheckboxList(): LiveData<List<ColorCheckboxData>> {
        return Transformations.map(selectedColor) { selectedColor ->
            profileColorList.map { color ->
                ColorCheckboxData(
                    color = color,
                    selected = color == selectedColor
                )
            }
        }
    }

    private suspend fun loadProfileColors() {
        profileColorList = getProfileColorsUseCase.execute()
    }

    private fun setDefaultProfileColor() {
        selectedColor.postValue(profileColorList[0])
    }

    private suspend fun loadAndSetEditData(profileId: String) {
        editProfileId.postValue(profileId)
        val editData = getProfileEditDataUseCase.execute(profileId)
        setEditData(editData)
    }

    private fun setEditData(editData: ProfileEditData) {
        profileName.postValue(editData.name)
        selectedColor.postValue(editData.color)
    }

    private fun postErrors(errors: ProfileErrors) {
        val profileNameError = if (errors.emptyName) {
            "Podanie nazwy jest wyamgane"
        } else null

        _errorProfileName.postValue(profileNameError)
    }
}