package com.example.medihelper.mainapp.more.patronconnect

import androidx.lifecycle.*
import com.example.medihelper.localdata.pojo.PersonItem
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectedPersonViewModel(
    private val personService: PersonService,
    private val serverApiService: ServerApiService
) : ViewModel() {

    val personNameLive: LiveData<String>
    val personColorResID: LiveData<Int>
    private val mainPersonItemLive = MutableLiveData<PersonItem>()

    init {
        viewModelScope.launch {
            val personItem = personService.getMainPersonId()?.let { personService.getItem(it) }
            mainPersonItemLive.postValue(personItem)
        }
        personNameLive = Transformations.map(mainPersonItemLive) { it?.personName }
        personColorResID = Transformations.map(mainPersonItemLive) { it?.personColorResId }
    }

    fun cancelConnection(mainActivity: MainActivity) = GlobalScope.launch {
        serverApiService.cancelPatronConnection()
        mainActivity.restartActivity()
    }
}