package com.example.medihelper.mainapp.persons

import androidx.lifecycle.*
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch

class AddEditPersonViewModel(
    private val personRepository: PersonRepository,
    sharedPrefService: SharedPrefService
) : ViewModel() {

    val personColorDisplayDataListLive: LiveData<List<PersonColorDisplayData>>
    val personNameLive = MutableLiveData<String>()
    val personColorResIDLive = MutableLiveData<Int>()
    val errorPersonNameLive = MutableLiveData<String>()
    private val personColorResIDList = sharedPrefService.getPersonColorResIDList()
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
            personRepository.getItem(args.editPersonID).let { personItem ->
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
                    personRepository.update(personEntity.copy(personID = editPersonID!!))
                } else {
                    personRepository.insert(personEntity)
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
