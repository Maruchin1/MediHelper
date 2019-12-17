package com.maruchin.medihelper.presentation.feature.calendar

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

    val selectedProfileId: String?
        get() = selectedProfile.profileId
    val profileItems: LiveData<List<ProfileItem>>
    val selectedProfilePosition: LiveData<Int>
    val mainProfileSelected: LiveData<Boolean> = selectedProfile.mainProfileSelectedLive
    val medicinesPlans: LiveData<List<MedicinePlanItem>>

    init {
        profileItems = liveData {
            val source = getLiveAllProfilesItemsUseCase.execute()
            emitSource(source)
        }
        selectedProfilePosition = Transformations.map(profileItems) { list ->
            val selectedProfileId = selectedProfile.profileId
            return@map list.indexOfFirst { it.profileId == selectedProfileId }
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