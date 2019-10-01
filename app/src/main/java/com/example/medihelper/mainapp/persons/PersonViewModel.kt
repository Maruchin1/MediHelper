package com.example.medihelper.mainapp.persons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.services.PersonProfileService
import kotlinx.coroutines.launch

class PersonViewModel(
    private val personRepository: PersonRepository,
    private val personProfileService: PersonProfileService
) : ViewModel() {

    val personItemListLive = personRepository.getItemListLive()

    fun selectPerson(personID: Int) = personProfileService.selectCurrPerson(personID)

    fun getPersonItemDisplayData(personItem: PersonItem) = PersonItemDisplayData(
        personID = personItem.personID,
        personName = personItem.personName,
        personColorResID = personItem.personColorResID,
        isMainPerson = personItem.mainPerson
    )

    data class PersonItemDisplayData(
        val personID: Int,
        val personName: String,
        val personColorResID: Int,
        val isMainPerson: Boolean
    )
}