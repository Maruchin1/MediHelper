package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineUnitsUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetMedicineUnitsUseCaseImplTest {

    private val medicineRepo: MedicineRepo = mock()

    private lateinit var useCase: GetMedicineUnitsUseCase

    @Before
    fun before() {
        useCase = GetMedicineUnitsUseCaseImpl(
            medicineRepo
        )
    }

    @Test
    fun execute() {
        val unitsList = listOf("tabletki", "ml", "pastylki")

        val expectedResult = listOf("tabletki", "ml", "pastylki")

        runBlocking {
            Mockito.`when`(medicineRepo.getMedicineUnits()).thenReturn(unitsList)

            val result = useCase.execute()

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }
}