package com.maruchin.medihelper.domain.usecases

import com.maruchin.medihelper.domain.repositories.ProfileRepo

class PersonUseCases(
    private val profileRepo: ProfileRepo
) {
//    private val currPersonId = MediatorLiveData<Int>()
//    private val currPerson: LiveData<Profile>

//    init {
//        currPersonId.addSource(profileRepo.getMainIdLive()) { currPersonId.value = it }
//        currPerson = Transformations.switchMap(currPersonId) { profileId ->
//            profileId?.let { profileRepo.getLiveById(it)  }
//        }
//    }
//
//    suspend fun addNewPerson(inputData: PersonInputData) {
//        val newPerson = Profile(
//            profileId = 0,
//            name = inputData.name,
//            color = inputData.color,
//            mainPerson = false,
//            connectionKey = null
//        )
//        profileRepo.insert(newPerson)
//    }
//
//    suspend fun addMainPerson(profileName: String) {
//        val mainPerson = Profile(
//            profileId = 0,
//            name = profileName,
//            color = R.color.colorPrimary,
//            mainPerson = true,
//            connectionKey = null
//        )
//        profileRepo.insert(mainPerson)
//    }
//
//    suspend fun updatePerson(id: Int, inputData: PersonInputData) {
//        val existingPerson = profileRepo.getById(id)
//        val updatedPerson = existingPerson.copy(
//            name = inputData.name,
//            color = inputData.color
//        )
//        profileRepo.update(updatedPerson)
//    }
//
//
//    suspend fun updateMainPerson(profileName: String) {
//        profileRepo.getMain()?.let { mainPerson ->
//            val updatedMain = mainPerson.copy(name = profileName)
//            profileRepo.update(updatedMain)
//        }
//    }
//
//    suspend fun deletePersonById(id: Int) = profileRepo.deleteById(id)
//
//    suspend fun getPersonById(id: Int): Profile = profileRepo.getById(id)
//
//    suspend fun mainPersonExists(): Boolean = profileRepo.getMainId() != null
//
//    fun getPersonLiveById(id: Int) = profileRepo.getLiveById(id)
//
//    fun getAllPersonListLive(): LiveData<List<Profile>> = profileRepo.getAllListLive()
//
//    fun getMainPersonColorLive(): LiveData<Int> = profileRepo.getMainPersonColorIdLive()
//
//    fun getPersonListLiveByMedicineId(id: Int): LiveData<List<Profile>> = profileRepo.getListLiveByMedicineId(id)
//
//    fun getCurrPersonLive() = currPerson
//
//    fun getColorIdList() = profileRepo.getColorIdList()
//
//    fun selectCurrPerson(id: Int) = currPersonId.postValue(id)
}