package com.example.medihelper.service

import com.example.medihelper.R
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.dao.PersonDao
import com.example.medihelper.localdata.entity.PersonEntity
import com.example.medihelper.localdata.pojo.PersonEditData

interface InitialDataService {
    suspend fun checkInitialData()
}

class InitialDataServiceImpl(
    private val personDao: PersonDao,
    private val appSharedPref: AppSharedPref
) : InitialDataService {

    override suspend fun checkInitialData() {
        if (personDao.getMainPersonID() == null) {
            personDao.insert(getInitialPerson())
        }
        if (appSharedPref.getMedicineUnitList().isNullOrEmpty()) {
            appSharedPref.saveMedicineUnitList(getInitialMedicineUnitList())
        }
        if (appSharedPref.getColorResIdList().isNullOrEmpty()) {
            appSharedPref.saveColorResIdList(getInitialPersonColorResIDList())
        }
    }

    private fun getInitialPerson() = PersonEntity(
        personId = 0,
        personName = "Użytkownik",
        personColorResId = R.color.colorPrimary,
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