package com.example.medihelper.mainapp.family

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.PersonEntity

class AddPersonViewModel : ViewModel() {

    val personColorDisplayDataListLive: LiveData<List<PersonColorDisplayData>>

    val personNameLive = MutableLiveData<String>()
    val genderLive = MutableLiveData<PersonEntity.Gender>()
    val personColorResIDLive = MutableLiveData<Int>()

    private val personColorResIDList = AppRepository.getPersonColorResIDList()

    init {
        personColorDisplayDataListLive = Transformations.map(personColorResIDLive) { selectedPersonColorResID ->
            mutableListOf<PersonColorDisplayData>().apply {
                personColorResIDList.forEach { personColorResID ->
                    this.add(
                        PersonColorDisplayData(
                            colorResID = personColorResID,
                            selected = personColorResID == selectedPersonColorResID
                        )
                    )
                }
            }
        }
        personColorResIDLive.value = personColorResIDList[0]
    }

    fun saveNewPerson() {
        //todo zrobić walidację
        val newPerson = PersonEntity(
            personName = personNameLive.value!!,
            gender = genderLive.value!!,
            personColorResID = personColorResIDLive.value!!
        )
        AppRepository.insertPerson(newPerson)
    }

    data class PersonColorDisplayData(
        val colorResID: Int,
        val selected: Boolean
    )
}
