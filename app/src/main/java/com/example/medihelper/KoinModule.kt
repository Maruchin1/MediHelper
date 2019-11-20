package com.example.medihelper

import androidx.room.Room
import androidx.work.WorkManager
import com.example.medihelper.data.repositories.AppUserRepoImpl
import com.example.medihelper.data.repositories.MedicineRepoImpl
import com.example.medihelper.data.repositories.PersonRepoImpl
import com.example.medihelper.domain.repositories.AppUserRepo
import com.example.medihelper.domain.repositories.MedicineRepo
import com.example.medihelper.domain.repositories.PersonRepo
import com.example.medihelper.domain.usecases.MedicinePlanUseCases
import com.example.medihelper.domain.usecases.MedicineUseCases
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.domain.usecases.ServerConnectionUseCases
import com.example.medihelper.localdata.*
import com.example.medihelper.utility.StatusOfTakingCalculator
import com.example.medihelper.mainapp.alarm.AlarmViewModel
import com.example.medihelper.mainapp.authentication.LoginViewModel
import com.example.medihelper.mainapp.launcher.MainPersonViewModel
import com.example.medihelper.mainapp.authentication.RegisterViewModel
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanViewModel
import com.example.medihelper.mainapp.authentication.PatronConnectViewModel
import com.example.medihelper.mainapp.options.NewPasswordViewModel
import com.example.medihelper.mainapp.options.OptionsViewModel
import com.example.medihelper.mainapp.schedule.PlannedMedicineOptionsViewModel
import com.example.medihelper.mainapp.schedule.ScheduleDayViewModel
import com.example.medihelper.mainapp.schedule.ScheduleViewModel
import com.example.medihelper.presentation.feature.medikit.AddEditMedicineViewModel
import com.example.medihelper.presentation.feature.medikit.MedicineDetailsViewModel
import com.example.medihelper.presentation.feature.medikit.MedicinesListViewModel
import com.example.medihelper.presentation.feature.personsprofiles.AddEditPersonViewModel
import com.example.medihelper.presentation.feature.personsprofiles.PersonOptionsViewModel
import com.example.medihelper.presentation.feature.personsprofiles.PersonViewModel
import com.example.medihelper.presentation.feature.plans.MedicinePlanHistoryViewModel
import com.example.medihelper.presentation.feature.plans.MedicinePlanListViewModel
import com.example.medihelper.remotedata.api.AuthenticationApi
import com.example.medihelper.remotedata.api.ConnectedPersonApi
import com.example.medihelper.remotedata.api.RegisteredUserApi
import com.example.medihelper.serversync.EntityDtoMapper
import com.example.medihelper.serversync.LocalDatabaseDispatcher
import com.example.medihelper.service.*
import com.example.medihelper.utility.MedicineScheduler
import com.example.medihelper.utility.NotificationUtil
import com.example.medihelper.utility.ReminderUtil
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
    single<DeletedHistory> { DeletedHistoryImpl(get()) }
    single<AppSharedPref> { AppSharedPrefImpl(get()) }
    single<MedicineImageFiles> { MedicineImageFilesImpl(get()) }
}

val remoteDataModule = module {
    single { get<Retrofit>().create(AuthenticationApi::class.java) }
    single { get<Retrofit>().create(RegisteredUserApi::class.java) }
    single { get<Retrofit>().create(ConnectedPersonApi::class.java) }
    single { EntityDtoMapper(get(), get(), get()) }
    single { LocalDatabaseDispatcher(get(), get(), get(), get(), get(), get()) }
}

val utilModule = module {
    factory { MedicineScheduler() }
    factory { NotificationUtil(get()) }
    factory { ReminderUtil(get()) }
    factory { StatusOfTakingCalculator() }
    factory { WorkManager.getInstance(get()) }
}

val serviceModule = module {
    single<PersonService> { PersonServiceImpl(get(), get(), get()) }
    single<MedicineService> { MedicineServiceImpl(get(), get(), get(), get()) }
    single<MedicinePlanService> { MedicinePlanServiceImpl(get(), get(), get(), get()) }
    single<PlannedMedicineService> { PlannedMedicineServiceImpl(get(), get(), get(), get(), get(), get(), get()) }
    single<DateTimeService> { DateTimeServiceImpl() }
    single<InitialDataService> { InitialDataServiceImpl(get(), get()) }
    single<LoadingScreenService> { LoadingScreenServiceImpl() }
    single<ServerApiService> {
        ServerApiServiceImpl(get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    single { FormValidatorService() }
}

val repoModule = module {
    single<AppUserRepo> { AppUserRepoImpl() }
    single<MedicineRepo> { MedicineRepoImpl() }
    single<PersonRepo> { PersonRepoImpl() }
}

val useCaseModule = module {
    single { MedicineUseCases(get()) }
    single { PersonUseCases(get()) }
    single { ServerConnectionUseCases(get()) }
    single { MedicinePlanUseCases(get()) }
}

val viewModelModule = module {
    viewModel { MedicinesListViewModel(get(), get(), get()) }
    viewModel { MedicineDetailsViewModel(get(), get(), get()) }
    viewModel { AddEditMedicineViewModel(get()) }
    viewModel { PersonViewModel(get()) }
    viewModel { PersonOptionsViewModel(get(), get()) }
    viewModel { AddEditPersonViewModel(get()) }
    viewModel { MedicinePlanListViewModel(get(), get(), get(), get()) }
    viewModel { MedicinePlanHistoryViewModel(get(), get(), get()) }



    viewModel { AddEditMedicinePlanViewModel(get(), get(), get(), get(), get()) }
    viewModel { PlannedMedicineOptionsViewModel(get(), get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get(), get()) }
    viewModel { PatronConnectViewModel(get()) }
    viewModel { ScheduleDayViewModel(get(), get()) }
    viewModel { AlarmViewModel(get()) }
    viewModel { MainPersonViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { OptionsViewModel(get(), get()) }
    viewModel { NewPasswordViewModel() }
}