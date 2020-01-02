package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SignOutUseCaseImplTest {

    private val userAuthRepo: UserAuthRepo = mock()

    private lateinit var useCase: SignOutUseCase

    @Before
    fun before() {
        useCase = SignOutUseCaseImpl(userAuthRepo)
    }

    @Test
    fun execute() {
        runBlocking {
            useCase.execute()

            Mockito.verify(userAuthRepo, Mockito.times(1)).signOut()
        }
    }
}