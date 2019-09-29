package com.example.medihelper.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class ServerSyncWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val appFilesDir by lazy { context.filesDir }
    private val sharedPrefService: SharedPrefService by inject()
    private val medicineRepository: MedicineRepository by inject()
    private val personRepository: PersonRepository by inject()
    private val medicinePlanRepository: MedicinePlanRepository by inject()
    private val plannedMedicineRepository: PlannedMedicineRepository by inject()
    private val registeredUserApi: RegisteredUserApi by inject()
    private val notificationService: NotificationService by inject()

    override suspend fun doWork(): Result {
        notificationService.showServerSyncNotification()
        var result = Result.failure()
        sharedPrefService.getLoggedUserAuthToken()?.let { authToken ->
            try {
                synchronizeData(authToken)
                result = Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                notificationService.showServerSyncFailureNotification()
            }
        }
        notificationService.cancelServerSyncNotification()
        return result
    }

    private suspend fun synchronizeData(authToken: String) {
        val medicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicineRepository.getEntityListToSync().map {
                MedicineDto.fromEntity(it, appFilesDir)
            },
            deleteRemoteIdList = medicineRepository.getDeletedRemoteIDList()
        )
        val responseMedicineDtoList =
            registeredUserApi.synchronizeMedicines(authToken, medicinesSyncRequestDto)
        dispatchMedicinesChanges(responseMedicineDtoList)

        val personsSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = personRepository.getEntityListToSync().map {
                PersonDto.fromEntity(it)
            },
            deleteRemoteIdList = personRepository.getDeletedRemoteIDList()
        )
        val responsePersonDtoList =
            registeredUserApi.synchronizePersons(authToken, personsSyncRequestDto)
        dispatchPersonsChanges(responsePersonDtoList)

        val medicinesPlansSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicinePlanRepository.getEntityListToSync().map {
                MedicinePlanDto.fromEntity(it, medicineRepository, personRepository)
            },
            deleteRemoteIdList = medicinePlanRepository.getDeletedRemoteIDList()
        )
        val responseMedicinePlanDtoList =
            registeredUserApi.synchronizeMedicinesPlans(authToken, medicinesPlansSyncRequestDto)
        dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val plannedMedicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = plannedMedicineRepository.getEntityListToSync().map {
                PlannedMedicineDto.fromEntity(it, medicinePlanRepository)
            },
            deleteRemoteIdList = plannedMedicineRepository.getDeletedRemoteIDList()
        )
        val responsePlannedMedicineDtoList =
            registeredUserApi.synchronizePlannedMedicines(authToken, plannedMedicinesSyncRequestDto)
        dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }

    private suspend fun dispatchMedicinesChanges(medicineDtoList: List<MedicineDto>) {
        val remoteIdList = medicineDtoList.map { it.medicineRemoteId!! }
        val updateList = mutableListOf<MedicineEntity>()
        val insertList = mutableListOf<MedicineEntity>()
        medicineDtoList.forEach { medicineDto ->
            val medicineEntity = medicineDto.toEntity(appFilesDir)
            if (medicineEntity.medicineID != 0) {
                updateList.add(medicineEntity)
            } else {
                val existingMedicineID =
                    medicineRepository.getLocalIDByRemoteID(medicineEntity.medicineRemoteID!!)
                if (existingMedicineID != null) {
                    updateList.add(medicineEntity.copy(medicineID = existingMedicineID))
                } else {
                    insertList.add(medicineEntity)
                }
            }
        }
        applyChangesToRepository(medicineRepository, remoteIdList, updateList, insertList)
    }

    private suspend fun dispatchPersonsChanges(personDtoList: List<PersonDto>) {
        val remoteIdList = personDtoList.map { it.personRemoteId!! }
        val updateList = mutableListOf<PersonEntity>()
        val insertList = mutableListOf<PersonEntity>()
        personDtoList.forEach { personDto ->
            val personEntity = personDto.toEntity()
            if (personEntity.personID != 0) {
                updateList.add(personEntity)
            } else {
                val existingPersonID =
                    personRepository.getLocalIDByRemoteID(personEntity.personRemoteID!!)
                if (existingPersonID != null) {
                    updateList.add(personEntity.copy(personID = existingPersonID))
                } else {
                    insertList.add(personEntity)
                }
            }
        }
        applyChangesToRepository(personRepository, remoteIdList, updateList, insertList)
    }

    private suspend fun dispatchMedicinesPlansChanges(medicinePlanDtoList: List<MedicinePlanDto>) {
        val remoteIdList = medicinePlanDtoList.map { it.medicinePlanRemoteId!! }
        val updateList = mutableListOf<MedicinePlanEntity>()
        val insertList = mutableListOf<MedicinePlanEntity>()
        medicinePlanDtoList.forEach { medicinePlanDto ->
            val medicinePlanEntity = medicinePlanDto.toEntity(medicineRepository, personRepository)
            if (medicinePlanEntity.medicinePlanID != 0) {
                updateList.add(medicinePlanEntity)
            } else {
                val existingMedicinePlanID =
                    medicinePlanRepository.getLocalIDByRemoteID(medicinePlanEntity.medicinePlanRemoteID!!)
                if (existingMedicinePlanID != null) {
                    updateList.add(medicinePlanEntity.copy(medicinePlanID = existingMedicinePlanID))
                } else {
                    insertList.add(medicinePlanEntity)
                }
            }
        }
        applyChangesToRepository(medicinePlanRepository, remoteIdList, updateList, insertList)
    }

    private suspend fun dispatchPlannedMedicinesChanges(plannedMedicineDtoList: List<PlannedMedicineDto>) {
        val remoteIdList = plannedMedicineDtoList.map { it.plannedMedicineRemoteId!! }
        val updateList = mutableListOf<PlannedMedicineEntity>()
        val insertList = mutableListOf<PlannedMedicineEntity>()
        plannedMedicineDtoList.forEach { plannedMedicineDto ->
            val plannedMedicineEntity = plannedMedicineDto.toEntity(medicinePlanRepository)
            if (plannedMedicineEntity.plannedMedicineID != 0) {
                updateList.add(plannedMedicineEntity)
            } else {
                val existingPlannedMedicineID =
                    plannedMedicineRepository.getLocalIDByRemoteID(plannedMedicineEntity.plannedMedicineRemoteID!!)
                if (existingPlannedMedicineID != null) {
                    updateList.add(plannedMedicineEntity.copy(plannedMedicineID = existingPlannedMedicineID))
                } else {
                    insertList.add(plannedMedicineEntity)
                }
            }
        }
        applyChangesToRepository(plannedMedicineRepository, remoteIdList, updateList, insertList)
    }

    private suspend fun <T> applyChangesToRepository(
        serverSyncRepository: ServerSyncRepository<T>,
        remoteIdList: List<Long>,
        updateEntityList: List<T>,
        insertEntityList: List<T>
    ) = serverSyncRepository.run {
        deleteByRemoteIDNotIn(remoteIdList)
        updateSynchronized(updateEntityList)
        insertSynchronized(insertEntityList)
        clearDeletedRemoteIDList()
    }
}