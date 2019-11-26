package com.maruchin.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.Person
import com.maruchin.medihelper.domain.entities.PersonInputData
import com.maruchin.medihelper.domain.repositories.PersonRepo

class PersonUseCases(
    private val personRepo: PersonRepo
) {
    private val currPersonId = MediatorLiveData<Int>()
    private val currPerson: LiveData<Person>

    init {
        currPersonId.addSource(personRepo.getMainIdLive()) { currPersonId.value = it }
        currPerson = Transformations.switchMap(currPersonId) { personId ->
            personId?.let { personRepo.getLiveById(it)  }
        }
    }

    suspend fun addNewPerson(inputData: PersonInputData) {
        val newPerson = Person(
            personId = 0,
            name = inputData.name,
            colorId = inputData.colorId,
            mainPerson = false,
            connectionKey = null
        )
        personRepo.insert(newPerson)
    }

    suspend fun addMainPerson(personName: String) {
        val mainPerson = Person(
            personId = 0,
            name = personName,
            colorId = R.color.colorPrimary,
            mainPerson = true,
            connectionKey = null
        )
        personRepo.insert(mainPerson)
    }

    suspend fun updatePerson(id: Int, inputData: PersonInputData) {
        val existingPerson = personRepo.getById(id)
        val updatedPerson = existingPerson.copy(
            name = inputData.name,
            colorId = inputData.colorId
        )
        personRepo.update(updatedPerson)
    }


    suspend fun updateMainPerson(personName: String) {
        personRepo.getMain()?.let { mainPerson ->
            val updatedMain = mainPerson.copy(name = personName)
            personRepo.update(updatedMain)
        }
    }

    suspend fun deletePersonById(id: Int) = personRepo.deleteById(id)

    suspend fun getPersonById(id: Int): Person = personRepo.getById(id)

    suspend fun mainPersonExists(): Boolean = personRepo.getMainId() != null

    fun getPersonLiveById(id: Int) = personRepo.getLiveById(id)

    fun getAllPersonListLive(): LiveData<List<Person>> = personRepo.getAllListLive()

    fun getMainPersonColorLive(): LiveData<Int> = personRepo.getMainPersonColorIdLive()

    fun getPersonListLiveByMedicineId(id: Int): LiveData<List<Person>> = personRepo.getListLiveByMedicineId(id)

    fun getCurrPersonLive() = currPerson

    fun getColorIdList() = personRepo.getColorIdList()

    fun selectCurrPerson(id: Int) = currPersonId.postValue(id)
}