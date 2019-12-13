package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.profile.GetAllProfilesItemsLiveUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile

class ProfileViewModel(
    private val getAllProfilesItemsLiveUseCase: GetAllProfilesItemsLiveUseCase,
    private val selectedProfile: SelectedProfile
) : ViewModel() {

    val profileItems: LiveData<List<ProfileItem>>
    val selectedProfilePosition: LiveData<Int>

    init {
        profileItems = liveData {
            val source = getAllProfilesItemsLiveUseCase.execute()
            emitSource(source)
        }
        selectedProfilePosition = Transformations.map(profileItems) { list ->
            val selectedProfileId = selectedProfile.profileId
            return@map list.indexOfFirst { it.profileId == selectedProfileId }
        }
    }

    fun selectProfile(position: Int) {
        val profileItem = profileItems.value?.get(position)
        if (profileItem != null) {
            selectedProfile.setProfileId(profileItem.profileId)
        }
    }
}