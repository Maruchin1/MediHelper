package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.R
import com.maruchin.medihelper.data.local.DeletedHistory
import com.maruchin.medihelper.data.local.SharedPref
import com.maruchin.medihelper.data.local.dao.PersonDao
import com.maruchin.medihelper.data.local.model.PersonEntity
import com.maruchin.medihelper.domain.entities.Person
import com.maruchin.medihelper.domain.repositories.PersonRepo

class PersonRepoImpl(
    private val personDao: PersonDao,
    private val sharedPref: SharedPref,
    private val deletedHistory: DeletedHistory
) : PersonRepo {

    init {
        checkDefaultColorIdList()
    }

    override suspend fun insert(person: Person) {
        val newEntity = PersonEntity(person = person)
        personDao.insert(newEntity)
    }

    override suspend fun update(person: Person) {
        val existingEntity = personDao.getById(person.personId)
        existingEntity.update(person)
        personDao.update(existingEntity)
    }

    override suspend fun deleteById(id: Int) {
        val remoteId = personDao.getRemoteIdById(id)
        if (remoteId != null) {
            deletedHistory.addToPersonHistory(remoteId)
        }
        personDao.deleteById(id)
    }

    override suspend fun getById(id: Int): Person {
        val personEntity = personDao.getById(id)
        return personEntity.toPerson()
    }

    override suspend fun getMain(): Person? {
        val entity = personDao.getMain()
        return entity?.toPerson()
    }

    override suspend fun getMainId(): Int? {
        return personDao.getMainId()
    }

    override fun getLiveById(id: Int): LiveData<Person> {
        val personEntityLive = personDao.getLiveById(id)
        return Transformations.map(personEntityLive) { it?.toPerson() }
    }

    override fun getMainLive(): LiveData<Person> {
        val mainPersonEntityLive = personDao.getMainLive()
        return Transformations.map(mainPersonEntityLive) { it?.toPerson() }
    }

    override fun getMainIdLive(): LiveData<Int> {
        return personDao.getMainIdLive()
    }

    override fun getMainPersonColorIdLive(): LiveData<Int> {
        return personDao.getMainColorIdLive()
    }

    override fun getAllListLive(): LiveData<List<Person>> {
        val entityList = personDao.getAllListLive()
        return Transformations.map(entityList) { list ->
            list.map { it.toPerson() }
        }
    }

    override fun getListLiveByMedicineId(id: Int): LiveData<List<Person>> {
        val entityList = personDao.getListLiveByMedicineId(id)
        return Transformations.map(entityList) { list ->
            list.map { it.toPerson() }
        }
    }

    override fun getColorIdList(): List<Int> {
        return sharedPref.getPersonColorIdList()
    }

    private fun checkDefaultColorIdList() = with(sharedPref) {
        if (getPersonColorIdList().isNullOrEmpty()) {
            savePersonColorIdList(getDefaultColorIdList())
        }
    }

    private fun getDefaultColorIdList() = listOf(
        R.color.colorPersonBlue,
        R.color.colorPersonBrown,
        R.color.colorPersonCyan,
        R.color.colorPersonGray,
        R.color.colorPersonLightGreen,
        R.color.colorPersonOrange,
        R.color.colorPersonPurple,
        R.color.colorPersonRed,
        R.color.colorPersonYellow
    )
}