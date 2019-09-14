package com.example.medihelper


import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.medihelper.localdatabase.AppDatabase
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.localdatabase.repositoriesimpl.MedicinePlanRepositoryImpl
import com.example.medihelper.localdatabase.repositoriesimpl.MedicineRepositoryImpl
import com.example.medihelper.localdatabase.repositoriesimpl.PersonRepositoryImpl
import com.example.medihelper.localdatabase.repositoriesimpl.PlannedMedicineRepositoryImpl
import com.example.medihelper.mainapp.addeditmedicine.AddEditMedicineViewModel
import com.example.medihelper.mainapp.addeditmedicineplan.AddEditMedicinePlanViewModel
import com.example.medihelper.dialogs.SelectMedicineViewModel
import com.example.medihelper.mainapp.family.AddEditPersonViewModel
import com.example.medihelper.mainapp.family.PersonViewModel
import com.example.medihelper.mainapp.medicineplanlist.MedicinePlanHistoryViewModel
import com.example.medihelper.mainapp.medicineplanlist.MedicinePlanListViewModel
import com.example.medihelper.mainapp.medicines.MedicineDetailsViewModel
import com.example.medihelper.mainapp.medicines.MedicinesViewModel
import com.example.medihelper.mainapp.schedule.PlannedMedicineOptionsViewModel
import com.example.medihelper.mainapp.schedule.ScheduleViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


class MainApplication : Application() {
    private val TAG = MainApplication::class.simpleName

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        AppRepository.init(this)
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}

val repositoryModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single<MedicineRepository> {
        MedicineRepositoryImpl(get<AppDatabase>().medicineDao())
    }
    single<MedicinePlanRepository> {
        MedicinePlanRepositoryImpl(get<AppDatabase>().medicinePlanDao())
    }
    single<PlannedMedicineRepository> {
        PlannedMedicineRepositoryImpl(get<AppDatabase>().plannedMedicineDao())
    }
    single<PersonRepository> {
        PersonRepositoryImpl(get<AppDatabase>().personDao())
    }
}

val viewModelModule = module {
    viewModel { MedicinesViewModel(get()) }
    viewModel { AddEditMedicineViewModel(get()) }
    viewModel { AddEditPersonViewModel(get()) }
    viewModel { PersonViewModel(get()) }
    viewModel { MedicinePlanHistoryViewModel(get()) }
    viewModel { MedicinePlanListViewModel(get()) }
    viewModel { MedicineDetailsViewModel(get(), get()) }
    viewModel { AddEditMedicinePlanViewModel(get(), get()) }
    viewModel { SelectMedicineViewModel(get()) }
    viewModel { PlannedMedicineOptionsViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get()) }
}