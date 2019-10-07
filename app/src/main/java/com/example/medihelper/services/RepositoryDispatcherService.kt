package com.example.medihelper.services

import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.remotedatabase.dto.MedicineDto
import com.example.medihelper.remotedatabase.dto.MedicinePlanDto
import com.example.medihelper.remotedatabase.dto.PersonDto
import com.example.medihelper.remotedatabase.dto.PlannedMedicineDto
import java.io.File

class RepositoryDispatcherService(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val plannedMedicineRepository: PlannedMedicineRepository
) {
    suspend fun dispatchMedicinesChanges(medicineDtoList: List<MedicineDto>) {
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

    suspend fun dispatchPersonsChanges(personDtoList: List<PersonDto>) {
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

    suspend fun dispatchMedicinesPlansChanges(medicinePlanDtoList: List<MedicinePlanDto>) {
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

    suspend fun dispatchPlannedMedicinesChanges(plannedMedicineDtoList: List<PlannedMedicineDto>) {
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