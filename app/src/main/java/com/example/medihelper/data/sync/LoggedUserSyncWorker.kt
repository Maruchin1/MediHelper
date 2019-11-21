package com.example.medihelper.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.data.local.DeletedHistory
import com.example.medihelper.data.local.dao.*
import com.example.medihelper.data.remote.api.RegisteredUserApi
import com.example.medihelper.data.remote.dto.SyncRequestDto
import com.example.medihelper.device.notifications.NotificationUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoggedUserSyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    companion object {
        const val KEY_AUTH_TOKEN = "key-auth-token"
    }

    private val registeredUserApi: RegisteredUserApi by inject()
    private val notificationUtil: NotificationUtil by inject()
    private val personDao: PersonDao by inject()
    private val medicineDao: MedicineDao by inject()
    private val medicinePlanDao: MedicinePlanDao by inject()
    private val timeDoseDao: TimeDoseDao by inject()
    private val plannedMedicineDao: PlannedMedicineDao by inject()
    private val localDatabaseDispatcher: LocalDatabaseDispatcher by inject()
    private val entityDtoMapper: EntityDtoMapper by inject()
    private val deletedHistory: DeletedHistory by inject()

    override suspend fun doWork(): Result {
        notificationUtil.showServerSyncNotification()
        var result = Result.failure()
        val authToken = inputData.getString(KEY_AUTH_TOKEN)

        if (authToken != null) {
            try {
                synchronizeData(authToken)
                result = Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                notificationUtil.showServerSyncFailNotification()
            }
        }
        notificationUtil.cancelServerSyncNotification()
        return result
    }

    private suspend fun synchronizeData(authToken: String) {
        //todo poprawić z nową bazą
        val personsSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = personDao.getEntityListToSync().map {
                entityDtoMapper.personEntityToDto(it)
            },
            deleteRemoteIdList = deletedHistory.getPersonHistory()
        )
        val responsePersonDtoList =
            registeredUserApi.synchronizePersons(authToken, personsSyncRequestDto)
        localDatabaseDispatcher.dispatchPersonsChanges(responsePersonDtoList)

        val medicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicineDao.getEntityListToSync().map {
                entityDtoMapper.medicineEntityToDto(it)
            },
            deleteRemoteIdList = deletedHistory.getMedicineHistory()
        )
        val responseMedicineDtoList =
            registeredUserApi.synchronizeMedicines(authToken, medicinesSyncRequestDto)
        localDatabaseDispatcher.dispatchMedicinesChanges(responseMedicineDtoList)

        val medicinesPlansSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicinePlanDao.getEntityListToSync().map { medicinePlanEntity ->
                val timeDoseList = timeDoseDao.getEntityListByMedicinePlanId(medicinePlanEntity.medicinePlanId)
                val timeDoseDtoList = timeDoseList.map { entityDtoMapper.timeDoseEntityToDto(it) }
                entityDtoMapper.medicinePlanEntityToDto(medicinePlanEntity, timeDoseDtoList)
            },
            deleteRemoteIdList = deletedHistory.getMedicinePlanHistory()
        )
        val responseMedicinePlanDtoList =
            registeredUserApi.synchronizeMedicinesPlans(authToken, medicinesPlansSyncRequestDto)
        localDatabaseDispatcher.dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val plannedMedicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = plannedMedicineDao.getEntityListToSync().map {
                entityDtoMapper.plannedMedicineEntityToDto(it)
            },
            deleteRemoteIdList = deletedHistory.getPlannedMedicineHistory()
        )
        val responsePlannedMedicineDtoList =
            registeredUserApi.synchronizePlannedMedicines(authToken, plannedMedicinesSyncRequestDto)
        localDatabaseDispatcher.dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }
}