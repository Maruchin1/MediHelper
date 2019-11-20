package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.repositories.MedicineRepo
import com.example.medihelper.localdata.entity.MedicineEntity

class MedicineRepoImpl : MedicineRepo {

    override suspend fun insert(medicine: Medicine) {
        val newEntity = MedicineEntity(
            medicineId = medicine.medicineId,
            medicineName = medicine.name,
            medicineUnit = medicine.unit,
            expireDate = medicine.expireDate,
            packageSize = medicine.packageSize,
            currState = medicine.currState,
            additionalInfo = medicine.additionalInfo,
            imageName = medicine.image?.name
        )
    }

    override suspend fun update(medicine: Medicine) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: Int): Medicine {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveById(id: Int): LiveData<Medicine> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllListLive(): LiveData<List<Medicine>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getListLiveFilteredByName(nameQuery: String): LiveData<List<Medicine>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}