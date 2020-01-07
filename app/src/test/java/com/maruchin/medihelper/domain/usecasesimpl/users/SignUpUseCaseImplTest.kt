package com.maruchin.medihelper.domain.usecasesimpl.users

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.model.SignUpErrors
import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.usecases.user.SignUpUseCase
import com.maruchin.medihelper.domain.utils.SignUpValidator
import com.maruchin.medihelper.testingframework.anyObject
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.mockito.Mockito

class SignUpUseCaseImplTest {

    private val userRepo: UserRepo = mock()
    private val validator: SignUpValidator = mock()

    private lateinit var useCase: SignUpUseCase

    @Before
    fun before() {
        useCase = SignUpUseCaseImpl(userRepo, validator)
    }

    @Ignore("usecase nietestowalny ponieważ zależność profileRepo jest wstrzykiwana w trakcie działąnia")
    fun execute_CorrectData() {
        val params = SignUpUseCase.Params(
            email = "test@mail.com",
            password = "abc",
            passwordConfirm = "abc",
            userName = "TestUser"
        )
        val anyValidatorParams = anyObject(SignUpValidator.Params::class.java)
        val errorsMock = SignUpErrors()

        Mockito.`when`(validator.validate(anyValidatorParams)).thenReturn(errorsMock)

        runBlocking {
            val errors = useCase.execute(params)

            Truth.assertThat(errors.noErrors).isTrue()
            Mockito.verify(userRepo, Mockito.times(1))
                .signUp("test@mail.com", "abc")
        }
    }
}