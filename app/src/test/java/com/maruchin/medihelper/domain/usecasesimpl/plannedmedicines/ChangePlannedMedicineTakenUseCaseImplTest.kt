package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.planned_medicines.ChangePlannedMedicineTakenUseCase
import com.maruchin.medihelper.testingframework.mock
import org.junit.Before

class ChangePlannedMedicineTakenUseCaseImplTest {

    private val plannedMedicineRepo: PlannedMedicineRepo = mock()
    private val medicineRepo: MedicineRepo = mock()

    private lateinit var useCase: ChangePlannedMedicineTakenUseCase

    @Before
    fun before() {
        useCase = ChangePlannedMedicineTakenUseCaseImpl(plannedMedicineRepo, medicineRepo)
    }

//    @Test
    fun execute_Correct() {

    }
}