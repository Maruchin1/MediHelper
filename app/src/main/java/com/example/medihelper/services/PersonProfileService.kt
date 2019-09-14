package com.example.medihelper.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.PersonRepository

class PersonProfileService(
    private val personRepository: PersonRepository,
    private val sharedPrefService: SharedPrefService
) {

    private val currPersonIDLive = MutableLiveData<Int>()
    private val currPersonItemLive: LiveData<PersonItem>

    init {
        currPersonItemLive = Transformations.switchMap(currPersonIDLive) { personID ->
            personRepository.getItemLive(personID)
        }
        currPersonIDLive.postValue(sharedPrefService.getMainPersonID())
    }

    fun getCurrPersonItemLive() = currPersonItemLive

    fun selectCurrPerson(personID: Int) = currPersonIDLive.postValue(personID)

    fun selectMainPerson() = currPersonIDLive.postValue(sharedPrefService.getMainPersonID())

    fun isMainPerson(personID: Int): Boolean {
        return personID == sharedPrefService.getMainPersonID()
    }
}