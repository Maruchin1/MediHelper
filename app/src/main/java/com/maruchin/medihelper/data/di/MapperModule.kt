package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.mappers.*
import org.koin.dsl.module

val mapperModule = module {
    factory {
        PlannedMedicineMapper()
    }
    factory {
        ProfileMapper()
    }
    factory {
        MedicineMapper()
    }
    factory {
        MedicinePlanMapper()
    }
    factory {
        TypeContainerMapper()
    }
}