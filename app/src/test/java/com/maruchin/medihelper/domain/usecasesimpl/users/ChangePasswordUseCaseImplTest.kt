package com.maruchin.medihelper.domain.usecasesimpl.users

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.model.ChangePasswordErrors
import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.usecases.user.ChangePasswordUseCase
import com.maruchin.medihelper.domain.utils.ChangePasswordValidator
import com.maruchin.medihelper.testingframework.anyObject
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class ChangePasswordUseCaseImplTest {

    private val userRepo: UserRepo = mock()
    private val validator: ChangePasswordValidator = mock()

    private lateinit var useCase: ChangePasswordUseCase

    @Before
    fun before() {
        useCase = ChangePasswordUseCaseImpl(userRepo, validator)
    }

    @Test
    fun execute_CorrectData() {
        val params = ChangePasswordUseCase.Params(
            newPassword = "abc",
            newPasswordConfirm = "abc"
        )
        val anyValidatorParams = anyObject(ChangePasswordValidator.Params::class.java)
        val errorsMock = ChangePasswordErrors(
            emptyPassword = false,
            emptyPasswordConfirm = false,
            passwordsNotTheSame = false
        )
        Mockito.`when`(validator.validate(anyValidatorParams)).thenReturn(errorsMock)

        runBlocking {
            val errors = useCase.execute(params)

            Truth.assertThat(errors.noErrors).isTrue()
            Mockito.verify(userRepo, Mockito.times(1)).changePassword("abc")
        }
    }

    @Test
    fun execute_IncorrectData() {
        val params = ChangePasswordUseCase.Params(
            newPassword = "abc",
            newPasswordConfirm = "cba"
        )
        val anyValidatorParams = anyObject(ChangePasswordValidator.Params::class.java)
        val errorsMock = ChangePasswordErrors(
            emptyPassword = false,
            emptyPasswordConfirm = false,
            passwordsNotTheSame = true
        )
        Mockito.`when`(validator.validate(anyValidatorParams)).thenReturn(errorsMock)

        runBlocking {
            val errors = useCase.execute(params)

            Truth.assertThat(errors.noErrors).isFalse()
            Mockito.verify(userRepo, Mockito.times(0)).changePassword("abc")
        }
    }
}