package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetProfileSimpleItemUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetProfileSimpleItemUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()

    private lateinit var useCase: GetProfileSimpleItemUseCase

    @Before
    fun before() {
        useCase = GetProfileSimpleItemUseCaseImpl(profileRepo)
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

        val expectedResult = ProfileSimpleItem(
            profileId = profileId,
            name = "Marcin",
            color = "#000000"
        )

        runBlocking {
            Mockito.`when`(profileRepo.getById(profileId)).thenReturn(profile)

            val result = useCase.execute(profileId)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }
}