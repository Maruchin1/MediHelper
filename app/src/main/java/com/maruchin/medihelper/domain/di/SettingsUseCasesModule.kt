package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.saved_types.*
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
        GetLiveMedicineUnitsUseCaseImpl(
            medicineUnitRepo = get()
        ) as GetLiveMedicineUnitsUseCase
    }
    factory {
        GetLiveMedicineTypesUseCaseImpl(
            medicineTypeRepo = get()
        ) as GetLiveMedicineTypesUseCase
    }
    factory {
        DeleteMedicineTypeUseCaseImpl(
            medicineTypeRepo = get()
        ) as DeleteMedicineTypeUseCase
    }
    factory {
        DeleteMedicineUnitUseCaseImpl(
            medicineUnitRepo = get()
        ) as DeleteMedicineUnitUseCase
    }
    factory {
        AddMedicineUnitUseCaseImpl(
            medicineUnitRepo = get()
        ) as AddMedicineUnitUseCase
    }
    factory {
        AddMedicineTypeUseCaseImpl(
            medicineTypeRepo = get()
        ) as AddMedicineTypeUseCase
    }
}