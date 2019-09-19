package com.example.medihelper


import android.app.Application
import android.content.Context
import android.os.Environment
import android.preference.PreferenceManager
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
import com.example.medihelper.mainapp.loginregister.LoginRegisterViewModel
import com.example.medihelper.mainapp.medicineplanlist.MedicinePlanHistoryViewModel
import com.example.medihelper.mainapp.medicineplanlist.MedicinePlanListViewModel
import com.example.medihelper.mainapp.medicines.MedicineDetailsViewModel
import com.example.medihelper.mainapp.medicines.MedicinesViewModel
import com.example.medihelper.mainapp.more.MoreViewModel
import com.example.medihelper.mainapp.schedule.PlannedMedicineOptionsViewModel
import com.example.medihelper.mainapp.schedule.ScheduleViewModel
import com.example.medihelper.remotedatabase.RegisteredUserRemoteRepository
import com.example.medihelper.services.MedicineSchedulerService
import com.example.medihelper.services.PersonProfileService
import com.example.medihelper.services.PhotoFileService
import com.example.medihelper.services.SharedPrefService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    repositoryModule,
                    remoteRepositoryModule,
                    viewModelModule,
                    serviceModule
                )
            )
        }
        runBlocking {
            val sharedPrefService: SharedPrefService = get()
            sharedPrefService.checkInitialDataLoaded()
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

val remoteRepositoryModule = module {
    single<RegisteredUserRemoteRepository> {
        Retrofit.Builder()
            .baseUrl(REGISTERED_USER_REMOTE_REPOSITORY_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(RegisteredUserRemoteRepository::class.java)
    }
}

val viewModelModule = module {
    viewModel { MedicinesViewModel(get()) }
    viewModel { AddEditMedicineViewModel(get(), get(), get()) }
    viewModel { AddEditPersonViewModel(get(), get()) }
    viewModel { PersonViewModel(get(), get()) }
    viewModel { MedicinePlanHistoryViewModel(get(), get()) }
    viewModel { MedicinePlanListViewModel(get(), get()) }
    viewModel { MedicineDetailsViewModel(get(), get()) }
    viewModel { AddEditMedicinePlanViewModel(get(), get(), get(), get()) }
    viewModel { SelectMedicineViewModel(get()) }
    viewModel { PlannedMedicineOptionsViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { MoreViewModel(get()) }
    viewModel { LoginRegisterViewModel(get(), get()) }
}

val serviceModule = module {
    single { SharedPrefService(PreferenceManager.getDefaultSharedPreferences(androidContext()), get()) }
    single { PersonProfileService(get(), get()) }
    single { MedicineSchedulerService(get(), get()) }
    single { PhotoFileService(androidContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)) }
}

private const val REGISTERED_USER_REMOTE_REPOSITORY_URL = "https://medihelper-api.herokuapp.com/registered-users/"