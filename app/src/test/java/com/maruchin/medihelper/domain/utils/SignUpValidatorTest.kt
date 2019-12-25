package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class SignUpValidatorTest {

    private lateinit var validator: SignUpValidator

    @Before
    fun before() {
        validator = SignUpValidator()
    }

    @Test
    fun validate_AllFieldsValid() {
        val params = SignUpValidator.Params(
            email = "test@gmail.com",
            password = "password",
            passwordConfirm = "password",
            userName = "TestUser"
        )

        val result =  validator.validate(params)

        Truth.assertThat(result.noErrors).isTrue()
        Truth.assertThat(result.emptyEmail).isFalse()
        Truth.assertThat(result.emptyPassword).isFalse()
        Truth.assertThat(result.passwordsNotTheSame).isFalse()
        Truth.assertThat(result.emptyUserName).isFalse()
    }

    @Test
    fun validate_AllFieldsEmpty() {
        val params = SignUpValidator.Params(
            email = null,
            password = null,
            passwordConfirm = null,
            userName = null
        )

        val result = validator.validate(params)

        Truth.assertThat(result.noErrors).isFalse()
        Truth.assertThat(result.emptyEmail).isTrue()
        Truth.assertThat(result.emptyPassword).isTrue()
        Truth.assertThat(result.passwordsNotTheSame).isFalse()
        Truth.assertThat(result.emptyUserName).isTrue()
    }

    @Test
    fun validate_PasswordsDifferent() {
        val params = SignUpValidator.Params(
            email = "test@gmail.com",
            password = "password",
            passwordConfirm = "abc",
            userName = "TestUser"
        )

        val result = validator.validate(params)

        Truth.assertThat(result.noErrors).isFalse()
        Truth.assertThat(result.passwordsNotTheSame).isTrue()
    }
}