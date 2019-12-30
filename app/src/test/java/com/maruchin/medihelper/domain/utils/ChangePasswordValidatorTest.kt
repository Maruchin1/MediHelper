package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class ChangePasswordValidatorTest {

    private lateinit var validator: ChangePasswordValidator

    @Before
    fun before() {
        validator = ChangePasswordValidator()
    }

    @Test
    fun validate_AllCorrect() {
        val params = ChangePasswordValidator.Params(
            password = "abc",
            passwordConfirm = "abc"
        )

        val errors = validator.validate(params)

        Truth.assertThat(errors.noErrors).isTrue()
        Truth.assertThat(errors.emptyPassword).isFalse()
        Truth.assertThat(errors.emptyPasswordConfirm).isFalse()
        Truth.assertThat(errors.passwordsNotTheSame).isFalse()
    }

    @Test
    fun validate_AllEmpty() {
        val params = ChangePasswordValidator.Params(
            password = null,
            passwordConfirm = null
        )

        val errors = validator.validate(params)

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors.emptyPassword).isTrue()
        Truth.assertThat(errors.emptyPasswordConfirm).isTrue()
        Truth.assertThat(errors.passwordsNotTheSame).isFalse()
    }

    @Test
    fun validate_DifferentPasswords() {
        val params = ChangePasswordValidator.Params(
            password = "abc",
            passwordConfirm = "def"
        )

        val errors = validator.validate(params)

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors.emptyPassword).isFalse()
        Truth.assertThat(errors.emptyPasswordConfirm).isFalse()
        Truth.assertThat(errors.passwordsNotTheSame).isTrue()
    }
}