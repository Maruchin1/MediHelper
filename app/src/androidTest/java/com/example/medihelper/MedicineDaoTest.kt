package com.example.medihelper

import com.example.medihelper.custom.AppExpireDate
import com.example.medihelper.localdatabase.dao.MedicineDao
import com.example.medihelper.localdatabase.entity.MedicineEntity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject
import com.google.common.truth.Truth.assertThat

class MedicineDaoTest : DatabaseTest() {

    private val medicineDao: MedicineDao by inject()

    @Test
    fun medicineDao_InsertAndReadEntityList() {
        val newEntity = MedicineEntity(
            medicineName = "Hitaxa",
            medicineUnit = "tabletki",
            expireDate = AppExpireDate(2020, 10)
        )
        val expectedResult = newEntity.copy(medicineID = 1)

        runBlocking {
            medicineDao.insert(newEntity)

            val list = medicineDao.getEntityList()
            assertThat(list.size).isEqualTo(1)

            val insertedEntity = list[0]
            assertThat(insertedEntity).isEqualTo(expectedResult)
        }
    }

    @Test
    fun medicineDao_UpdateAndReadEntity() {
        val newEntity = MedicineEntity(
            medicineName = "Hitaxa",
            medicineUnit = "tabletki",
            expireDate = AppExpireDate(2020, 10)
        )
        val expectedResult = newEntity.copy(
            medicineID = 1,
            medicineName = "Zenaro"
        )
        runBlocking {
            medicineDao.insert(newEntity)
            val existingEntity = medicineDao.getEntity(1)

            val updatedEntity = existingEntity.copy(medicineName = "Zenaro")
            medicineDao.update(updatedEntity)

            val entityAfterUpdate = medicineDao.getEntity(1)
            assertThat(entityAfterUpdate).isEqualTo(expectedResult)
        }
    }

    @Test
    fun medicineDao_InsertAndDelete() {
        val newEntity = MedicineEntity(
            medicineName = "Hitaxa",
            medicineUnit = "tabletki",
            expireDate = AppExpireDate(2020, 10)
        )

        runBlocking {
            medicineDao.insert(newEntity)
            var list = medicineDao.getEntityList()
            assertThat(list.size).isEqualTo(1)

            medicineDao.delete(1)
            list = medicineDao.getEntityList()
            assertThat(list.size).isEqualTo(0)
        }
    }
}