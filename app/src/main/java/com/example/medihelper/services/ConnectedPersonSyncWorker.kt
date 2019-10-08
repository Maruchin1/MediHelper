package com.example.medihelper.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.remotedatabase.ConnectedPersonApi
import com.example.medihelper.remotedatabase.dto.MedicineDto
import com.example.medihelper.remotedatabase.dto.MedicinePlanDto
import com.example.medihelper.remotedatabase.dto.PlannedMedicineDto
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ConnectedPersonSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params),
    KoinComponent {

    private val sharedPrefService: SharedPrefService by inject()
    private val medicinePlanRepository: MedicinePlanRepository by inject()
    private val plannedMedicineRepository: PlannedMedicineRepository by inject()
    private val connectedPersonApi: ConnectedPersonApi by inject()
    private val notificationService: NotificationService by inject()
    private val repositoryDispatcherService: RepositoryDispatcherService by inject()

    override suspend fun doWork(): Result {
        notificationService.showServerSyncNotification()
        var result = Result.failure()
        sharedPrefService.getAuthToken()?.let { authToken ->
            try {
                synchronizeData(authToken)
                result = Result.success()
                sharedPrefService.saveLastSyncTimeLive(Date())
            } catch (e: Exception) {
                e.printStackTrace()
                notificationService.showServerSyncFailureNotification()
            }
        }
        notificationService.cancelServerSyncNotification()
        return result
    }

    private suspend fun synchronizeData(authToken: String) {
        val responseMedicineDtoList = connectedPersonApi.getMedicines(authToken)
        repositoryDispatcherService.dispatchMedicinesChanges(responseMedicineDtoList)

        val responseMedicinePlanDtoList = connectedPersonApi.getMedicinesPlans(authToken)
        repositoryDispatcherService.dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val updatedPlannedMedicineDtoList = plannedMedicineRepository.getEntityListToSync().map {
            PlannedMedicineDto.fromEntity(it, medicinePlanRepository)
        }
        val responsePlannedMedicineDtoList =
            connectedPersonApi.synchronizePlannedMedicines(authToken, updatedPlannedMedicineDtoList)
        repositoryDispatcherService.dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }
}