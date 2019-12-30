package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorsUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class GetProfileColorsUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()

    private lateinit var useCase: GetProfileColorsUseCase

    @Before
    fun before() {
        useCase = GetProfileColorsUseCaseImpl(profileRepo)
    }

    @Test
    fun execute() {
        val colorsList = listOf("#000000", "#ffffff")

        runBlocking {
            Mockito.`when`(profileRepo.getColorsList()).thenReturn(colorsList)

            val result = useCase.execute()

            Truth.assertThat(result).isEqualTo(colorsList)
        }
    }
}