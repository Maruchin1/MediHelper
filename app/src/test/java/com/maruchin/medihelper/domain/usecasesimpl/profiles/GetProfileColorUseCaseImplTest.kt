package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetProfileColorUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()

    private lateinit var useCase: GetProfileColorUseCase

    @Before
    fun before() {
        useCase = GetProfileColorUseCaseImpl(profileRepo)
    }

    @Test
    fun execute() {
        val profileId = "abc"
        val profile = Profile(
            entityId = profileId,
            name = "Marcin",
            color = "#000000",
            mainPerson = true
        )

        runBlocking {
            Mockito.`when`(profileRepo.getById(profileId)).thenReturn(profile)

            val result = useCase.execute(profileId)

            Truth.assertThat(result).isEqualTo("#000000")
        }
    }
}