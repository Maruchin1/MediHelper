package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.repositories.PictureRepo
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.domain.utils.MedicineValidator
import com.maruchin.medihelper.testingframework.mock
import org.junit.Before
import org.junit.Test


class SaveMedicineUseCaseImplTest {

    private val medicineRepo: MedicineRepo = mock()
    private val pictureRepo: PictureRepo = mock()
    private val medicineUnitRepo: MedicineUnitRepo = mock()
    private val medicineTypeRepo: MedicineTypeRepo = mock()
    private val validator: MedicineValidator = mock()

    private lateinit var useCase: SaveMedicineUseCase

    @Before
    fun before() {
        useCase = SaveMedicineUseCaseImpl(
            medicineRepo,
            pictureRepo,
            medicineUnitRepo,
            medicineTypeRepo,
            validator
        )
    }

    @Test
    fun execute_CorrectData() {
        //todo dopisać test jak będzie ogarnięte zapisywanie zdjeć w pełni
    }
}