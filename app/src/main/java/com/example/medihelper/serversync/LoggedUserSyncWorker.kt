package com.example.medihelper.serversync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.remotedatabase.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.*
import com.example.medihelper.services.NotificationService
import com.example.medihelper.services.SharedPrefService
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class LoggedUserSyncWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val appFilesDir by lazy { context.filesDir }
    private val sharedPrefService: SharedPrefService by inject()
    private val medicineRepository: MedicineRepository by inject()
    private val personRepository: PersonRepository by inject()
    private val medicinePlanRepository: MedicinePlanRepository by inject()
    private val plannedMedicineRepository: PlannedMedicineRepository by inject()
    private val registeredUserApi: RegisteredUserApi by inject()
    private val notificationService: NotificationService by inject()

    private val localDatabaseDispatcher: LocalDatabaseDispatcher by inject()
    private val entityDtoConverter: EntityDtoConverter by inject()

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
        val personsSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = personRepository.getEntityListToSync().map {
                entityDtoConverter.personEntityToDto(it)
            },
            deleteRemoteIdList = personRepository.getDeletedRemoteIDList()
        )
        val responsePersonDtoList =
            registeredUserApi.synchronizePersons(authToken, personsSyncRequestDto)
        localDatabaseDispatcher.dispatchPersonsChanges(responsePersonDtoList)

        val medicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicineRepository.getEntityListToSync().map {
                entityDtoConverter.medicineEntityToDto(it)
            },
            deleteRemoteIdList = medicineRepository.getDeletedRemoteIDList()
        )
        val responseMedicineDtoList =
            registeredUserApi.synchronizeMedicines(authToken, medicinesSyncRequestDto)
        localDatabaseDispatcher.dispatchMedicinesChanges(responseMedicineDtoList)

        val medicinesPlansSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicinePlanRepository.getEntityListToSync().map {
                entityDtoConverter.medicinePlanEntityToDto(it)
            },
            deleteRemoteIdList = medicinePlanRepository.getDeletedRemoteIDList()
        )
        val responseMedicinePlanDtoList =
            registeredUserApi.synchronizeMedicinesPlans(authToken, medicinesPlansSyncRequestDto)
        localDatabaseDispatcher.dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val plannedMedicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = plannedMedicineRepository.getEntityListToSync().map {
                entityDtoConverter.plannedMedicineEntityToDto(it)
            },
            deleteRemoteIdList = plannedMedicineRepository.getDeletedRemoteIDList()
        )
        val responsePlannedMedicineDtoList =
            registeredUserApi.synchronizePlannedMedicines(authToken, plannedMedicinesSyncRequestDto)
        localDatabaseDispatcher.dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }
}