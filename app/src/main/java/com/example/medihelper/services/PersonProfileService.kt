package com.example.medihelper.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.PersonRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonProfileService(
    private val personRepository: PersonRepository
) {
    private val currPersonIDLive = MediatorLiveData<Int>()
    private val currPersonItemLive: LiveData<PersonItem>

    init {
        currPersonItemLive = Transformations.switchMap(currPersonIDLive) { personID ->
            personRepository.getItemLive(personID)
        }
        currPersonIDLive.apply {
            addSource(personRepository.getMainPersonIDLive()) { mainPersonID ->
                currPersonIDLive.value = mainPersonID
            }
        }
    }

    fun getCurrPersonItemLive() = currPersonItemLive

    fun selectCurrPerson(personID: Int) = currPersonIDLive.postValue(personID)

    fun selectMainPersonAsCurrent() = GlobalScope.launch {
        currPersonIDLive.postValue(personRepository.getMainPersonID())
    }
}