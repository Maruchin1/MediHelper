package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.mappers.MedicineMapper
import com.maruchin.medihelper.data.mappers.PlannedMedicineMapper
import com.maruchin.medihelper.data.mappers.ProfileMapper
import com.maruchin.medihelper.data.mappers.UserMapper
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
        UserMapper()
    }
}