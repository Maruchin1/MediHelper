package com.maruchin.medihelper.domain.usecases.medicines

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMedicineInfoUseCaseTest {

    @Mock
    private lateinit var medicineRepo: MedicineRepo

    private val useCase: GetMedicineInfoUseCase by lazy { GetMedicineInfoUseCase(medicineRepo) }

    @Test
    fun execute() {
        val urlString = "https://test.pl"

        val mockRepoResult = mutableListOf(
            MedicineInfo(
                header = "Przeciwskazania",
                body = "Nie przyjmować z innymi lekami"
            )
        )

        runBlocking {
            Mockito.`when`(medicineRepo.getMedicineInfo(urlString)).thenReturn(mockRepoResult)

            val result = useCase.execute(urlString)

            Truth.assertThat(result.size).isEqualTo(1)
            Truth.assertThat(result[0].header).isEqualTo("Przeciwskazania")
            Truth.assertThat(result[0].body).isEqualTo("Nie przyjmować z innymi lekami")
        }
    }
}