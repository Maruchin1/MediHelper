package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteMedicinesPlansUseCase
import com.maruchin.medihelper.domain.usecases.profile.DeleteProfileUseCase
import com.maruchin.medihelper.testingframework.mock
import com.maruchin.medihelper.testingframework.verifyInvocations
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DeleteProfileUseCaseImplTest {

    private val profileRepo: ProfileRepo = mock()
    private val medicinePlanRepo: MedicinePlanRepo = mock()
    private val deleteMedicinesPlansUseCase: DeleteMedicinesPlansUseCase = mock()

    private lateinit var useCase: DeleteProfileUseCase

    @Before
    fun before() {
        useCase = DeleteProfileUseCaseImpl(
            profileRepo,
            medicinePlanRepo,
            deleteMedicinesPlansUseCase
        )
    }

    @Test
    fun execute() {
        val profileId = "abc"

        runBlocking {
            Mockito.`when`(medicinePlanRepo.getListByProfile(profileId)).thenReturn(emptyList())

            useCase.execute(profileId)

            Mockito.verify(profileRepo, Mockito.times(1)).deleteById("abc")
            verifyInvocations(deleteMedicinesPlansUseCase, invocations = 1).execute(Mockito.anyList())
        }
    }
}