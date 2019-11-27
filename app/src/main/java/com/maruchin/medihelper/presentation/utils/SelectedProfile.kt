package com.maruchin.medihelper.presentation.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class SelectedProfile {

    val profileIdLive: LiveData<String>
        get() = _profileIdLive

    private val _profileIdLive = MediatorLiveData<String>()


    fun setProfileId(profileId: String) {
        _profileIdLive.value = profileId
    }
}