package com.example.medihelper

import com.example.medihelper.custom.AppDate
import com.google.common.truth.Truth.assertThat
import com.example.medihelper.localdatabase.entity.MedicineEntity
import org.junit.Test

class MedicineEntityTest {

    @Test
    fun medicineEntity_ReduceCurrState() {
        val medicineEntity = MedicineEntity(
            medicineName = "Apap",
            medicineUnit = "tableki",
            expireDate = AppDate(2020, 5, 1),
            packageSize = 60f,
            currState = 40f
        )
        medicineEntity.reduceCurrState(2f)
        assertThat(medicineEntity.currState).isEqualTo(38f)
    }

    @Test
    fun medicineEntity_ReduceCurrState_LessThanZero() {
        val medicineEntity = MedicineEntity(
            medicineName = "Apap",
            medicineUnit = "tableki",
            expireDate = AppDate(2020, 5, 1),
            packageSize = 60f,
            currState = 1f
        )
        medicineEntity.reduceCurrState(2f)
        assertThat(medicineEntity.currState).isEqualTo(0f)
    }

    @Test
    fun medicineEntity_IncreaseCurrState() {
        val medicineEntity = MedicineEntity(
            medicineName = "Apap",
            medicineUnit = "tableki",
            expireDate = AppDate(2020, 5, 1),
            packageSize = 60f,
            currState = 40f
        )
        medicineEntity.increaseCurrState(2f)
        assertThat(medicineEntity.currState).isEqualTo(42f)
    }

    @Test
    fun medicineEntity_IncreaseCurrState_MoreThenPackageSize() {
        val medicineEntity = MedicineEntity(
            medicineName = "Apap",
            medicineUnit = "tableki",
            expireDate = AppDate(2020, 5, 1),
            packageSize = 60f,
            currState = 59f
        )
        medicineEntity.increaseCurrState(2f)
        assertThat(medicineEntity.currState).isEqualTo(60f)
    }
}