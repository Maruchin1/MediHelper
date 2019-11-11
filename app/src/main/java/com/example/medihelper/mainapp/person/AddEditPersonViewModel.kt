package com.example.medihelper.mainapp.person

import androidx.lifecycle.*
import com.example.medihelper.localdata.pojo.PersonEditData
import com.example.medihelper.service.PersonService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditPersonViewModel(
    private val personService: PersonService
) : ViewModel() {

    val titleLive = MutableLiveData("Dodaj osobę")
    val personColorDisplayDataListLive: LiveData<List<PersonColorDisplayData>>
    val personNameLive = MutableLiveData<String>()
    val personColorResIDLive = MutableLiveData<Int>()
    val errorPersonNameLive = MutableLiveData<String>()
    private val personColorResIDList = personService.getColorResIdList()
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
            titleLive.postValue("Edytuj osobę")
            personService.getEditData(args.editPersonID).let { editData ->
                personNameLive.postValue(editData.personName)
                personColorResIDLive.postValue(editData.personColorResId)
            }
        } else {
            personNameLive.postValue("")
            personColorResIDLive.postValue(personColorResIDList[0])
        }
    }

    fun saveNewPerson(): Boolean {
        if (validateInputData()) {
            val editData = PersonEditData(
                personId = editPersonID ?: 0,
                personName = personNameLive.value!!,
                personColorResId = personColorResIDLive.value!!
            )
            GlobalScope.launch {
                personService.save(editData)
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
