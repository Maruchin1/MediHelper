package com.example.medihelper


import android.app.Application
import android.os.Environment
import android.preference.PreferenceManager
import androidx.room.Room
import androidx.work.WorkManager
import com.example.medihelper.localdatabase.AppDatabase
import com.example.medihelper.localdatabase.repositories.*
import com.example.medihelper.localdatabase.repositoriesimpl.MedicinePlanRepositoryImpl
import com.example.medihelper.localdatabase.repositoriesimpl.MedicineRepositoryImpl
import com.example.medihelper.localdatabase.repositoriesimpl.PersonRepositoryImpl
import com.example.medihelper.localdatabase.repositoriesimpl.PlannedMedicineRepositoryImpl
import com.example.medihelper.mainapp.medicines.AddEditMedicineViewModel
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanViewModel
import com.example.medihelper.dialogs.SelectMedicineViewModel
import com.example.medihelper.mainapp.persons.AddEditPersonViewModel
import com.example.medihelper.mainapp.persons.PersonViewModel
import com.example.medihelper.mainapp.more.loginregister.LoginRegisterViewModel
import com.example.medihelper.mainapp.medicineplan.MedicinePlanHistoryViewModel
import com.example.medihelper.mainapp.medicineplan.MedicinePlanListViewModel
import com.example.medihelper.mainapp.medicines.MedicineDetailsViewModel
import com.example.medihelper.mainapp.medicines.MedicinesViewModel
import com.example.medihelper.mainapp.more.loggeduser.LoggedUserViewModel
import com.example.medihelper.mainapp.more.MoreViewModel
import com.example.medihelper.mainapp.more.loggeduser.NewPasswordViewModel
import com.example.medihelper.mainapp.schedule.PlannedMedicineOptionsViewModel
import com.example.medihelper.mainapp.schedule.ScheduleViewModel
import com.example.medihelper.remotedatabase.api.*
import com.example.medihelper.services.*
import com.google.gson.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    localDatabaseModule,
                    remoteDatabaseModule,
                    viewModelModule,
                    serviceModule
                )
            )
        }
        runBlocking {
            val initialDataService: InitialDataService = get()
            initialDataService.checkInitialData()
        }
    }
}

val localDatabaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single<MedicineRepository> {
        MedicineRepositoryImpl(get<AppDatabase>().medicineDao(), get<AppDatabase>().deletedEntityDao())
    }
    single<MedicinePlanRepository> {
        MedicinePlanRepositoryImpl(get<AppDatabase>().medicinePlanDao(), get<AppDatabase>().deletedEntityDao())
    }
    single<PlannedMedicineRepository> {
        PlannedMedicineRepositoryImpl(get<AppDatabase>().plannedMedicineDao(), get<AppDatabase>().deletedEntityDao())
    }
    single<PersonRepository> {
        PersonRepositoryImpl(get<AppDatabase>().personDao(), get<AppDatabase>().deletedEntityDao())
    }
}

val remoteDatabaseModule = module {
    single { appRetrofit.create(AuthenticationApi::class.java) }
    single { appRetrofit.create(RegisteredUserApi::class.java) }
}

val viewModelModule = module {
    viewModel { MedicinesViewModel(androidContext().filesDir, get()) }
    viewModel { AddEditMedicineViewModel(androidContext().filesDir, get(), get(), get()) }
    viewModel { AddEditPersonViewModel(get(), get()) }
    viewModel { PersonViewModel(get(), get()) }
    viewModel { MedicinePlanHistoryViewModel(get(), get()) }
    viewModel { MedicinePlanListViewModel(get(), get()) }
    viewModel { MedicineDetailsViewModel(androidContext().filesDir, get(), get()) }
    viewModel { AddEditMedicinePlanViewModel(get(), get(), get(), get()) }
    viewModel { SelectMedicineViewModel(androidContext().filesDir, get()) }
    viewModel { PlannedMedicineOptionsViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { MoreViewModel(get()) }
    viewModel { LoginRegisterViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { LoggedUserViewModel(get(), get()) }
    viewModel { NewPasswordViewModel() }
}

val serviceModule = module {
    single { SharedPrefService(PreferenceManager.getDefaultSharedPreferences(androidContext())) }
    single { PersonProfileService(get()) }
    single { MedicineSchedulerService(get(), get()) }
    single { MedicineImageService(androidContext().filesDir, androidContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)) }
    single { WorkerService(WorkManager.getInstance(androidContext())) }
    single { InitialDataService(get(), get()) }
    single { NotificationService(androidContext()) }
}

private val appRetrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://medihelper-api.herokuapp.com/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeHierarchyAdapter(ByteArray::class.java, byteArrayToStringTypeAdapter)
                    .setLenient()
                    .create()
            )
        )
        .build()
}

private val byteArrayToStringTypeAdapter by lazy {
    object : JsonSerializer<ByteArray>, JsonDeserializer<ByteArray> {
        override fun serialize(src: ByteArray?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src?.let { String(src) })
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ByteArray {
            return json?.asString?.toByteArray() ?: byteArrayOf()
        }

    }
}