package com.maruchin.medihelper.domain.usecases.medicines

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecasesimpl.medicines.GetMedicineInfoUseCaseImpl
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetMedicineInfoUseCaseImplTest {

    private val medicineRepo: MedicineRepo = mock()

    private lateinit var useCase: GetMedicineInfoUseCase

    @Before
    fun before() {
        useCase = GetMedicineInfoUseCaseImpl(
            medicineRepo
        )
    }

    @Test
    fun execute() {
        val urlString = "http://test.com"
        val medicineInfoList = listOf(
            MedicineInfo(
                header = "heade1",
                body = "body1"
            ),
            MedicineInfo(
                header = "header2",
                body = "body2"
            )
        )

        val exepectedResult = listOf(
            MedicineInfo(
                header = "heade1",
                body = "body1"
            ),
            MedicineInfo(
                header = "header2",
                body = "body2"
            )
        )

        runBlocking {
            Mockito.`when`(medicineRepo.getMedicineInfo(urlString)).thenReturn(medicineInfoList)

            val result = useCase.execute(urlString)

            Truth.assertThat(result).isEqualTo(exepectedResult)
        }
    }
}