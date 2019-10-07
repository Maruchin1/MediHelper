package com.example.medihelper.mainapp.more.patronconnect

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.MainApplication
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectedPersonViewModel(
    private val mainApplication: MainApplication,
    private val sharedPrefService: SharedPrefService,
    private val personRepository: PersonRepository,
    private val medicineRepository: MedicineRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val plannedMedicineRepository: PlannedMedicineRepository
) : ViewModel() {

    val personNameLive: LiveData<String>
    val personColorResID: LiveData<Int>
    private val mainPersonItemLive = personRepository.getMainPersonItemLive()

    init {
        personNameLive = Transformations.map(mainPersonItemLive) { it.personName }
        personColorResID = Transformations.map(mainPersonItemLive) { it.personColorResID }
    }

    fun cancelConnection() = GlobalScope.launch {
        sharedPrefService.deleteAuthToken()
        resetDatabase()
        mainApplication.switchToMainDatabase()
    }

    private suspend fun resetDatabase() = listOf(
        personRepository,
        medicineRepository,
        medicinePlanRepository,
        plannedMedicineRepository
    ).forEach {
        it.deleteAll()
    }
}