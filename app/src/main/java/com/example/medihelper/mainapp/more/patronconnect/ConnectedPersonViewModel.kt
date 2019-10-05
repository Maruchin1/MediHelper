package com.example.medihelper.mainapp.more.patronconnect

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectedPersonViewModel(
    private val sharedPrefService: SharedPrefService,
    private val personRepository: PersonRepository
) : ViewModel() {

    val personNameLive: LiveData<String>
    val personColorResID: LiveData<Int>
    private val mainPersonItemLive =  personRepository.getMainPersonItemLive()

    init {
        personNameLive = Transformations.map(mainPersonItemLive) { it.personName }
        personColorResID = Transformations.map(mainPersonItemLive) { it.personColorResID }
    }

    fun cancelConnection() = GlobalScope.launch {
        sharedPrefService.deleteAuthToken()
    }
}