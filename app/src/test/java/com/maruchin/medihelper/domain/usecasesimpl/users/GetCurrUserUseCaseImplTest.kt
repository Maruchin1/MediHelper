package com.maruchin.medihelper.domain.usecasesimpl.users

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.UserNotSignedInException
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetCurrUserUseCaseImplTest {

    private val userAuthRepo: UserAuthRepo = mock()

    private lateinit var useCase: GetCurrUserUseCase

    @Before
    fun before() {
        useCase = GetCurrUserUseCaseImpl(userAuthRepo)
    }

    @Test
    fun execute_UserSignedIn() {
        val currUserMock = User(
            entityId = "abc",
            email = "test@mail.com"
        )

        runBlocking {
            Mockito.`when`(userAuthRepo.getCurrUser()).thenReturn(currUserMock)

            val currUser = useCase.execute()

            Truth.assertThat(currUser).isEqualTo(currUserMock)
        }
    }

    @Test(expected = UserNotSignedInException::class)
    fun execute_UserNotSignedIn() {
        runBlocking {
            Mockito.`when`(userAuthRepo.getCurrUser()).thenReturn(null)

            val currUser = useCase.execute()
        }
    }
}