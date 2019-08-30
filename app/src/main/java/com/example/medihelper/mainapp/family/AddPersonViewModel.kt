package com.example.medihelper.mainapp.family

import androidx.lifecycle.*
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem

class AddPersonViewModel : ViewModel() {
    private val TAG = AddPersonViewModel::class.simpleName

    val personColorDisplayDataListLive: LiveData<List<PersonColorDisplayData>>
    val personNameLive = MutableLiveData<String>()
    val personColorResIDLive = MutableLiveData<Int>()

    private val editPersonIDLive = MutableLiveData<Int>()
    private val editPersonItemLive: LiveData<PersonItem>
    private val editPersonItemObserver: Observer<PersonItem>
    private val personColorResIDList = AppRepository.getPersonColorResIDList()

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
        editPersonItemLive = Transformations.switchMap(editPersonIDLive) { personID ->
            AppRepository.getPersonItemLive(personID)
        }
        editPersonItemObserver = Observer { personItem ->
            personNameLive.value = personItem?.personName
            personColorResIDLive.value = personItem?.personColorResID ?: personColorResIDList[0]
        }
        editPersonItemLive.observeForever(editPersonItemObserver)
    }

    fun setArgs(args: AddPersonActivityArgs) {
        editPersonIDLive.value = args.editPersonID
    }

    fun saveNewPerson() {
        //todo zrobić walidację
        val personEntity = PersonEntity(
            personName = personNameLive.value!!,
            personColorResID = personColorResIDLive.value!!
        )
        val editPersonItem = editPersonItemLive.value
        if (editPersonItem != null) {
            AppRepository.updatePerson(personEntity.copy(personID = editPersonItem.personID))
        } else {
            AppRepository.insertPerson(personEntity)
        }
    }

    override fun onCleared() {
        super.onCleared()
        editPersonItemLive.removeObserver(editPersonItemObserver)
    }

    data class PersonColorDisplayData(
        val colorResID: Int,
        val selected: Boolean
    )
}
