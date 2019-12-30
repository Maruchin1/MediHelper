package com.maruchin.medihelper.domain.usecases.profile

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.model.ProfileItem

interface GetLiveAllProfilesItemsUseCase {

    suspend fun execute(): LiveData<List<ProfileItem>>
}