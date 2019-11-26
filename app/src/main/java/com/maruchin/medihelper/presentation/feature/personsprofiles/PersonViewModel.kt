package com.maruchin.medihelper.presentation.feature.personsprofiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.presentation.model.PersonItem

class PersonViewModel(
    private val personUseCases: PersonUseCases
) : ViewModel() {

    val personItemList: LiveData<List<PersonItem>> = MutableLiveData()

    private val profileList: LiveData<List<Profile>> = MutableLiveData()

    init {
//        profileList = personUseCases.getAllPersonListLive()
//        profileItemList = Transformations.map(profileList) { profileList ->
//            profileList.map { PersonItem(it) }
//        }
    }

//    fun selectPerson(id: Int) = personUseCases.selectCurrPerson(id)
}