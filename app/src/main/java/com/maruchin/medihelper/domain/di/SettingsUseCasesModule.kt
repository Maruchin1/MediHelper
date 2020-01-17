package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.settings.*
import com.maruchin.medihelper.domain.usecasesimpl.settings.*
import org.koin.dsl.module

val settingsUseCasesModule = module {
    factory {
        AreNotificationsEnabledUseCaseImpl(
            settingsRepo = get()
        ) as AreNotificationsEnabledUseCase

    }
    factory {
        AreLiveNotificationsEnabledUseCaseImpl(
            settingsRepo = get()
        ) as AreLiveNotificationsEnabledUseCase
    }
    factory {
        SetNotificationsEnabledUseCaseImpl(
            settingsRepo = get(),
            deviceNotifications = get()
        ) as SetNotificationsEnabledUseCase
    }
    factory {
        GetLiveSavedMedicineUnitsUseCaseImpl(
            medicineUnitRepo = get()
        ) as GetLiveSavedMedicineUnitsUseCase
    }
    factory {
        GetLiveSavedMedicineTypesUseCaseImpl(
            medicineTypeRepo = get()
        ) as GetLiveSavedMedicineTypesUseCase
    }
}