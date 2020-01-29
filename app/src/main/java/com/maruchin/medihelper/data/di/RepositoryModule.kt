package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.repositories.*
import com.maruchin.medihelper.domain.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single {
        UserRepoImpl(
            appFirebase = get(),
            auth = get()
        ) as UserRepo
    }
    single {
        MedicineRepoImpl(
            appFirebase = get(),
            mapper = get()
        ) as MedicineRepo
    }
    single {
        ProfileRepoImpl(
            appFirebase = get(),
            dataSharedPref = get(),
            mapper = get()
        ) as ProfileRepo
    }
    single {
        PlanRepoImpl(
            appFirebase = get(),
            mapper = get()
        ) as PlanRepo
    }
    single {
        PlannedMedicineRepoImpl(
            appFirebase = get(),
            mapper = get()
        ) as PlannedMedicineRepo
    }
    single {
        SettingsRepoImpl(
            settingsSharedPref = get()
        ) as SettingsRepo
    }
    single {
        PictureRepoImpl(
            appFirebase = get()
        ) as PictureRepo
    }
    single {
        MedicineUnitRepoImpl(
            appFirebase = get(),
            mapper = get()
        ) as MedicineUnitRepo
    }
    single {
        MedicineTypeRepoImpl(
            appFirebase = get(),
            mapper = get()
        ) as MedicineTypeRepo
    }
}