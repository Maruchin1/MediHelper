package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.repositories.*
import com.maruchin.medihelper.domain.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single {
        UserAuthRepoImpl(
            appFirebase = get(),
            auth = get()
        ) as UserAuthRepo
    }
    single {
        MedicineRepoImpl(
            appFirebase = get(),
            dataSharedPref = get(),
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
}