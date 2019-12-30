package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileErrors
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.SaveProfileUseCase
import com.maruchin.medihelper.domain.utils.ProfileValidator
import com.maruchin.medihelper.testingframework.anyObject
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SaveProfileUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()
    private val validator: ProfileValidator = mock()

    private lateinit var useCase: SaveProfileUseCase

    @Before
    fun before() {
        useCase = SaveProfileUseCaseImpl(profileRepo, validator)
    }

    @Test
    fun execute_CorrectData_New() {
        val params = SaveProfileUseCase.Params(
            profileId = null,
            name = "Wojtek",
            color = "#000000"
        )
        val errors = ProfileErrors(
            emptyName = false,
            emptyColor = false
        )

        runBlocking {
            Mockito.`when`(validator.validate(anyObject(ProfileValidator.Params::class.java))).thenReturn(errors)

            val result = useCase.execute(params)

            Mockito.verify(profileRepo, Mockito.times(1)).addNew(anyObject(Profile::class.java))
            Truth.assertThat(result).isEqualTo(errors)
        }
    }

    @Test
    fun execute_CorrectData_Update() {
        val params = SaveProfileUseCase.Params(
            profileId = "abc",
            name = "Wojtek",
            color = "#000000"
        )
        val errors = ProfileErrors(
            emptyName = false,
            emptyColor = false
        )

        runBlocking {
            Mockito.`when`(validator.validate(anyObject(ProfileValidator.Params::class.java))).thenReturn(errors)

            val result = useCase.execute(params)

            Mockito.verify(profileRepo, Mockito.times(1)).update(anyObject(Profile::class.java))
            Truth.assertThat(result).isEqualTo(errors)
        }
    }
}