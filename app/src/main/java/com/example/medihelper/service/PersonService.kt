package com.example.medihelper.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.DeletedHistory
import com.example.medihelper.localdata.dao.PersonDao
import com.example.medihelper.data.local.model.PersonEntity
import com.example.medihelper.localdata.pojo.PersonEditData
import com.example.medihelper.localdata.pojo.PersonItem
import com.example.medihelper.localdata.pojo.PersonOptionsData

interface PersonService {
    suspend fun save(editData: PersonEditData)
    suspend fun delete(id: Int)
    suspend fun getItem(id: Int): PersonItem
    suspend fun getEditData(id: Int): PersonEditData
    suspend fun hasMainPerson(): Boolean
    fun getMainPersonItemLive(): LiveData<PersonItem>
    fun getMainPersonColorLive(): LiveData<Int>
    fun getOptionsDataLive(id: Int): LiveData<PersonOptionsData>
    fun getItemListLive(): LiveData<List<PersonItem>>
    fun getItemListLiveByMedicineID(id: Int): LiveData<List<PersonItem>>
    fun getCurrPersonItemLive(): LiveData<PersonItem>
    fun selectCurrPerson(id: Int)
    fun getColorResIdList(): List<Int>
    fun saveColorResIdList(newList: List<Int>)
}

class PersonServiceImpl(
    private val personDao: PersonDao,
    private val appSharedPref: AppSharedPref,
    private val deletedHistory: DeletedHistory
) : PersonService {

    private val currPersonIdLive = MediatorLiveData<Int>()
    private val currPersonItemLive: LiveData<PersonItem>

    init {
        currPersonIdLive.addSource(personDao.getMainIdLive()) {currPersonIdLive.value = it}
        currPersonItemLive = Transformations.switchMap(currPersonIdLive) {
            personDao.getItemLive(it)
        }
    }

    override suspend fun save(editData: PersonEditData) {
        if (editData.personId == 0) {
            val newEntity = PersonEntity(
                personName = editData.personName,
                personColorResId = editData.personColorResId
            )
            personDao.insert(newEntity)
        } else {
            val existingEntity = personDao.getEntity(editData.personId)
            val updatedEntity = existingEntity.copy(
                personName = editData.personName,
                personColorResId = editData.personColorResId,
                synchronizedWithServer = false
            )
            personDao.update(updatedEntity)
        }
    }

    override suspend fun delete(id: Int) {
        personDao.getRemoteIdById(id)?.let { deletedHistory.addToPersonHistory(it) }
        personDao.delete(id)
    }

    override suspend fun getItem(id: Int) = personDao.getItem(id)

    override suspend fun getEditData(id: Int) = personDao.getEditData(id)

    override suspend fun hasMainPerson(): Boolean {
        return personDao.getMainPersonId() != null
    }

    override fun getMainPersonItemLive() = personDao.getMainItemLive()

    override fun getMainPersonColorLive() = personDao.getMainColorLive()

    override fun getOptionsDataLive(id: Int) = personDao.getOptionsDataLive(id)

    override fun getItemListLive() = personDao.getItemListLive()

    override fun getItemListLiveByMedicineID(id: Int) = personDao.getItemListLiveByMedicineID(id)

    override fun getCurrPersonItemLive() = currPersonItemLive

    override fun selectCurrPerson(id: Int) = currPersonIdLive.postValue(id)

    override fun getColorResIdList() = appSharedPref.getColorResIdList()

    override fun saveColorResIdList(newList: List<Int>) = appSharedPref.saveColorResIdList(newList)
}