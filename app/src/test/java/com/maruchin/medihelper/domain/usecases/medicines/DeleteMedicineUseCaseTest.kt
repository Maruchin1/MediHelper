package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito

class DeleteMedicineUseCaseTest {

    private val medicineRepo: MedicineRepo = mock()

    private lateinit var useCase: DeleteMedicineUseCase

    @Before
    fun before() {
        useCase = DeleteMedicineUseCase(medicineRepo)
    }

    @Test
    fun execute_medicinePictureExists() {
        val medicineId = "ABCD"

        runBlocking {
            val mockMedicine = Medicine(
                medicineId = "ABCD",
                name = "Lek",
                unit = "tabletki",
                expireDate = AppExpireDate(2020, 6),
                packageSize = null,
                currState = null,
                pictureName = "picture.jpg"
            )
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(mockMedicine)

            useCase.execute(medicineId)

            Mockito.verify(medicineRepo, Mockito.times(1)).deleteMedicinePicture("picture.jpg")
            Mockito.verify(medicineRepo, Mockito.times(1)).deleteById("ABCD")
        }
    }

    @Test
    fun execute_medicinePictureNotExists() {
        val medicineId = "ABCD"

        runBlocking {
            val mockMedicine = Medicine(
                medicineId = "ABCD",
                name = "Lek",
                unit = "tabletki",
                expireDate = AppExpireDate(2020, 6),
                packageSize = null,
                currState = null,
                pictureName = null
            )
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(mockMedicine)

            useCase.execute(medicineId)

            Mockito.verify(medicineRepo, Mockito.times(0)).deleteMedicinePicture(Mockito.anyString())
            Mockito.verify(medicineRepo, Mockito.times(1)).deleteById("ABCD")
        }
    }
}