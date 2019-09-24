package com.example.medihelper.services

import android.util.Log
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.pojos.PostResponseDto
import com.example.medihelper.remotedatabase.remoterepositories.MedicinePlanRemoteRepository
import com.example.medihelper.remotedatabase.remoterepositories.MedicineRemoteRepository
import com.example.medihelper.remotedatabase.remoterepositories.PersonRemoteRepository
import com.example.medihelper.remotedatabase.pojos.medicine.MedicinePostDto
import com.example.medihelper.remotedatabase.pojos.medicineplan.MedicinePlanGetDto
import com.example.medihelper.remotedatabase.pojos.medicineplan.MedicinePlanPostDto
import com.example.medihelper.remotedatabase.pojos.medicineplan.TimeOfTakingDto
import com.example.medihelper.remotedatabase.pojos.person.PersonPostDto
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ServerSyncService(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository,
    private val medicineRemoteRepository: MedicineRemoteRepository,
    private val personRepository: PersonRepository,
    private val personRemoteRepository: PersonRemoteRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val medicinePlanRemoteRepository: MedicinePlanRemoteRepository
) {
    private val TAG = "ServerSyncService"

    suspend fun overwriteRemote(authToken: String) {
        Log.i(TAG, "overwriteRemote")

        medicineRemoteRepository.overwriteMedicines(
            authToken = authToken,
            medicinePostDtoList = medicineRepository.getEntityList().map { MedicinePostDto.fromMedicineEntity(it, appFilesDir) }
        ).forEach { postResponseDto ->
            val medicineEntity = medicineRepository.getEntity(postResponseDto.localId)
            medicineEntity.medicineRemoteID = postResponseDto.remoteId
            medicineRepository.update(medicineEntity)
        }

        personRemoteRepository.overwritePersons(
            authToken = authToken,
            personPostDtoList = personRepository.getEntityList().map { PersonPostDto.fromPersonEntity(it) }
        ).forEach { postResponseDto ->
            val personEntity = personRepository.getEntity(postResponseDto.localId)
            personEntity.personRemoteID = postResponseDto.remoteId
            personRepository.update(personEntity)
        }

        medicinePlanRemoteRepository.overwriteMedicinesPlans(
            authToken = authToken,
            medicinePlanPostDtoList = medicinePlanRepository.getEntityList().map {
                MedicinePlanPostDto.fromMedicinePlanEntity(it, medicineRepository, personRepository)
            }
        ).forEach { postResponseDto ->
            val medicinePlanEntity = medicinePlanRepository.getEntity(postResponseDto.localId)
            medicinePlanEntity.medicinePlanRemoteID = postResponseDto.remoteId
            medicinePlanRepository.update(medicinePlanEntity)
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
    }
}