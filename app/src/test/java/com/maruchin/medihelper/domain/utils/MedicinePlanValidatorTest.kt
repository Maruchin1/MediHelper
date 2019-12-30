package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.MedicinePlanErrors
import org.junit.Before
import org.junit.Test

class MedicinePlanValidatorTest {

    private lateinit var validator: MedicinePlanValidator

    @Before
    fun before() {
        validator = MedicinePlanValidator()
    }

    @Test
    fun validate_Basic_AllCorrect() {
        val params = MedicinePlanValidator.Params(
            profileId = "aaa",
            medicineId = "bbb",
            planType = MedicinePlan.Type.ONCE,
            startDate = AppDate(2019, 11, 1),
            endDate = null,
            intakeDays = null,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val errors = validator.validate(params)

        val expectedResult = MedicinePlanErrors(
            emptyProfileId = false,
            emptyMedicineId = false,
            emptyPlanType = false,
            emptyStartDate = false,
            emptyEndDate = false,
            incorrectDatesOrder = false,
            emptyIntakeDays = false,
            emptyTimeDoseList = false
        )

        Truth.assertThat(errors.noErrors).isTrue()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }

    @Test
    fun validate_Basic_AllEmpty() {
        val params = MedicinePlanValidator.Params(
            profileId = null,
            medicineId = null,
            planType = null,
            startDate = null,
            endDate = null,
            intakeDays = null,
            timeDoseList = null
        )

        val errors = validator.validate(params)

        val expectedResult = MedicinePlanErrors(
            emptyProfileId = true,
            emptyMedicineId = true,
            emptyPlanType = true,
            emptyStartDate = false,
            emptyEndDate = false,
            incorrectDatesOrder = false,
            emptyIntakeDays = false,
            emptyTimeDoseList = true
        )

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }

    @Test
    fun validate_Once_EmptyStartDate() {
        val params = MedicinePlanValidator.Params(
            profileId = "aaa",
            medicineId = "bbb",
            planType = MedicinePlan.Type.ONCE,
            startDate = null,
            endDate = null,
            intakeDays = null,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val errors = validator.validate(params)

        val expectedResult = MedicinePlanErrors(
            emptyProfileId = false,
            emptyMedicineId = false,
            emptyPlanType = false,
            emptyStartDate = true,
            emptyEndDate = false,
            incorrectDatesOrder = false,
            emptyIntakeDays = false,
            emptyTimeDoseList = false
        )

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }

    @Test
    fun validate_Period_EmptyDatesAndIntakeDays() {
        val params = MedicinePlanValidator.Params(
            profileId = "aaa",
            medicineId = "bbb",
            planType = MedicinePlan.Type.PERIOD,
            startDate = null,
            endDate = null,
            intakeDays = null,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val errors = validator.validate(params)

        val expectedResult = MedicinePlanErrors(
            emptyProfileId = false,
            emptyMedicineId = false,
            emptyPlanType = false,
            emptyStartDate = true,
            emptyEndDate = true,
            incorrectDatesOrder = false,
            emptyIntakeDays = true,
            emptyTimeDoseList = false
        )

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }

    @Test
    fun validate_Once_IncorrectDatesOrder() {
        val params = MedicinePlanValidator.Params(
            profileId = "aaa",
            medicineId = "bbb",
            planType = MedicinePlan.Type.PERIOD,
            startDate = AppDate(2019, 11, 10),
            endDate = AppDate(2019, 11, 9),
            intakeDays = IntakeDays.Everyday,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val errors = validator.validate(params)

        val expectedResult = MedicinePlanErrors(
            emptyProfileId = false,
            emptyMedicineId = false,
            emptyPlanType = false,
            emptyStartDate = false,
            emptyEndDate = false,
            incorrectDatesOrder = true,
            emptyIntakeDays = false,
            emptyTimeDoseList = false
        )

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }

    @Test
    fun validate_Continuous_EmptyStartAndIntakeDays() {
        val params = MedicinePlanValidator.Params(
            profileId = "aaa",
            medicineId = "bbb",
            planType = MedicinePlan.Type.CONTINUOUS,
            startDate = null,
            endDate = null,
            intakeDays = null,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val errors = validator.validate(params)

        val expectedResult = MedicinePlanErrors(
            emptyProfileId = false,
            emptyMedicineId = false,
            emptyPlanType = false,
            emptyStartDate = true,
            emptyEndDate = false,
            incorrectDatesOrder = false,
            emptyIntakeDays = true,
            emptyTimeDoseList = false
        )

        Truth.assertThat(errors.noErrors).isFalse()
        Truth.assertThat(errors).isEqualTo(expectedResult)
    }
}