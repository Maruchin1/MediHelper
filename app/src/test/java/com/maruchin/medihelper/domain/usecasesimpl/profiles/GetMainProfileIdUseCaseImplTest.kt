package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetMainProfileIdUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetMainProfileIdUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()

    private lateinit var useCase: GetMainProfileIdUseCase

    @Before
    fun before() {
        useCase = GetMainProfileIdUseCaseImpl(profileRepo)
    }

    @Test
    fun execute() {
        val mainProfileId = "abc"

        runBlocking {
            Mockito.`when`(profileRepo.getMainId()).thenReturn(mainProfileId)

            val result = useCase.execute()

            Truth.assertThat(result).isEqualTo(mainProfileId)
        }
    }
}