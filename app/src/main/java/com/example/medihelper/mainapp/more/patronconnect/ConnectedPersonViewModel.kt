package com.example.medihelper.mainapp.more.patronconnect

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.MainApplication
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectedPersonViewModel(
    private val mainApplication: MainApplication,
    private val sharedPrefService: SharedPrefService,
    private val personRepository: PersonRepository,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    val personNameLive: LiveData<String>
    val personColorResID: LiveData<Int>
    private val mainPersonItemLive = personRepository.getMainPersonItemLive()

    init {
        personNameLive = Transformations.map(mainPersonItemLive) { it?.personName }
        personColorResID = Transformations.map(mainPersonItemLive) { it?.personColorResID }
    }

    fun cancelConnection(mainActivity: MainActivity) = GlobalScope.launch {
        sharedPrefService.deleteAuthToken()
        resetDatabase()
        mainApplication.switchToMainDatabase()
        mainActivity.restartActivity()
    }

    private suspend fun resetDatabase() {
        medicineRepository.deleteAll()
        personRepository.deleteAllWithMain()
    }
}