package com.example.medihelper.data.di

import com.example.medihelper.data.repositories.*
import com.example.medihelper.domain.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single {
        AppUserRepoImpl(
            context = get(),
            sharedPref = get(),
            authenticationApi = get(),
            registeredUserApi = get(),
            apiResponseMapper = get(),
            deletedHistory = get()
        ) as AppUserRepo
    }
    single {
        MedicineRepoImpl(
            medicineDao = get(),
            sharedPref = get(),
            deletedHistory = get(),
            imagesFiles = get()
        ) as MedicineRepo
    }
    single {
        PersonRepoImpl(
            personDao = get(),
            sharedPref = get(),
            deletedHistory = get()
        ) as PersonRepo
    }
    single {
        MedicinePlanRepoImpl(
            medicinePlanDao = get(),
            timeDoseDao = get(),
            deletedHistory = get(),
            imagesFiles = get()
        ) as MedicinePlanRepo
    }
    single {
        PlannedMedicineRepoImpl(
            plannedMedicineDao = get(),
            deletedHistory = get(),
            imagesFiles = get()
        ) as PlannedMedicineRepo
    }
}