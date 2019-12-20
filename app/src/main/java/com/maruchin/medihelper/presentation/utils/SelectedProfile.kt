package com.maruchin.medihelper.presentation.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.usecases.profile.GetMainProfileIdUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorUseCase
import kotlinx.coroutines.*

class SelectedProfile(
    private val getMainProfileIdUseCase: GetMainProfileIdUseCase,
    private val getProfileColorUseCase: GetProfileColorUseCase
) {
    val mainProfileSelectedLive: LiveData<Boolean>
    val profileColorLive: LiveData<String?>

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
        profileColorLive = Transformations.switchMap(_profileIdLive) {
            liveData {
                val color = getProfileColorUseCase.execute(it)
                emit(color)
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