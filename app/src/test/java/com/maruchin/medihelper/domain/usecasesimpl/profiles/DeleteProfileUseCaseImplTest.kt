package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.plans.DeletePlansUseCase
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase
import com.maruchin.medihelper.testingframework.mock
import com.maruchin.medihelper.testingframework.verifyInvocations
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DeleteProfileUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()
    private val planRepo: PlanRepo = mock()
    private val deletePlansUseCase: DeletePlansUseCase = mock()

    private lateinit var useCase: DeleteProfileUseCase

    @Before
    fun before() {
        useCase = DeleteProfileUseCaseImpl(
            profileRepo,
            planRepo,
            deletePlansUseCase
        )
    }

    @Test
    fun execute() {
        val profileId = "abc"

        runBlocking {
            Mockito.`when`(planRepo.getByProfile(profileId)).thenReturn(emptyList())

            useCase.execute(profileId)

            Mockito.verify(profileRepo, Mockito.times(1)).deleteById("abc")
            verifyInvocations(deletePlansUseCase, invocations = 1).execute(Mockito.anyList())
        }
    }
}