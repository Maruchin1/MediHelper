package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.MedicinePlanOnce
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo

class MedicinePlanRepoImpl : MedicinePlanRepo {

    override suspend fun addNew(entity: MedicinePlan) {
        if (entity is MedicinePlanOnce) {
            entity.date
        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(entity: MedicinePlan) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: String): MedicinePlan? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLiveById(id: String): LiveData<MedicinePlan?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<MedicinePlan> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllListLive(): LiveData<List<MedicinePlan>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}