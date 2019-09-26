package com.example.medihelper.services

import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.*
import java.io.File

class ServerSyncService(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val plannedMedicineRepository: PlannedMedicineRepository,
    private val registeredUserApi: RegisteredUserApi
) {
    private val TAG = "ServerSyncService"

    suspend fun synchronizeData(authToken: String) {
        val medicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicineRepository.getEntityListToSync().map { MedicineDto.fromEntity(it, appFilesDir) },
            deleteRemoteIdList = medicineRepository.getDeletedRemoteIDList()
        )
        val responseMedicineDtoList = registeredUserApi.synchronizeMedicines(authToken, medicinesSyncRequestDto)
        dispatchMedicinesChanges(responseMedicineDtoList)

        val personsSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = personRepository.getEntityListToSync().map { PersonDto.fromEntity(it) },
            deleteRemoteIdList = personRepository.getDeletedRemoteIDList()
        )
        val responsePersonDtoList = registeredUserApi.synchronizePersons(authToken, personsSyncRequestDto)
        dispatchPersonsChanges(responsePersonDtoList)

        val medicinesPlansSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = medicinePlanRepository.getEntityListToSync().map {
                MedicinePlanDto.fromEntity(it, medicineRepository, personRepository)
            },
            deleteRemoteIdList = medicinePlanRepository.getDeletedRemoteIDList()
        )
        val responseMedicinePlanDtoList = registeredUserApi.synchronizeMedicinesPlans(authToken, medicinesPlansSyncRequestDto)
        dispatchMedicinesPlansChanges(responseMedicinePlanDtoList)

        val plannedMedicinesSyncRequestDto = SyncRequestDto(
            insertUpdateDtoList = plannedMedicineRepository.getEntityListToSync().map {
                PlannedMedicineDto.fromEntity(it, medicinePlanRepository)
            },
            deleteRemoteIdList = plannedMedicineRepository.getDeletedRemoteIDList()
        )
        val responsePlannedMedicineDtoList = registeredUserApi.synchronizePlannedMedicines(authToken, plannedMedicinesSyncRequestDto)
        dispatchPlannedMedicinesChanges(responsePlannedMedicineDtoList)
    }

    private suspend fun dispatchMedicinesChanges(medicineDtoList: List<MedicineDto>) {
        val remoteIdList = medicineDtoList.map { it.medicineRemoteId!! }
        medicineRepository.deleteByRemoteIDNotIn(remoteIdList)

        medicineDtoList.forEach { medicineDto ->
            val medicineEntity = medicineDto.toEntity(appFilesDir)
            if (medicineEntity.medicineID != 0) {
                medicineRepository.update(medicineEntity)
            } else {
                val existingMedicineID = medicineRepository.getIDByRemoteID(medicineEntity.medicineRemoteID!!)
                if (existingMedicineID != null) {
                    medicineRepository.update(medicineEntity.copy(medicineID = existingMedicineID))
                } else {
                    medicineRepository.insert(medicineEntity)
                }
            }
        }
        medicineRepository.clearDeletedRemoteIDList()
    }

    private suspend fun dispatchPersonsChanges(personDtoList: List<PersonDto>) {
        val remoteIdList = personDtoList.map { it.personRemoteId!! }
        personRepository.deleteByRemoteIDNotIn(remoteIdList)

        personDtoList.forEach { personDto ->
            val personEntity = personDto.toEntity()
            if (personEntity.personID != 0) {
                personRepository.update(personEntity)
            } else {
                val existingPersonID = personRepository.getIDByRemoteID(personEntity.personRemoteID!!)
                if (existingPersonID != null) {
                    personRepository.update(personEntity.copy(personID = existingPersonID))
                } else {
                    personRepository.insert(personEntity)
                }
            }
        }
        personRepository.clearDeletedRemoteIDList()
    }

    private suspend fun dispatchMedicinesPlansChanges(medicinePlanDtoList: List<MedicinePlanDto>) {
        val remoteIdList = medicinePlanDtoList.map { it.medicinePlanRemoteId!! }
        medicinePlanRepository.deleteByRemoteIDNotIn(remoteIdList)

        medicinePlanDtoList.forEach { medicinePlanDto ->
            val medicinePlanEntity = medicinePlanDto.toEntity(medicineRepository, personRepository)
            if (medicinePlanEntity.medicinePlanID != 0) {
                medicinePlanRepository.update(medicinePlanEntity)
            } else {
                val existingMedicinePlanID = medicinePlanRepository.getIDByRemoteID(medicinePlanEntity.medicinePlanRemoteID!!)
                if (existingMedicinePlanID != null) {
                    medicinePlanRepository.update(medicinePlanEntity.copy(medicinePlanID = existingMedicinePlanID))
                } else {
                    medicinePlanRepository.insert(medicinePlanEntity)
                }
            }
        }
        medicinePlanRepository.clearDeletedRemoteIDList()
    }

    private suspend fun dispatchPlannedMedicinesChanges(plannedMedicineDtoList: List<PlannedMedicineDto>) {
        val remoteIdList = plannedMedicineDtoList.map { it.plannedMedicineRemoteId!! }
        plannedMedicineRepository.deleteByRemoteIDNotIn(remoteIdList)

        plannedMedicineDtoList.forEach { plannedMedicineDto ->
            val plannedMedicineEntity = plannedMedicineDto.toEntity(medicinePlanRepository)
            if (plannedMedicineEntity.plannedMedicineID != 0) {
                plannedMedicineRepository.update(plannedMedicineEntity)
            } else {
                val existingPlannedMedicineID = plannedMedicineRepository.getIDByRemoteID(plannedMedicineEntity.plannedMedicineRemoteID!!)
                if (existingPlannedMedicineID != null) {
                    plannedMedicineRepository.update(plannedMedicineEntity.copy(plannedMedicineID = existingPlannedMedicineID))
                } else {
                    plannedMedicineRepository.insert(plannedMedicineEntity)
                }
            }
        }
        plannedMedicineRepository.clearDeletedRemoteIDList()
    }
}