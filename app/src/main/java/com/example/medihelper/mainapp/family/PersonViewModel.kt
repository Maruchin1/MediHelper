package com.example.medihelper.mainapp.family

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.pojos.PersonItem

class PersonViewModel : ViewModel() {

    val personItemListLive = AppRepository.getPersonItemListLive()
    val optionsEnabledPersonIDLive = MutableLiveData<Int>()

    fun selectPerson(personID: Int) = AppRepository.setSelectedPerson(personID)

    fun deletePerson(personID: Int) {
        if (personID == AppRepository.getSelectedPersonItemLive().value?.personID) {
            AppRepository.setSelectedPerson(AppRepository.getMainPersonID())
        }
        AppRepository.deletePerson(personID)
    }

    fun getPersonItemDisplayData(personItem: PersonItem) = PersonItemDisplayData(
        personID = personItem.personID,
        personName = personItem.personName,
        personColorResID = personItem.personColorResID,
        isMainPerson = personItem.personID == AppRepository.getMainPersonID(),
        optionsEnabled = false
    )

    data class PersonItemDisplayData(
        val personID: Int,
        val personName: String,
        val personColorResID: Int,
        val isMainPerson: Boolean,
        var optionsEnabled: Boolean
    )
}