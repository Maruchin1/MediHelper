package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileEditData
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetProfileEditDataUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetProfileEditDataUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()

    private lateinit var usecase: GetProfileEditDataUseCase

    @Before
    fun before() {
        usecase = GetProfileEditDataUseCaseImpl(profileRepo)
    }

    @Test
    fun execute() {
        val profileId = "abc"
        val profile = Profile(
            entityId = profileId,
            name = "Wojtek",
            color = "#000000",
            mainPerson = false
        )

        val expectedResult = ProfileEditData(
            profileId = profileId,
            name = "Wojtek",
            color = "#000000"
        )

        runBlocking {
            Mockito.`when`(profileRepo.getById(profileId)).thenReturn(profile)

            val result = usecase.execute(profileId)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }
}