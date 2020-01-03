package com.maruchin.medihelper.presentation.feature.profiles

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.mediplans.GetLiveMedicinesPlansItemsByProfileUseCase
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetLiveAllProfilesItemsUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val selectedProfile: SelectedProfile,
    private val getLiveAllProfilesItemsUseCase: GetLiveAllProfilesItemsUseCase,
    private val getLiveMedicinesPlansItemsByProfileUseCase: GetLiveMedicinesPlansItemsByProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String> = selectedProfile.profileColorLive
    val profileItems: LiveData<List<ProfileItem>>
    val profileItemsAvailable: LiveData<Boolean>
    val selectedProfilePosition: LiveData<Int>
    val mainProfileSelected: LiveData<Boolean> = selectedProfile.mainProfileSelectedLive
    val medicinesPlans: LiveData<List<MedicinePlanItem>>
    val medicinesPlansLoaded: LiveData<Boolean>
    val noMedicinesPlans: LiveData<Boolean>
    val medicinesPlansAvailable: LiveData<Boolean>

    val selectedProfileId: String?
        get() = selectedProfile.profileId


    init {
        profileItems = liveData {
            val source = getLiveAllProfilesItemsUseCase.execute()
            emitSource(source)
        }
        profileItemsAvailable = Transformations.map(profileItems) { !it.isNullOrEmpty() }
        selectedProfilePosition = Transformations.switchMap(profileItems) { list ->
            Transformations.map(selectedProfile.profileIdLive) { selectedProfileId ->
                list.indexOfFirst { it.profileId == selectedProfileId }
            }
        }
        medicinesPlans = liveData {
            val source = Transformations.switchMap(selectedProfile.profileIdLive) { profileId ->
                liveData {
                    val source = getLiveMedicinesPlansItemsByProfileUseCase.execute(profileId)
                    emitSource(source)
                }
            }
            emitSource(source)
        }
        medicinesPlansLoaded = Transformations.map(medicinesPlans) { it != null }
        noMedicinesPlans = Transformations.map(medicinesPlans) { it.isEmpty() }
        medicinesPlansAvailable = Transformations.map(medicinesPlans) { it.isNotEmpty() }
    }

    fun deleteProfile() = viewModelScope.launch {
        val selectedProfileId = selectedProfile.profileId
        selectedProfile.setMain()
        if (selectedProfileId != null) {
            deleteProfileUseCase.execute(selectedProfileId)
        }
    }

    fun selectProfile(position: Int) {
        val profileItem = profileItems.value?.get(position)
        if (profileItem != null) {
            selectedProfile.setProfileId(profileItem.profileId)
        }
    }
}