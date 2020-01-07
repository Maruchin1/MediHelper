package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.data.utils.ProfileColor
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.user.InitDefaultsUseCase

class InitDefaultsUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val medicineUnitRepo: MedicineUnitRepo,
    private val medicineTypeRepo: MedicineTypeRepo
) : InitDefaultsUseCase {

    override suspend fun execute(userName: String) {
        initMainProfile(userName)
        initMedicineUnits()
        initMedicineTypes()
    }

    private suspend fun initMainProfile(profileName: String) {
        val mainProfile = getMainProfile(profileName)
        profileRepo.addNew(mainProfile)
    }

    private suspend fun initMedicineUnits() {
        val units = getMedicineUnits()
        medicineUnitRepo.init(units)
    }

    private suspend fun initMedicineTypes() {
        val types = getMedicineTypes()
        medicineTypeRepo.init(types)
    }

    private fun getMainProfile(profileName: String) = Profile(
        entityId = "",
        name = profileName,
        color = ProfileColor.MAIN.colorString,
        mainPerson = true
    )

    private fun getMedicineUnits() = listOf(
        "dawki", "tabletki", "pigułki", "pastylki", "g", "mg", "ml"
    )

    private fun getMedicineTypes() = listOf(
        "Alergia",
        "Kaszel",
        "Katar",
        "Antybiotyk",
        "Probiotyk",
        "Ból głowy",
        "Ból brzucha"
    )
}