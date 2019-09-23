package com.example.medihelper.services

import android.util.Log
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.MedicineRemoteRepository
import com.example.medihelper.remotedatabase.PersonRemoteRepository
import com.example.medihelper.remotedatabase.pojos.MedicinePostDto
import com.example.medihelper.remotedatabase.pojos.PersonPostDto
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ServerSyncService(
    private val appFilesDir: File,
    private val medicineRepository: MedicineRepository,
    private val medicineRemoteRepository: MedicineRemoteRepository,
    private val personRepository: PersonRepository,
    private val personRemoteRepository: PersonRemoteRepository
) {
    private val TAG = "ServerSyncService"

    suspend fun overwriteRemote(authToken: String) {
        Log.i(TAG, "overwriteRemote")
        val operationTime = getCurrOperationTime()

        val medicinePostDtoList = medicineRepository.getEntityList().map { medicineEntity ->
            MedicinePostDto(medicineEntity, operationTime, appFilesDir)
        }
        medicineRemoteRepository.overwriteMedicines(authToken, medicinePostDtoList)

        val personPostDtoList = personRepository.getEntityList().map { personEntity -> PersonPostDto(personEntity, operationTime) }
        personRemoteRepository.overwritePersons(authToken, personPostDtoList)
    }

    suspend fun overwriteLocal(authToken: String) {
        Log.i(TAG, "overwriteLocal")
        medicineRepository.deleteAll()
        personRepository.deleteAll()

        val medicineEntityList = medicineRemoteRepository.getAllMedicines(authToken).map { medicineGetDto ->
            medicineGetDto.toMedicineEntity(appFilesDir)
        }
        val personEntityList = personRemoteRepository.getAllPersons(authToken).map { personGetDto -> personGetDto.toPersonEntity() }

        medicineRepository.insert(medicineEntityList)
        personRepository.insert(personEntityList)
    }

    private fun getCurrOperationTime() = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
}