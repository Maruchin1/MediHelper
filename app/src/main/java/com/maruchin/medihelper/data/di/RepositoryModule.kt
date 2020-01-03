package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.repositories.*
import com.maruchin.medihelper.domain.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single {
        UserAuthRepoImpl(
            db = get(),
            auth = get()
        ) as UserAuthRepo
    }
    single {
        MedicineRepoImpl(
            db = get(),
            auth = get(),
            sharedPref = get(),
            storage = get(),
            mapper = get()
        ) as MedicineRepo
    }
    single {
        ProfileRepoImpl(
            db = get(),
            auth = get(),
            sharedPref = get(),
            mapper = get()
        ) as ProfileRepo
    }
    single {
        MedicinePlanRepoImpl(
            db = get(),
            auth = get(),
            mapper = get()
        ) as MedicinePlanRepo
    }
    single {
        PlannedMedicineRepoImpl(
            db = get(),
            auth = get(),
            mapper = get()
        ) as PlannedMedicineRepo
    }
    single {
        SettingsRepoImpl(
            sharedPref = get()
        ) as SettingsRepo
    }
}