package com.maruchin.medihelper.presentation.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.maruchin.medihelper.domain.usecases.profile.GetMainProfileIdUseCase
import kotlinx.coroutines.*

class SelectedProfile(
    private val getMainProfileIdUseCase: GetMainProfileIdUseCase
) {

    val profileIdLive: LiveData<String>
        get() = _profileIdLive

    private val _profileIdLive = MediatorLiveData<String>()

    init {
        GlobalScope.launch {
            val mainProfileId = getMainProfileIdUseCase.execute()
            _profileIdLive.postValue(mainProfileId)
        }
    }

    fun setProfileId(profileId: String) {
        _profileIdLive.value = profileId
    }
}