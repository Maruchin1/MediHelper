package com.maruchin.medihelper.domain.usecasesimpl.users

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.model.SignInErrors
import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.usecases.user.SignInUseCase
import com.maruchin.medihelper.domain.utils.SignInValidator
import com.maruchin.medihelper.testingframework.anyObject
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SignInUseCaseImplTest {

    private val userRepo: UserRepo = mock()
    private val validator: SignInValidator = mock()

    private lateinit var useCase: SignInUseCase

    @Before
    fun before() {
        useCase = SignInUseCaseImpl(userRepo, validator)
    }

    @Test
    fun execute_CorrectData() {
        val params = SignInUseCase.Params(
            email = "test@email.com",
            password = "abc"
        )
        val anyValidatorParams = anyObject(SignInValidator.Params::class.java)
        val errorsMock = SignInErrors()

        Mockito.`when`(validator.validate(anyValidatorParams)).thenReturn(errorsMock)

        runBlocking {
            val errors = useCase.execute(params)

            Truth.assertThat(errors.noErrors).isTrue()
            Mockito.verify(userRepo, Mockito.times(1))
                .signIn("test@email.com", "abc")
        }
    }

    @Test
    fun execute_IncorrectData() {
        val params = SignInUseCase.Params(
            email = "test@email.com",
            password = null
        )
        val anyValidatorParams = anyObject(SignInValidator.Params::class.java)
        val errorsMock = SignInErrors(emptyPassword = true)

        Mockito.`when`(validator.validate(anyValidatorParams)).thenReturn(errorsMock)

        runBlocking {
            val errors = useCase.execute(params)

            Truth.assertThat(errors.noErrors).isFalse()
            Mockito.verify(userRepo, Mockito.times(0))
                .signIn("test@email.com", "abc")
        }
    }
}