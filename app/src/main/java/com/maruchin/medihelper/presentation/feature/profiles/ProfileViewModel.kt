package com.maruchin.medihelper.presentation.feature.profiles

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.mediplans.GetLiveMedicinesPlansItemsByProfileUseCase
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetLiveAllProfilesItemsUseCase
import com.maruchin.medihelper.presentation.model.MedicinePlanItemData
import com.maruchin.medihelper.presentation.model.ProfileItemData
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val selectedProfile: SelectedProfile,
    private val getLiveAllProfilesItemsUseCase: GetLiveAllProfilesItemsUseCase,
    private val getLiveMedicinesPlansItemsByProfileUseCase: GetLiveMedicinesPlansItemsByProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase
) : ViewModel() {

    val colorPrimary: LiveData<String> = selectedProfile.profileColorLive

    val profileItems: LiveData<List<ProfileItemData>>
    val selectedProfilePosition: LiveData<Int>
    val mainProfileSelected: LiveData<Boolean> = selectedProfile.mainProfileSelectedLive

    val medicinesPlans: LiveData<List<MedicinePlanItemData>>
    val medicinesPlansLoaded: LiveData<Boolean>
    val noMedicinesPlansForProfile: LiveData<Boolean>
    val medicinesPlansAvailable: LiveData<Boolean>

    val selectedProfileId: String?
        get() = selectedProfile.profileId


    init {
        profileItems = getLiveProfileItemsData()
        selectedProfilePosition = getLiveSelectedProfilePosition()

        medicinesPlans = getLivePlansBySelectedProfile()
        medicinesPlansLoaded = getLiveMedicinesPlansLoaded()
        noMedicinesPlansForProfile = getLiveNoPlansForProfile()
        medicinesPlansAvailable = getLivePlansAvailable()
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

    private fun getLiveProfileItemsData(): LiveData<List<ProfileItemData>> {
        return liveData {
            val profileItemsLive = getLiveAllProfilesItemsUseCase.execute()
            val dataLive = transformLiveProfileItemsToData(profileItemsLive)
            emitSource(dataLive)
        }
    }

    private fun transformLiveProfileItemsToData(
        profileItemsLive: LiveData<List<ProfileItem>>
    ): LiveData<List<ProfileItemData>> {
        return Transformations.map(profileItemsLive) { profileItems ->
            mapProfileItemsToData(profileItems)
        }
    }

    private fun mapProfileItemsToData(profileItems: List<ProfileItem>): List<ProfileItemData> {
        return profileItems.map { profileItem ->
            ProfileItemData.fromDomainModel(profileItem)
        }
    }

    private fun getLiveSelectedProfilePosition(): LiveData<Int> {
        return Transformations.switchMap(profileItems) { profiles ->
            Transformations.map(selectedProfile.profileIdLive) { selectedProfileId ->
                getProfilePosition(profiles, selectedProfileId)
            }
        }
    }

    private fun getProfilePosition(profiles: List<ProfileItemData>, profileId: String): Int {
        return profiles.indexOfFirst { profile ->
            profile.profileId == profileId
        }
    }

    private fun getLivePlansBySelectedProfile(): LiveData<List<MedicinePlanItemData>> {
        return Transformations.switchMap(selectedProfile.profileIdLive) { selectedProfileId ->
            liveData {
                val itemsLive = getLiveMedicinesPlansItemsByProfileUseCase.execute(selectedProfileId)
                val dataLive = mapLivePlansItemsToData(itemsLive)
                emitSource(dataLive)
            }
        }
    }

    private fun mapLivePlansItemsToData(
        plansLive: LiveData<List<MedicinePlanItem>>
    ): LiveData<List<MedicinePlanItemData>> {
        return Transformations.map(plansLive) { plans ->
            mapPlansItemsToData(plans)
        }
    }

    private fun mapPlansItemsToData(plans: List<MedicinePlanItem>): List<MedicinePlanItemData> {
        return plans.map { plan ->
            MedicinePlanItemData.fromDomainModel(plan)
        }
    }

    private fun getLiveMedicinesPlansLoaded(): LiveData<Boolean> {
        return Transformations.map(medicinesPlans) { plans ->
            plans != null
        }
    }

    private fun getLiveNoPlansForProfile(): LiveData<Boolean> {
        return Transformations.map(medicinesPlans) { plans ->
            plans.isEmpty()
        }
    }

    private fun getLivePlansAvailable(): LiveData<Boolean> {
        return Transformations.map(medicinesPlans) { plans ->
            plans.isNotEmpty()
        }
    }
}