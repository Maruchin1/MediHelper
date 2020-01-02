package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.domain.utils.MedicineValidator
import com.maruchin.medihelper.testingframework.mock
import org.junit.Before
import org.junit.Test


class SaveMedicineUseCaseImplTest {

    private val medicineRepo: MedicineRepo = mock()
    private val validator: MedicineValidator = mock()

    private lateinit var useCase: SaveMedicineUseCase

    @Before
    fun before() {
        useCase = SaveMedicineUseCaseImpl(
            medicineRepo,
            validator
        )
    }

    @Test
    fun execute_CorrectData() {
        //todo dopisać test jak będzie ogarnięte zapisywanie zdjeć w pełni
    }
}