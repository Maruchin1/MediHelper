package com.example.medihelper

import androidx.room.Room
import androidx.work.WorkManager
import com.example.medihelper.localdata.*
import com.example.medihelper.utility.StatusOfTakingCalculator
import com.example.medihelper.mainapp.alarm.AlarmViewModel
import com.example.medihelper.mainapp.authentication.LoginViewModel
import com.example.medihelper.mainapp.launcher.MainPersonViewModel
import com.example.medihelper.mainapp.authentication.RegisterViewModel
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanViewModel
import com.example.medihelper.mainapp.medicineplan.MedicinePlanHistoryViewModel
import com.example.medihelper.mainapp.medicineplan.MedicinePlanListViewModel
import com.example.medihelper.mainapp.medicine.AddEditMedicineViewModel
import com.example.medihelper.mainapp.medicine.MedicineDetailsViewModel
import com.example.medihelper.mainapp.medicine.MedicinesViewModel
import com.example.medihelper.mainapp.authentication.PatronConnectViewModel
import com.example.medihelper.mainapp.options.NewPasswordViewModel
import com.example.medihelper.mainapp.options.OptionsViewModel
import com.example.medihelper.mainapp.person.AddEditPersonViewModel
import com.example.medihelper.mainapp.person.PersonOptionsViewModel
import com.example.medihelper.mainapp.person.PersonViewModel
import com.example.medihelper.mainapp.schedule.PlannedMedicineOptionsViewModel
import com.example.medihelper.mainapp.schedule.ScheduleDayViewModel
import com.example.medihelper.mainapp.schedule.ScheduleViewModel
import com.example.medihelper.remotedata.api.AuthenticationApi
import com.example.medihelper.remotedata.api.ConnectedPersonApi
import com.example.medihelper.remotedata.api.RegisteredUserApi
import com.example.medihelper.serversync.EntityDtoMapper
import com.example.medihelper.serversync.LocalDatabaseDispatcher
import com.example.medihelper.service.*
import com.example.medihelper.utility.MedicineScheduler
import com.google.gson.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainRoomModule = module(override = true) {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.MAIN_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

val connectedRoomModule = module(override = true) {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.CONNECTED_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

val retrofitModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://medihelper-api.herokuapp.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
    }
}

val localDataModule = module(override = true) {
    single { get<AppDatabase>().medicineDao() }
    single { get<AppDatabase>().personDao() }
    single { get<AppDatabase>().medicinePlanDao() }
    single { get<AppDatabase>().timeDoseDao() }
    single { get<AppDatabase>().plannedMedicineDao() }
    single<DeletedHistory> { DeletedHistoryImpl(androidContext()) }
    single<AppSharedPref> { AppSharedPrefImpl(androidContext()) }
    single<MedicineImageFiles> { MedicineImageFilesImpl(androidContext()) }
    single { StatusOfTakingCalculator() }
    single { MedicineScheduler() }
}

val remoteDataModule = module {
    single { get<Retrofit>().create(AuthenticationApi::class.java) }
    single { get<Retrofit>().create(RegisteredUserApi::class.java) }
    single { get<Retrofit>().create(ConnectedPersonApi::class.java) }
    single { EntityDtoMapper(get(), get(), get()) }
    single { LocalDatabaseDispatcher(get(), get(), get(), get(), get(), get()) }
}

val serviceModule = module {
    single<PersonService> { PersonServiceImpl(get(), get(), get()) }
    single<MedicineService> { MedicineServiceImpl(get(), get(), get(), get()) }
    single<MedicinePlanService> { MedicinePlanServiceImpl(get(), get(), get(), get()) }
    single<PlannedMedicineService> { PlannedMedicineServiceImpl(get(), get(), get(), get(), get(), get()) }
    single<AlarmService> { AlarmServiceImpl(get(), get()) }
    single<DateTimeService> { DateTimeServiceImpl() }
    single<InitialDataService> { InitialDataServiceImpl(get(), get()) }
    single<LoadingScreenService> { LoadingScreenServiceImpl() }
    single<NotificationService> { NotificationServiceImpl(get()) }
    single<ServerApiService> {
        ServerApiServiceImpl(get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }


    single { FormValidatorService() }

    single { WorkManager.getInstance(androidContext()) }
}

val viewModelModule = module {
    viewModel { MedicinesViewModel(get(), get(), get()) }
    viewModel { AddEditMedicineViewModel(get(), get()) }
    viewModel { AddEditPersonViewModel(get(), get()) }
    viewModel { PersonViewModel(get()) }
    viewModel { MedicinePlanHistoryViewModel(get(), get(), get()) }
    viewModel { MedicinePlanListViewModel(get(), get(), get(), get()) }
    viewModel { MedicineDetailsViewModel(get(), get(), get()) }
    viewModel { AddEditMedicinePlanViewModel(get(), get(), get(), get(), get()) }
    viewModel { PlannedMedicineOptionsViewModel(get(), get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get(), get()) }
    viewModel { PersonOptionsViewModel(get(), get()) }
    viewModel { PatronConnectViewModel(get()) }
    viewModel { ScheduleDayViewModel(get(), get()) }
    viewModel { AlarmViewModel(get()) }
    viewModel { MainPersonViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { OptionsViewModel(get(), get()) }
    viewModel { NewPasswordViewModel() }
}