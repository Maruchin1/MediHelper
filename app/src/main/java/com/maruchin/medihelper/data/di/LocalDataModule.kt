package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.local.DeletedHistory
import com.maruchin.medihelper.data.local.ImagesFiles
import com.maruchin.medihelper.data.local.RoomDatabase
import com.maruchin.medihelper.data.local.SharedPref
import org.koin.dsl.module

val localDataModule = module {
    single {
        get<RoomDatabase>().medicineDao()
    }
    single {
        get<RoomDatabase>().personDao()
    }
    single {
        get<RoomDatabase>().medicinePlanDao()
    }
    single {
        get<RoomDatabase>().timeDoseDao()
    }
    single {
        get<RoomDatabase>().plannedMedicineDao()
    }
    single {
        ImagesFiles(context = get())
    }
    single {
        DeletedHistory(context = get())
    }
    single {
        SharedPref(context = get())
    }
}