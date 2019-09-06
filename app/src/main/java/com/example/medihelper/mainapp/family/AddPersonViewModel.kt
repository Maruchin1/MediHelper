package com.example.medihelper.mainapp.family

import androidx.lifecycle.*
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import kotlinx.coroutines.launch

class AddPersonViewModel : ViewModel() {
    private val TAG = AddPersonViewModel::class.simpleName

    val personColorDisplayDataListLive: LiveData<List<PersonColorDisplayData>>
    val personNameLive = MutableLiveData<String>()
    val personColorResIDLive = MutableLiveData<Int>()
    private val personColorResIDList = AppRepository.getPersonColorResIDList()
    private var editPersonID: Int? = null

    init {
        personColorDisplayDataListLive = Transformations.map(personColorResIDLive) { selectedPersonColorResID ->
            mutableListOf<PersonColorDisplayData>().apply {
                personColorResIDList.forEach { personColorResID ->
                    this.add(
                        PersonColorDisplayData(
                            colorResID = personColorResID,
                            selected = personColorResID == selectedPersonColorResID
                        )
                    )
                }
            }
        }
        personNameLive.value = ""
        personColorResIDLive.value = personColorResIDList[0]
    }

    fun setArgs(args: AddPersonFragmentArgs) = viewModelScope.launch {
        if (args.editPersonID != -1) {
            editPersonID = args.editPersonID
            AppRepository.getPersonItem(args.editPersonID).let { personItem ->
                personNameLive.postValue(personItem.personName)
                personColorResIDLive.postValue(personItem.personColorResID)
            }
        }
    }

    fun saveNewPerson() {
        //todo zrobić walidację
        val personEntity = PersonEntity(
            personName = personNameLive.value!!,
            personColorResID = personColorResIDLive.value!!
        )
        viewModelScope.launch {
            if (editPersonID != null) {
                AppRepository.updatePerson(personEntity.copy(personID = editPersonID!!))
            } else {
                AppRepository.insertPerson(personEntity)
            }
        }
    }

    data class PersonColorDisplayData(
        val colorResID: Int,
        val selected: Boolean
    )
}
