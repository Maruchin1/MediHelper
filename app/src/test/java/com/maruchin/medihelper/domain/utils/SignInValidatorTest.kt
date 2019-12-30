package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
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

        Truth.assertThat(errors.noErrors).isTrue()
        Truth.assertThat(errors.globalMessage).isEmpty()
        Truth.assertThat(errors.emptyEmail).isFalse()
        Truth.assertThat(errors.emptyPassword).isFalse()
    }
}