package com.maruchin.medihelper.domain.usecases.medicines

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchForMedicineInfoUseCaseTest {

    @Mock
    private lateinit var medicineRepo: MedicineRepo

    private val useCase: SearchForMedicineInfoUseCase by lazy { SearchForMedicineInfoUseCase(medicineRepo) }

    @Test
    fun execute() {
        val medicineName = "Hitaxa"

        val mockRepoResult = mutableListOf(
            MedicineInfoSearchResult(
                medicineName = "Hitaxa (roztwór doustny)",
                urlString = "https://www.mp.pl/pacjent/leki/lek/82959,Hitaxa-roztwor-doustny"
            )
        )

        runBlocking {
            Mockito.`when`(medicineRepo.searchForMedicineInfo(medicineName)).thenReturn(mockRepoResult)

            val result = useCase.execute(medicineName)

            Truth.assertThat(result.size).isEqualTo(1)
            Truth.assertThat(result[0].medicineName).isEqualTo("Hitaxa (roztwór doustny)")
            Truth.assertThat(result[0].urlString)
                .isEqualTo("https://www.mp.pl/pacjent/leki/lek/82959,Hitaxa-roztwor-doustny")
        }
    }
}