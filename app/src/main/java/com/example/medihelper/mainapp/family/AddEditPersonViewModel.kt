package com.example.medihelper.mainapp.family

import androidx.lifecycle.*
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.PersonEntity
import kotlinx.coroutines.launch

class AddEditPersonViewModel : ViewModel() {
    private val TAG = AddEditPersonViewModel::class.simpleName

    val personColorDisplayDataListLive: LiveData<List<PersonColorDisplayData>>
    val personNameLive = MutableLiveData<String>()
    val personColorResIDLive = MutableLiveData<Int>()
    val errorPersonNameLive = MutableLiveData<String>()
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
    }

    fun setArgs(args: AddEditPersonFragmentArgs) = viewModelScope.launch {
        if (args.editPersonID != -1) {
            editPersonID = args.editPersonID
            AppRepository.getPersonItem(args.editPersonID).let { personItem ->
                personNameLive.postValue(personItem.personName)
                personColorResIDLive.postValue(personItem.personColorResID)
            }
        } else {
            personNameLive.postValue("")
            personColorResIDLive.postValue(personColorResIDList[0])
        }
    }

    fun saveNewPerson(): Boolean {
        if (validateInputData()) {
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
            return true
        }
        return false
    }

    private fun validateInputData(): Boolean {
        var inputDataValid = true
        errorPersonNameLive.value = if (personNameLive.value.isNullOrEmpty()) {
            inputDataValid = false
            "Podanie nazwy jest wymagane"
        } else {
            null
        }
        return inputDataValid
    }

    data class PersonColorDisplayData(
        val colorResID: Int,
        val selected: Boolean
    )
}
