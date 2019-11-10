package com.example.medihelper.service

import com.example.medihelper.R
import com.example.medihelper.localdatabase.entity.PersonEntity
import com.example.medihelper.localdatabase.pojo.PersonEditData

interface InitialDataService {
    suspend fun checkInitialData()
}

class InitialDataServiceImpl(
    private val personService: PersonService,
    private val medicineService: MedicineService
) : InitialDataService {

    override suspend fun checkInitialData() {
        if (personService.getMainPersonID() == null) {
            personService.save(getInitialPerson())
        }
        if (medicineService.getMedicineUnitList().isNullOrEmpty()) {
            medicineService.saveMedicineUnitList(getInitialMedicineUnitList())
        }
        if (personService.getColorResIdList().isNullOrEmpty()) {
            personService.saveColorResIdList(getInitialPersonColorResIDList())
        }
    }

    private fun getInitialPerson() = PersonEditData(
        personID = 0,
        personName = "Użytkownik",
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