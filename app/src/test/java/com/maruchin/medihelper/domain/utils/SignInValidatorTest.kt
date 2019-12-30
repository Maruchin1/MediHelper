package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.model.SignInErrors
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignInValidatorTest {

    private lateinit var validator: SignInValidator

    @Before
    fun before() {
        validator =  SignInValidator()
    }

    @Test
    fun validate_AllCorrect() {
        val params = SignInValidator.Params(
            email = "test@mail.com",
            password = "abc"
        )

        val errors = validator.validate(params)

        val expectedResult = SignInErrors(
            emptyEmail = false,
            emptyPassword = false,
            incorrectEmail = false,
            incorrectPassword = false,
            undefinedError = false
        )

        Truth.assertThat(errors.noErrors).isTrue()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }

    @Test
    fun validate_AllEmpty() {
        val params = SignInValidator.Params(
            email = null,
            password = null
        )

        val errors = validator.validate(params)

        val expectedResult = SignInErrors(
            emptyEmail = true,
            emptyPassword = true,
            incorrectEmail = false,
            incorrectPassword = false,
            undefinedError = false
        )

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }
}