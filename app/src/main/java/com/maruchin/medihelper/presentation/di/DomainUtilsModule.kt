package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.utils.*
import org.koin.dsl.module

val domainUtilsModule = module {
    factory {
        PlannedMedicineScheduler()
    }
    factory {
        ProfileValidator()
    }
    factory {
        MedicineValidator()
    }
    factory {
        MedicinePlanValidator()
    }
    factory {
        SignInValidator()
    }
}