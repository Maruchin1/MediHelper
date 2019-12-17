package com.maruchin.medihelper.presentation.feature.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.mediplans.GetLiveMedicinesPlansItemsByProfileUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetLiveAllProfilesItemsUseCase
import com.maruchin.medihelper.presentation.utils.SelectedProfile

class ProfileViewModel(
    private val getLiveAllProfilesItemsUseCase: GetLiveAllProfilesItemsUseCase,
    private val selectedProfile: SelectedProfile,
    private val getLiveMedicinesPlansItemsByProfileUseCase: GetLiveMedicinesPlansItemsByProfileUseCase
    ) : ViewModel() {

    val selectedProfileId: String?
        get() = selectedProfile.profileId
    val profileItems: LiveData<List<ProfileItem>>
    val selectedProfilePosition: LiveData<Int>
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

    fun selectProfile(position: Int) {
        val profileItem = profileItems.value?.get(position)
        if (profileItem != null) {
            selectedProfile.setProfileId(profileItem.profileId)
        }
    }
}