package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class ProfileValidatorTest {

    private lateinit var validator: ProfileValidator

    @Before
    fun before() {
        validator = ProfileValidator()
    }

    @Test
    fun validate_AllCorrect() {
        val params = ProfileValidator.Params(
            name = "Wojtek",
            color = "#000000"
        )

        val errors = validator.validate(params)

        Truth.assertThat(errors.noErrors).isTrue()
        Truth.assertThat(errors.emptyName).isFalse()
        Truth.assertThat(errors.emptyColor).isFalse()
    }

    @Test
    fun validate_AllEmpty() {
        val params = ProfileValidator.Params(
            name = null,
            color = null
        )

        val errors = validator.validate(params)

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors.emptyName).isTrue()
        Truth.assertThat(errors.emptyColor).isTrue()
    }
}