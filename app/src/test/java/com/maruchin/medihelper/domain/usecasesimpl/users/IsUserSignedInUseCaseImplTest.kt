package com.maruchin.medihelper.domain.usecasesimpl.users

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.IsUserSignedInUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class IsUserSignedInUseCaseImplTest {

    private val userAuthRepo: UserAuthRepo = mock()

    private lateinit var useCase: IsUserSignedInUseCase

    @Before
    fun before() {
        useCase = IsUserSignedInUseCaseImpl(userAuthRepo)
    }

    @Test
    fun execute_UserSignedIn() {
        val currUserMock = User(
            entityId = "abc",
            email = "test@mail.com"
        )

        runBlocking {
            Mockito.`when`(userAuthRepo.getCurrUser()).thenReturn(currUserMock)

            val result = useCase.execute()

            Truth.assertThat(result).isTrue()
        }
    }

    @Test
    fun execute_UserNotSignedIn() {
        runBlocking {
            Mockito.`when`(userAuthRepo.getCurrUser()).thenReturn(null)

            val result = useCase.execute()

            Truth.assertThat(result).isFalse()
        }
    }
}