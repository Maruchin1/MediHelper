package com.example.medihelper.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.DeletedHistory
import com.example.medihelper.localdata.dao.PersonDao
import com.example.medihelper.localdata.entity.PersonEntity
import com.example.medihelper.localdata.pojo.PersonEditData
import com.example.medihelper.localdata.pojo.PersonItem
import com.example.medihelper.localdata.pojo.PersonOptionsData

interface PersonService {
    suspend fun save(editData: PersonEditData)
    suspend fun delete(id: Int)
    suspend fun getItem(id: Int): PersonItem
    suspend fun getEditData(id: Int): PersonEditData
    fun getMainPersonId(): Int?
    fun getMainPersonColorLive(): LiveData<Int>
    fun getItemLive(id: Int): LiveData<PersonItem>
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

    private val currPersonIDLive = MutableLiveData<Int>(appSharedPref.getMainPersonId())
    private val currPersonItemLive: LiveData<PersonItem>

    init {
        currPersonItemLive = Transformations.switchMap(currPersonIDLive) { personID ->
            personID?.let { personDao.getItemLive(it) }
        }
    }

    override suspend fun save(editData: PersonEditData) {
        val entity = PersonEntity(
            personId = editData.personId,
            personName = editData.personName,
            personColorResId = editData.personColorResId,
            synchronizedWithServer = false
        )
        if (entity.personId == 0) {
            personDao.insert(entity).toInt()
        } else {
            personDao.update(entity)
        }
    }

    override suspend fun delete(id: Int) {
        personDao.getRemoteIdById(id)?.let { deletedHistory.addToPersonHistory(it) }
        personDao.delete(id)
    }

    override suspend fun getItem(id: Int) = personDao.getItem(id)

    override suspend fun getEditData(id: Int) = personDao.getEditData(id)

    override fun getMainPersonId() = appSharedPref.getMainPersonId()

    override fun getMainPersonColorLive(): LiveData<Int> {
        val liveData = appSharedPref.getMainPersonId()?.let { personDao.getColorLive(it) }
        return liveData ?: MutableLiveData<Int>()
    }

    override fun getItemLive(id: Int) = personDao.getItemLive(id)

    override fun getOptionsDataLive(id: Int) = personDao.getOptionsDataLive(id)

    override fun getItemListLive() = personDao.getItemListLive()

    override fun getItemListLiveByMedicineID(id: Int) = personDao.getItemListLiveByMedicineID(id)

    override fun getCurrPersonItemLive() = currPersonItemLive

    override fun selectCurrPerson(id: Int) = currPersonIDLive.postValue(id)

    override fun getColorResIdList() = appSharedPref.getColorResIdList()

    override fun saveColorResIdList(newList: List<Int>) = appSharedPref.saveColorResIdList(newList)
}