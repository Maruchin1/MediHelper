package com.maruchin.medihelper.domain.usecases.medicines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

class GetAllMedicinesItemsLiveUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val medicineRepo: MedicineRepo = mock()
    private val observer: Observer<List<MedicineItem>> = mock()

    private lateinit var useCase: GetAllMedicinesItemsLiveUseCase

    @Before
    fun before() {
        useCase = GetAllMedicinesItemsLiveUseCase(medicineRepo)
    }

//    @Test
    fun execute() {
        val mockMedicineList = listOf(
            Medicine(
                medicineId = "A",
                name = "Lek1",
                unit = "tabletki",
                expireDate = AppExpireDate(2020, 6),
                packageSize = null,
                currState = null,
                additionalInfo = null,
                pictureName = null
            ),
            Medicine(
                medicineId = "B",
                name = "Lek2",
                unit = "patylki",
                expireDate = AppExpireDate(2022, 1),
                packageSize = null,
                currState = null,
                additionalInfo = null,
                pictureName = null
            )
        )
        runBlocking {
            Mockito.`when`(medicineRepo.getAllListLive()).thenReturn(MutableLiveData(mockMedicineList))
        }

        val result = runBlocking {
            useCase.execute()
        }
        result.observeForever(observer)

        val captor = ArgumentCaptor.forClass(List::class.java)
        captor.run {
            val list = this.capture()

            Truth.assertThat(list).isNotEmpty()
            Truth.assertThat(list.size).isEqualTo(2)

            val firstItem = list[0]
            Truth.assertThat(firstItem).isInstanceOf(MedicineItem::class.java)

            val  firstMedicineItem = firstItem as MedicineItem
            Truth.assertThat(firstMedicineItem.medicineId).isEqualTo("A")
            Truth.assertThat(firstMedicineItem.name).isEqualTo("Lek1")
            Truth.assertThat(firstMedicineItem.unit).isEqualTo("tabletki")
        }
    }
}