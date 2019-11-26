package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.sync.EntityDtoMapper
import com.maruchin.medihelper.data.sync.LocalDatabaseDispatcher
import org.koin.dsl.module

val syncModule = module {
    single {
        EntityDtoMapper(
            medicineDao = get(),
            personDao = get(),
            medicinePlanDao = get()
        )
    }
    single {
        LocalDatabaseDispatcher(
            entityDtoMapper = get(),
            medicineDao = get(),
            personDao = get(),
            medicinePlanDao = get(),
            plannedMedicineDao = get(),
            deletedHistory = get()
        )
    }
}