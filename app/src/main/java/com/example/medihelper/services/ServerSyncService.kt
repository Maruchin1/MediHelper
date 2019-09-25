package com.example.medihelper.services

import android.util.Log
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.remotedatabase.pojos.MedicineDto
import com.example.medihelper.remotedatabase.pojos.MedicinePlanDto
import com.example.medihelper.remotedatabase.pojos.PersonDto
import com.example.medihelper.remotedatabase.pojos.SyncRequestDto
import com.example.medihelper.remotedatabase.remoterepositories.MedicinePlanRemoteRepository
import com.example.medihelper.remotedatabase.remoterepositories.MedicineRemoteRepository
import com.example.medihelper.remotedatabase.remoterepositories.PersonRemoteRepository
import com.example.medihelper.remotedatabase.pojos.medicine.MedicinePostDto
import com.example.medihelper.remotedatabase.pojos.medicineplan.MedicinePlanPostDto
import com.example.medihelper.remotedatabase.pojos.person.PersonPostDto
import com.example.medihelper.remotedatabase.pojos.plannedmedicine.PlannedMedicinePostDto
import com.example.medihelper.remotedatabase.remoterepositories.PlannedMedicineRemoteRepository
import java.io.File

class ServerSyncService(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository,
    private val medicineRemoteRepository: MedicineRemoteRepository,
    private val personRepository: PersonRepository,
    private val personRemoteRepository: PersonRemoteRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val medicinePlanRemoteRepository: MedicinePlanRemoteRepository,
    private val plannedMedicineRepository: PlannedMedicineRepository,
    private val plannedMedicineRemoteRepository: PlannedMedicineRemoteRepository
) {
    private val TAG = "ServerSyncService"

    suspend fun overwriteRemote(authToken: String) {
        Log.i(TAG, "overwriteRemote")

        medicineRemoteRepository.overwriteMedicines(
            authToken = authToken,
            postDtoList = medicineRepository.getEntityList().map { MedicinePostDto.fromMedicineEntity(it, appFilesDir) }
        ).forEach { postResponseDto ->
            val medicineEntity = medicineRepository.getEntity(postResponseDto.localId)
            medicineEntity.medicineRemoteID = postResponseDto.remoteId
            medicineRepository.update(medicineEntity)
        }

        personRemoteRepository.overwritePersons(
            authToken = authToken,
            postDtoList = personRepository.getEntityList().map { PersonPostDto.fromPersonEntity(it) }
        ).forEach { postResponseDto ->
            val personEntity = personRepository.getEntity(postResponseDto.localId)
            personEntity.personRemoteID = postResponseDto.remoteId
            personRepository.update(personEntity)
        }

        medicinePlanRemoteRepository.overwriteMedicinesPlans(
            authToken = authToken,
            postDtoList = medicinePlanRepository.getEntityList().map {
                MedicinePlanPostDto.fromMedicinePlanEntity(it, medicineRepository, personRepository)
            }
        ).forEach { postResponseDto ->
            val medicinePlanEntity = medicinePlanRepository.getEntity(postResponseDto.localId)
            medicinePlanEntity.medicinePlanRemoteID = postResponseDto.remoteId
            medicinePlanRepository.update(medicinePlanEntity)
        }

        plannedMedicineRemoteRepository.overwritePlannedMedicines(
            authToken = authToken,
            postDtoList = plannedMedicineRepository.getEntityList().map {
                PlannedMedicinePostDto.fromPlannedMedicineEntity(it, medicinePlanRepository)
            }
        ).forEach { postResponseDto ->
            val plannedMedicineEntity = plannedMedicineRepository.getEntity(postResponseDto.localId)
            plannedMedicineEntity.plannedMedicineRemoteID = postResponseDto.remoteId
            plannedMedicineRepository.update(plannedMedicineEntity)
        }
    }

    suspend fun overwriteLocal(authToken: String) {
        Log.i(TAG, "overwriteLocal")
        medicineRepository.deleteAll()
        personRepository.deleteAll()
        medicinePlanRepository.deleteAll()

        val medicineEntityList = medicineRemoteRepository.getAllMedicines(authToken).map { it.toMedicineEntity(appFilesDir) }
        medicineRepository.insert(medicineEntityList)

        val personEntityList = personRemoteRepository.getAllPersons(authToken).map { it.toPersonEntity() }
        personRepository.insert(personEntityList)

        val medicinePlanEntityList = medicinePlanRemoteRepository.getAllMedicinesPlans(authToken).map {
            it.toMedicinePlanEntity(medicineRepository, personRepository)
        }
        medicinePlanRepository.insert(medicinePlanEntityList)

        val plannedMedicineEntityList = plannedMedicineRemoteRepository.getAllPlannedMedicines(authToken).map {
            it.toPlannedMedicineEntity(medicinePlanRepository)
        }
        plannedMedicineRepository.insert(plannedMedicineEntityList)
    }

    suspend fun syncWithServer(authToken: String) {
        val medicinesSyncRequestDto = SyncRequestDto(
            deleteRemoteIdList = medicineRepository.getDeletedRemoteIDList(),
            insertUpdateDtoList = medicineRepository.getEntityListToSync().map { MedicineDto.fromMedicineEntity(it, appFilesDir) }
        )
        val personsSyncRequestDto = SyncRequestDto(
            deleteRemoteIdList = personRepository.getDeletedRemoteIDList(),
            insertUpdateDtoList = personRepository.getEntityListToSync().map { PersonDto.fromPersonEntity(it) }
        )
        val medicinePlanSyncRequestDto = SyncRequestDto(
            deleteRemoteIdList = medicinePlanRepository.getDeletedRemoteIDList(),
            insertUpdateDtoList = medicinePlanRepository.getEntityListToSync().map {
                MedicinePlanDto.fromMedicinePlanEntity(it, medicineRepository, personRepository)
            }
        )


        val remoteMedicineDtoList = medicineRemoteRepository.synchronizeMedicines(authToken, medicinesSyncRequestDto)
        val remotePersonDtoList = personRemoteRepository.synchronizePersons(authToken, personsSyncRequestDto)
        val remoteMedicinePlanDtoList = medicinePlanRemoteRepository.synchronizeMedicinesPlans(authToken, medicinePlanSyncRequestDto)

        medicineRepository.run {
            deleteAll()
            insert(remoteMedicineDtoList.map { it.toMedicineEntity(appFilesDir) })
            clearDeletedRemoteIDList()
        }
        personRepository.run {
            deleteAll()
            insert(remotePersonDtoList.map { it.toPersonEntity() })
            clearDeletedRemoteIDList()
        }
        medicinePlanRepository.run {
            deleteAll()
            insert(remoteMedicinePlanDtoList.map { it.toMedicinePlanEntity(medicineRepository, personRepository) })
            clearDeletedRemoteIDList()
        }
    }

//    private suspend fun syncMedicines(authToken: String) {
//        val medicinesSyncRequestDto = SyncRequestDto(
//            deleteRemoteIdList = medicineRepository.getDeletedRemoteIDList(),
//            insertUpdateDtoList = medicineRepository.getEntityListToSync().map { MedicineDto.fromMedicineEntity(it, appFilesDir) }
//        )
//        val remoteMedicineDtoList = medicineRemoteRepository.synchronizeMedicines(authToken, medicinesSyncRequestDto)
//        remoteMedicineDtoList.forEach { medicineDto ->
//            if (medicineDto.medicineLocalId != null) {
//                val medicineEntity = medicineDto.toMedicineEntity(
//                    medicineID = medicineDto.medicineLocalId,
//                    appFilesDir = appFilesDir)
//                medicineRepository.update(medicineEntity)
//            } else {
//                val existingMedicineID = medicineRepository.getIDByRemoteID(medicineDto.medicineRemoteId!!)
//                val medicineEntity = medicineDto.toMedicineEntity(
//                    medicineID = existingMedicineID ?: 0,
//                    appFilesDir = appFilesDir)
//                if (existingMedicineID == null) {
//                    medicineRepository.insert(medicineEntity)
//                } else {
//                    medicineRepository.update(medicineEntity)
//                }
//            }
//        }
//
//        val medicineRemoteIdList = remoteMedicineDtoList.map { it.medicineRemoteId!! }
//        medicineRepository.deleteAllByRemoteIDNotIn(medicineRemoteIdList)
//    }
}