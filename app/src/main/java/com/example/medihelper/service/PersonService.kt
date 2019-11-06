package com.example.medihelper.service

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.localdatabase.DeletedHistory
import com.example.medihelper.localdatabase.dao.PersonDao
import com.example.medihelper.localdatabase.entity.PersonEntity
import com.example.medihelper.localdatabase.pojo.PersonEditData
import com.example.medihelper.localdatabase.pojo.PersonItem
import com.example.medihelper.localdatabase.pojo.PersonOptionsData

interface PersonService {
    suspend fun save(editData: PersonEditData)
    suspend fun delete(id: Int)
    suspend fun getEntity(id: Int): PersonEntity
    suspend fun getEntityList(): List<PersonEntity>
    suspend fun getItem(id: Int): PersonItem
    suspend fun getEditData(id: Int): PersonEditData
    suspend fun getMainPersonID(): Int?
    suspend fun getMainPersonEntity(): PersonEntity
    fun getItemLive(id: Int): LiveData<PersonItem>
    fun getOptionsDataLive(id: Int): LiveData<PersonOptionsData>
    fun getItemListLive(): LiveData<List<PersonItem>>
    fun getItemListLiveByMedicineID(id: Int): LiveData<List<PersonItem>>
    fun getMainPersonIDLive(): LiveData<Int>
    fun getMainPersonItemLive(): LiveData<PersonItem>
    fun getMainPersonColorLive(): LiveData<Int>
    fun getCurrPersonItemLive(): LiveData<PersonItem>
    fun selectCurrPerson(id: Int)
    fun getColorResIdList(): List<Int>
    fun saveColorResIdList(newList: List<Int>)
}

class PersonServiceImpl(
    private val personDao: PersonDao,
    private val sharedPreferences: SharedPreferences,
    private val deletedHistory: DeletedHistory
) : PersonService {

    companion object {
        private const val KEY_PERSON_COLOR_RES_ID_SET = "key-person-color-res-id-set"
    }

    private val currPersonIDLive = MediatorLiveData<Int>()
    private val currPersonItemLive: LiveData<PersonItem>

    init {
        currPersonIDLive.addSource(personDao.getMainPersonIDLive()) { mainPersonID ->
            currPersonIDLive.postValue(mainPersonID)
        }
        currPersonItemLive = Transformations.switchMap(currPersonIDLive) { personID ->
            personID?.let { personDao.getItemLive(it) }
        }
    }

    override suspend fun save(editData: PersonEditData) {
        val entity = PersonEntity(
            personID = editData.personID,
            personName = editData.personName,
            personColorResID = editData.personColorResID,
            synchronizedWithServer = false
        )
        if (entity.personID == 0) {
            personDao.insert(entity).toInt()
        } else {
            personDao.update(entity)
        }
    }

    override suspend fun delete(id: Int) {
        personDao.getRemoteIdById(id)?.let { deletedHistory.addToPersonHistory(it) }
        personDao.delete(id)
    }

    override suspend fun getEntity(id: Int) = personDao.getEntity(id)

    override suspend fun getEntityList() = personDao.getEntityList()

    override suspend fun getItem(id: Int) = personDao.getItem(id)

    override suspend fun getEditData(id: Int) = personDao.getEditData(id)

    override suspend fun getMainPersonID() = personDao.getMainPersonID()

    override suspend fun getMainPersonEntity() = personDao.getMainPersonEntity()

    override fun getItemLive(id: Int) = personDao.getItemLive(id)

    override fun getOptionsDataLive(id: Int) = personDao.getOptionsDataLive(id)

    override fun getItemListLive() = personDao.getItemListLive()

    override fun getItemListLiveByMedicineID(id: Int) = personDao.getItemListLiveByMedicineID(id)

    override fun getMainPersonIDLive() = personDao.getMainPersonIDLive()

    override fun getMainPersonItemLive() = personDao.getMainPersonItemLive()

    override fun getMainPersonColorLive() = personDao.getMainPersonColorLive()

    override fun getCurrPersonItemLive() = currPersonItemLive

    override fun selectCurrPerson(id: Int) = currPersonIDLive.postValue(id)

    override fun getColorResIdList(): List<Int> {
        return sharedPreferences.getStringSet(KEY_PERSON_COLOR_RES_ID_SET, null)?.map {
            it.toInt()
        } ?: emptyList()
    }

    override fun saveColorResIdList(newList: List<Int>) = sharedPreferences.edit(true) {
        putStringSet(KEY_PERSON_COLOR_RES_ID_SET, newList.map { it.toString() }.toMutableSet())
    }
}