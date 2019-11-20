package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.Person
import com.example.medihelper.domain.repositories.PersonRepo

class PersonRepoImpl : PersonRepo {
    override suspend fun insert(person: Person) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(person: Person) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: Int): Person {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveById(id: Int): LiveData<Person> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMainIdLive(): LiveData<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMainPersonColorLive(): LiveData<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllListLive(): LiveData<List<Person>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getListLiveByMedicineId(id: Int): LiveData<List<Person>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getColorIdList(): List<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}