package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineReminderModeUseCase
import com.maruchin.medihelper.testingframework.mock
import com.maruchin.medihelper.testingframework.verifyInvocations
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ChangePlannedMedicineReminderModeUseCaseImplTest {

    private val settingsRepo: SettingsRepo = mock()

    private lateinit var useCase: ChangePlannedMedicineReminderModeUseCase

    @Before
    fun before() {
        useCase = ChangePlannedMedicineReminderModeUseCaseImpl(settingsRepo)
    }

    @Test
    fun execute() {
        val newMode = ReminderMode.ALARM

        runBlocking {
            useCase.execute(newMode)

            verifyInvocations(settingsRepo, invocations = 1).setReminderMode(newMode)
        }
    }
}