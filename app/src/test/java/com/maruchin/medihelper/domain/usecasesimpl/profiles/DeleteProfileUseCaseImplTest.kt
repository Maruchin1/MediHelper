package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DeleteProfileUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()

    private lateinit var useCase: DeleteProfileUseCase

    @Before
    fun before() {
        useCase = DeleteProfileUseCaseImpl(profileRepo)
    }

    @Test
    fun execute() {
        val profileId = "abc"

        runBlocking {

            useCase.execute(profileId)

            Mockito.verify(profileRepo, Mockito.times(1)).deleteById("abc")
        }
    }
}