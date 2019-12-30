package com.maruchin.medihelper.domain.usecases.medicines

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecasesimpl.medicines.SearchForMedicineInfoUseCaseImpl
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SearchForMedicineInfoUseCaseImplTest {

    private val medicineRepo: MedicineRepo = mock()

    private lateinit var useCase: SearchForMedicineInfoUseCase

    @Before
    fun before() {
        useCase =
            SearchForMedicineInfoUseCaseImpl(
                medicineRepo
            )
    }

    @Test
    fun execute() {
        val medicineName = "Hitaxa"
        val searchResultList = listOf(
            MedicineInfoSearchResult(
                medicineName = "Hitaxa",
                urlString = "http://test.com"
            ),
            MedicineInfoSearchResult(
                medicineName = "Hitaxa2",
                urlString = "http://test2.com"
            )
        )

        val expectedResult = listOf(
            MedicineInfoSearchResult(
                medicineName = "Hitaxa",
                urlString = "http://test.com"
            ),
            MedicineInfoSearchResult(
                medicineName = "Hitaxa2",
                urlString = "http://test2.com"
            )
        )

        runBlocking {
            Mockito.`when`(medicineRepo.searchForMedicineInfo(medicineName)).thenReturn(searchResultList)

            val result = useCase.execute(medicineName)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }
}