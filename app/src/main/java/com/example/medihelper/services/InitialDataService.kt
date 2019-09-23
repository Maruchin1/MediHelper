package com.example.medihelper.services

import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.repositories.PersonRepository

class InitialDataService(
    private val sharedPrefService: SharedPrefService,
    private val personRepository: PersonRepository
) {
    suspend fun checkInitialData() {
        if (personRepository.getMainPersonID() == null) {
            personRepository.insert(getInitialPerson())
        }
        if (sharedPrefService.getMedicineUnitList().isNullOrEmpty()) {
            sharedPrefService.saveMedicineUnitList(getInitialMedicineUnitList())
        }
        if (sharedPrefService.getPersonColorResIDList().isNullOrEmpty()) {
            sharedPrefService.savePersonColorResIDList(getInitialPersonColorResIDList())
        }
    }

    private fun getInitialPerson() = PersonEntity(
        personName = "Ja",
        personColorResID = R.color.colorPrimary,
        mainPerson = true
    )

    private fun getInitialMedicineUnitList() = listOf("dawki", "pigułki", "ml", "g", "mg", "krople")

    private fun getInitialPersonColorResIDList() = listOf(
        R.color.colorPersonBlue,
        R.color.colorPersonBrown,
        R.color.colorPersonCyan,
        R.color.colorPersonGray,
        R.color.colorPersonLightGreen,
        R.color.colorPersonOrange,
        R.color.colorPersonPurple,
        R.color.colorPersonRed,
        R.color.colorPersonYellow
    )
}