package com.example.medihelper

import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.example.medihelper.service.DateTimeService
import com.example.medihelper.services.StatusOfTakingService
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given

class StatusOfTakingServiceTest : KoinTest {

    private val statusOfTakingService: StatusOfTakingService by inject()

    @Before
    fun before() {
        startKoin { modules(listOf(appModule, repositoryModule, serviceModule)) }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun statusOfTakingService_SetMedicineTaken_FromWaitingToTaken() {
        val plannedMedicineEntity = PlannedMedicineEntity(
            medicinePlanID = 1,
            plannedDate = AppDate(2019, 10, 1),
            plannedTime = AppTime(8, 30),
            plannedDoseSize = 2f,
            statusOfTaking = PlannedMedicineEntity.StatusOfTaking.WAITING
        )
        val status = statusOfTakingService.getStatusByTaken(plannedMedicineEntity, true)
        assertThat(status).isEqualTo(PlannedMedicineEntity.StatusOfTaking.TAKEN)
    }

    @Test
    fun statusOfTakingService_SetMedicineTaken_FromNotTakenToTaken() {
        val plannedMedicineEntity = PlannedMedicineEntity(
            medicinePlanID = 1,
            plannedDate = AppDate(2019, 10, 1),
            plannedTime = AppTime(8, 30),
            plannedDoseSize = 2f,
            statusOfTaking = PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN
        )
        val status = statusOfTakingService.getStatusByTaken(plannedMedicineEntity, true)
        assertThat(status).isEqualTo(PlannedMedicineEntity.StatusOfTaking.TAKEN)
    }

    @Test
    fun statusOfTakingService_SetMedicineTaken_FromTakenToWaiting() {
        val plannedMedicineEntity = PlannedMedicineEntity(
            medicinePlanID = 1,
            plannedDate = AppDate(2019, 10, 1),
            plannedTime = AppTime(8, 30),
            plannedDoseSize = 2f,
            statusOfTaking = PlannedMedicineEntity.StatusOfTaking.TAKEN
        )

        declareMock<DateTimeService> {
            given(this.getCurrDate()).willReturn(AppDate(2019, 10, 1))
            given(this.getCurrTime()).willReturn(AppTime(7, 0))
        }

        val status = statusOfTakingService.getStatusByTaken(plannedMedicineEntity, false)
        assertThat(status).isEqualTo(PlannedMedicineEntity.StatusOfTaking.WAITING)
    }

    @Test
    fun statusOfTakingService_SetMedicineTaken_FromTakenToNotTaken() {
        val plannedMedicineEntity = PlannedMedicineEntity(
            medicinePlanID = 1,
            plannedDate = AppDate(2019, 10, 1),
            plannedTime = AppTime(8, 30),
            plannedDoseSize = 2f,
            statusOfTaking = PlannedMedicineEntity.StatusOfTaking.TAKEN
        )

        declareMock<DateTimeService> {
            given(this.getCurrDate()).willReturn(AppDate(2019, 10, 1))
            given(this.getCurrTime()).willReturn(AppTime(10, 0))
        }

        val status = statusOfTakingService.getStatusByTaken(plannedMedicineEntity, false)
        assertThat(status).isEqualTo(PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN)
    }

    @Test
    fun statusOfTakingService_UpdateStatusByCurrDate_FromWaitingToNotTaken() {
        val plannedMedicineEntity = PlannedMedicineEntity(
            medicinePlanID = 1,
            plannedDate = AppDate(2019, 10, 1),
            plannedTime = AppTime(8, 30),
            plannedDoseSize = 2f,
            statusOfTaking = PlannedMedicineEntity.StatusOfTaking.WAITING
        )

        declareMock<DateTimeService> {
            given(this.getCurrDate()).willReturn(AppDate(2019, 10, 1))
            given(this.getCurrTime()).willReturn(AppTime(8, 31))
        }

        val status = statusOfTakingService.getStatusByCurrDate(plannedMedicineEntity)
        assertThat(status).isEqualTo(PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN)
    }

    @Test
    fun statusOfTakingService_UpdateStatusByCurrDate_FromTakenToNoChange() {
        val plannedMedicineEntity = PlannedMedicineEntity(
            medicinePlanID = 1,
            plannedDate = AppDate(2019, 10, 1),
            plannedTime = AppTime(8, 30),
            plannedDoseSize = 2f,
            statusOfTaking = PlannedMedicineEntity.StatusOfTaking.TAKEN
        )

        declareMock<DateTimeService> {
            given(this.getCurrDate()).willReturn(AppDate(2019, 10, 1))
            given(this.getCurrTime()).willReturn(AppTime(8, 31))
        }

        val status = statusOfTakingService.getStatusByCurrDate(plannedMedicineEntity)
        assertThat(status).isEqualTo(PlannedMedicineEntity.StatusOfTaking.TAKEN)
    }
}