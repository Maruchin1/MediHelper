package com.example.medihelper.mainapp.persons

import androidx.lifecycle.ViewModel
import com.example.medihelper.localdatabase.pojo.PersonItem
import com.example.medihelper.service.PersonService

class PersonViewModel(
    private val personService: PersonService
) : ViewModel() {

    val personItemListLive = personService.getItemListLive()

    fun selectPerson(personID: Int) = personService.selectCurrPerson(personID)

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