package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PictureRepo
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.plans.DeletePlansUseCase
import com.maruchin.medihelper.testingframework.mock
import com.maruchin.medihelper.testingframework.verifyInvocations
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito

class DeleteMedicineUseCaseImplTest {

    private val medicineRepo: MedicineRepo = mock()
    private val pictureRepo: PictureRepo = mock()
    private val planRepo: PlanRepo = mock()
    private val deletePlansUseCase: DeletePlansUseCase = mock()

    private lateinit var useCase: DeleteMedicineUseCase

    @Before
    fun before() {
        useCase = DeleteMedicineUseCaseImpl(
            medicineRepo,
            pictureRepo,
            planRepo,
            deletePlansUseCase
        )
    }

    @Test
    fun execute_medicinePictureExists() {
        val medicineId = "ABCD"

        runBlocking {
            val mockMedicine = Medicine(
                entityId = "ABCD",
                name = "Lek",
                unit = "tabletki",
                expireDate = AppExpireDate(2020, 6),
                type = "Na katar",
                state = MedicineState(
                    packageSize = 0f,
                    currState = 0f
                ),
                pictureName = "picture.jpg"
            )
            val medicinesPlansMock = listOf(
                Plan(
                    entityId = "mmm",
                    profileId = "ppp",
                    medicineId = "ABCD",
                    planType = Plan.Type.ONE_DAY,
                    startDate = AppDate(2019, 11, 3),
                    endDate = null,
                    intakeDays = null,
                    timeDoseList = listOf(
                        TimeDose(
                            time = AppTime(9, 30),
                            doseSize = 2f
                        )
                    )
                )
            )
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(mockMedicine)
            Mockito.`when`(planRepo.getListByMedicine(medicineId)).thenReturn(medicinesPlansMock)

            useCase.execute(medicineId)

            verifyInvocations(pictureRepo, invocations = 1).deleteMedicinePicture("picture.jpg")
            verifyInvocations(medicineRepo, invocations = 1).deleteById("ABCD")
            verifyInvocations(
                deletePlansUseCase,
                invocations = 1
            ).execute(medicinesPlansMock.map { it.entityId })
        }
    }

    @Test
    fun execute_medicinePictureNotExists() {
        val medicineId = "ABCD"

        runBlocking {
            val mockMedicine = Medicine(
                entityId = "ABCD",
                name = "Lek",
                unit = "tabletki",
                expireDate = AppExpireDate(2020, 6),
                type = "Na katar",
                state = MedicineState(
                    packageSize = 0f,
                    currState = 0f
                ),
                pictureName = null
            )
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(mockMedicine)
            Mockito.`when`(planRepo.getListByMedicine(medicineId)).thenReturn(emptyList())

            useCase.execute(medicineId)

            Mockito.verify(pictureRepo, Mockito.times(0)).deleteMedicinePicture(Mockito.anyString())
            Mockito.verify(medicineRepo, Mockito.times(1)).deleteById("ABCD")
            verifyInvocations(deletePlansUseCase, invocations = 1).execute(Mockito.anyList())
        }
    }
}