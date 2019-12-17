package com.maruchin.medihelper.presentation.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.usecases.profile.GetMainProfileIdUseCase
import kotlinx.coroutines.*

class SelectedProfile(
    private val getMainProfileIdUseCase: GetMainProfileIdUseCase
) {

    val mainProfileSelectedLive: LiveData<Boolean>
    val profileId: String?
        get() = _profileIdLive.value
    val profileIdLive: LiveData<String>
        get() = _profileIdLive

    private val _profileIdLive = MediatorLiveData<String>()

    init {
        mainProfileSelectedLive = Transformations.switchMap(_profileIdLive) {
            liveData {
                val mainSelected = it == getMainProfileIdUseCase.execute()
                emit(mainSelected)
            }
        }
        GlobalScope.launch {
            setMain()
        }
    }

    fun setProfileId(profileId: String) {
        _profileIdLive.value = profileId
    }

    suspend fun setMain() {
        val mainProfileId = getMainProfileIdUseCase.execute()
        _profileIdLive.postValue(mainProfileId)
    }
}