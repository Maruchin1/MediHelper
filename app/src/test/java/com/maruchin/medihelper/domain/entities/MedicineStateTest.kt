package com.maruchin.medihelper.domain.entities

import com.google.common.truth.Truth
import org.junit.Test

class MedicineStateTest {

    @Test
    fun type_Full_StateGood() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 100f
        )

        val type = state.type

        Truth.assertThat(type).isEqualTo(MedicineState.Type.GOOD)
    }

    @Test
    fun type_76percent_StateGood() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 76f
        )

        val type = state.type

        Truth.assertThat(type).isEqualTo(MedicineState.Type.GOOD)
    }

    @Test
    fun type_75percent_StateMedium() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 75f
        )

        val type = state.type

        Truth.assertThat(type).isEqualTo(MedicineState.Type.MEDIUM)
    }

    @Test
    fun type_41percent_StateMedium() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 41f
        )

        val type = state.type

        Truth.assertThat(type).isEqualTo(MedicineState.Type.MEDIUM)
    }

    @Test
    fun type_40percent_StateSmall() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 40f
        )

        val type = state.type

        Truth.assertThat(type).isEqualTo(MedicineState.Type.SMALL)
    }

    @Test
    fun type_1percent_StateSmall() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 1f
        )

        val type = state.type

        Truth.assertThat(type).isEqualTo(MedicineState.Type.SMALL)
    }

    @Test
    fun type_0percent_StateEmpty() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 0f
        )

        val type = state.type

        Truth.assertThat(type).isEqualTo(MedicineState.Type.EMPTY)
    }

    @Test
    fun reduce_Normal() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 76f
        )

        state.reduce(1f)

        Truth.assertThat(state.packageSize).isEqualTo(100f)
        Truth.assertThat(state.currState).isEqualTo(75f)
        Truth.assertThat(state.type).isEqualTo(MedicineState.Type.MEDIUM)
    }

    @Test
    fun reduce_LessTanZero() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 10f
        )

        state.reduce(11f)

        Truth.assertThat(state.packageSize).isEqualTo(100f)
        Truth.assertThat(state.currState).isEqualTo(0f)
        Truth.assertThat(state.type).isEqualTo(MedicineState.Type.EMPTY)
    }

    @Test
    fun increase_Normal() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 75f
        )

        state.increase(1f)

        Truth.assertThat(state.packageSize).isEqualTo(100f)
        Truth.assertThat(state.currState).isEqualTo(76f)
        Truth.assertThat(state.type).isEqualTo(MedicineState.Type.GOOD)
    }

    @Test
    fun increase_MoreThanPackageSize() {
        val state = MedicineState(
            packageSize = 100f,
            currState = 90f
        )

        state.increase(11f)

        Truth.assertThat(state.packageSize).isEqualTo(100f)
        Truth.assertThat(state.currState).isEqualTo(100f)
        Truth.assertThat(state.type).isEqualTo(MedicineState.Type.GOOD)
    }
}