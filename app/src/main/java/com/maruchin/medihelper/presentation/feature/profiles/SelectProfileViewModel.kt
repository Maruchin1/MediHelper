package com.maruchin.medihelper.presentation.feature.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.profile.GetLiveAllProfilesItemsUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile

class SelectProfileViewModel(
    private val getLiveAllProfilesItemsUseCase: GetLiveAllProfilesItemsUseCase,
    private val selectedProfile: SelectedProfile
) : ViewModel() {

    val profileItemList: LiveData<List<ProfileItem>>

    init {
        profileItemList = liveData {
            val source = getLiveAllProfilesItemsUseCase.execute()
            emitSource(source)
        }
    }

    fun selectProfile(profileId: String) = selectedProfile.setProfileId(profileId)
}