package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.utils.MedicinePlanValidator
import com.maruchin.medihelper.domain.utils.MedicineValidator
import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler
import com.maruchin.medihelper.domain.utils.ProfileValidator
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
}