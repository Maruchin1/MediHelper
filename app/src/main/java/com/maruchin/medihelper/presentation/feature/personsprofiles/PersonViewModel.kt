package com.maruchin.medihelper.presentation.feature.personsprofiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.domain.entities.Person
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.presentation.model.PersonItem

class PersonViewModel(
    private val personUseCases: PersonUseCases
) : ViewModel() {

    val personItemList: LiveData<List<PersonItem>>

    private val personList: LiveData<List<Person>>

    init {
        personList = personUseCases.getAllPersonListLive()
        personItemList = Transformations.map(personList) { personList ->
            personList.map { PersonItem(it) }
        }
    }

    fun selectPerson(id: Int) = personUseCases.selectCurrPerson(id)
}