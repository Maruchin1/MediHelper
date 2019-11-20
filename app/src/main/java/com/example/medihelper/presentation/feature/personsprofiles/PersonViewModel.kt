package com.example.medihelper.presentation.feature.personsprofiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.domain.entities.Person
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.presentation.model.PersonItem

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