package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class PlannedMedicineRepoImpl : PlannedMedicineRepo {

    override suspend fun addNew(entity: PlannedMedicine) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(entity: PlannedMedicine) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: String): PlannedMedicine? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLiveById(id: String): LiveData<PlannedMedicine?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<PlannedMedicine> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllListLive(): LiveData<List<PlannedMedicine>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}