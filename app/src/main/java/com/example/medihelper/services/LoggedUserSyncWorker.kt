package com.example.medihelper.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.remotedatabase.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.*
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
        val personsSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = personRepository.getEntityListToSync().map {
                PersonDto.fromEntity(it)
            },
            deleteRemoteIdList = personRepository.getDeletedRemoteIDList()
        )
        val responsePersonDtoList =
            registeredUserApi.synchronizePersons(authToken, personsSyncRequestDto)
        repositoryDispatcherService.dispatchPersonsChanges(responsePersonDtoList)

        val medicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicineRepository.getEntityListToSync().map {
                MedicineDto.fromEntity(it, appFilesDir)
            },
            deleteRemoteIdList = medicineRepository.getDeletedRemoteIDList()
        )
        val responseMedicineDtoList =
            registeredUserApi.synchronizeMedicines(authToken, medicinesSyncRequestDto)
        repositoryDispatcherService.dispatchMedicinesChanges(responseMedicineDtoList)

        val medicinesPlansSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicinePlanRepository.getEntityListToSync().map {
                MedicinePlanDto.fromEntity(it, medicineRepository, personRepository)
            },
            deleteRemoteIdList = medicinePlanRepository.getDeletedRemoteIDList()
        )
        val responseMedicinePlanDtoList =
            registeredUserApi.synchronizeMedicinesPlans(authToken, medicinesPlansSyncRequestDto)
        repositoryDispatcherService.dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val plannedMedicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = plannedMedicineRepository.getEntityListToSync().map {
                PlannedMedicineDto.fromEntity(it, medicinePlanRepository)
            },
            deleteRemoteIdList = plannedMedicineRepository.getDeletedRemoteIDList()
        )
        val responsePlannedMedicineDtoList =
            registeredUserApi.synchronizePlannedMedicines(authToken, plannedMedicinesSyncRequestDto)
        repositoryDispatcherService.dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }
}