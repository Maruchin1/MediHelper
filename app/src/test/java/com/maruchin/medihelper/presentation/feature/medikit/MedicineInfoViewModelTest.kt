package com.maruchin.medihelper.presentation.feature.medikit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineInfoUseCase
import com.maruchin.medihelper.domain.usecases.medicines.SearchForMedicineInfoUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

class MedicineInfoViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val searchForMedicineInfoUseCase: SearchForMedicineInfoUseCase = mock()
    private val getMedicineInfoUseCase: GetMedicineInfoUseCase = mock()

    private val observer: Observer<List<MedicineInfoSearchResult>> = mock()

    private lateinit var viewModel: MedicineInfoViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        viewModel = MedicineInfoViewModel(searchForMedicineInfoUseCase, getMedicineInfoUseCase)
        viewModel.searchResults.observeForever(observer)
    }

    @Test
    fun getNoResults() {

    }

//    @Test
    fun getSearchResults() {
        val args = MedicineInfoDialogArgs(medicineName = "Hitaxa")
        val mockSearchResult = listOf(
            MedicineInfoSearchResult(
                medicineName = "Hitaxa (roztwór doustny)",
                urlString = "https://www.mp.pl/pacjent/leki/lek/82959,Hitaxa-roztwor-doustny"
            ),
            MedicineInfoSearchResult(
                medicineName = "Hitaxa (tabletki ulegające rozpadowi)",
                urlString = "https://www.mp.pl/pacjent/leki/lek/82448,Hitaxa-tabletki-ulegajace-rozpadowi-w-jamie-ustnej"
            )
        )

        runBlocking {
            Mockito.`when`(searchForMedicineInfoUseCase.execute("Hitaxa")).thenReturn(mockSearchResult)
        }

        viewModel.setArgs(args)

        val captor = ArgumentCaptor.forClass(List::class.java)
        captor.run {
            val resultList = this.capture()
            Truth.assertThat(resultList).isNotEmpty()
            Truth.assertThat(resultList.size).isEqualTo(2)

            val firstItem = resultList[0]
            Truth.assertThat(firstItem).isInstanceOf(MedicineInfoSearchResult::class.java)

            val firstResult = firstItem as MedicineInfoSearchResult
            Truth.assertThat(firstResult.medicineName).isEqualTo("Hitaxa (roztwór doustny)")
            Truth.assertThat(firstResult.urlString)
                .isEqualTo("https://www.mp.pl/pacjent/leki/lek/82959,Hitaxa-roztwor-doustny")

            val secondResult = resultList[1] as MedicineInfoSearchResult
            Truth.assertThat(secondResult.medicineName).isEqualTo("Hitaxa (tabletki ulegające rozpadowi)")
            Truth.assertThat(secondResult.urlString)
                .isEqualTo("https://www.mp.pl/pacjent/leki/lek/82448,Hitaxa-tabletki-ulegajace-rozpadowi-w-jamie-ustnej")
        }
    }

    @Test
    fun getMedicineInfo() {
    }

    @Test
    fun getLoadingInProgress() {
    }

    @Test
    fun setArgs() {
    }

    @Test
    fun getMedicineInfo1() {
    }
}