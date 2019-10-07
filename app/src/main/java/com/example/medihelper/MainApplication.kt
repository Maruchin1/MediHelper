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
import com.example.medihelper.localdatabase.entities.PersonEntity
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
import com.example.medihelper.mainapp.more.patronconnect.ConnectedPersonViewModel
import com.example.medihelper.mainapp.more.patronconnect.PatronConnectViewModel
import com.example.medihelper.mainapp.persons.PersonOptionsViewModel
import com.example.medihelper.mainapp.schedule.PlannedMedicineOptionsViewModel
import com.example.medihelper.mainapp.schedule.ScheduleViewModel
import com.example.medihelper.remotedatabase.AuthenticationApi
import com.example.medihelper.remotedatabase.ConnectedPersonApi
import com.example.medihelper.remotedatabase.RegisteredUserApi
import com.example.medihelper.services.*
import com.google.gson.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class MainApplication : Application() {

    private val initialDataService: InitialDataService by inject()
    private val sharedPrefService: SharedPrefService by inject()
    private val connectedPersonDatabaseSequence by lazy {
        listOf(connectedPersonDatabaseModule, repositoryModule, viewModelModule, serviceModule)
    }
    private val mainDatabaseSequence by lazy {
        listOf(mainDatabaseModule, repositoryModule, viewModelModule, serviceModule)
    }

    fun switchToConnectedPersonDatabase() {
        unloadKoinModules(mainDatabaseSequence)
        loadKoinModules(connectedPersonDatabaseSequence)
    }

    fun switchToMainDatabase() {
        unloadKoinModules(connectedPersonDatabaseSequence)
        loadKoinModules(mainDatabaseSequence)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    appModule,
                    repositoryModule,
                    remoteDatabaseModule,
                    viewModelModule,
                    serviceModule
                )
            )
        }
        runBlocking {
            val databaseModule = when (sharedPrefService.getAppMode()) {
                SharedPrefService.AppMode.CONNECTED -> connectedPersonDatabaseModule
                else -> mainDatabaseModule
            }
            loadKoinModules(databaseModule)
            initialDataService.checkInitialData()
        }
    }
}

private const val EXTERNAL_PICTURES_DIR = "external-pictures-dir"

val appModule = module {
    single { androidApplication() as MainApplication }
    single { androidContext().filesDir }
    single(named(EXTERNAL_PICTURES_DIR)) { androidContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) }
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    single { WorkManager.getInstance(androidContext()) }
}

val mainDatabaseModule = module {
    single(override = true) {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.MAIN_DATABASE_NAME).build()
    }
}

val connectedPersonDatabaseModule = module {
    single(override = true) {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.CONNECTED_PERSON_DATABASE_NAME)
            .build()
    }
}

val repositoryModule = module {
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
    single { appRetrofit.create(ConnectedPersonApi::class.java) }
}

val viewModelModule = module {
    viewModel { MedicinesViewModel(get(), get(), get(), get()) }
    viewModel { AddEditMedicineViewModel(get(), get(), get(), get()) }
    viewModel { AddEditPersonViewModel(get(), get()) }
    viewModel { PersonViewModel(get(), get()) }
    viewModel { MedicinePlanHistoryViewModel(get(), get()) }
    viewModel { MedicinePlanListViewModel(get(), get(), get()) }
    viewModel { MedicineDetailsViewModel(get(), get(), get(), get()) }
    viewModel { AddEditMedicinePlanViewModel(get(), get(), get(), get()) }
    viewModel { SelectMedicineViewModel(get(), get()) }
    viewModel { PlannedMedicineOptionsViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get()) }
    viewModel { MoreViewModel(get(), get()) }
    viewModel { LoginRegisterViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { LoggedUserViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { NewPasswordViewModel() }
    viewModel { PersonOptionsViewModel(get(), get()) }
    viewModel { PatronConnectViewModel(get(), get(), get()) }
    viewModel { ConnectedPersonViewModel(get(), get(), get(), get(), get(), get()) }
}

val serviceModule = module {
    single { SharedPrefService(get()) }
    single { PersonProfileService(get()) }
    single { MedicineSchedulerService(get(), get()) }
    single { MedicineImageService(get(), get(named(EXTERNAL_PICTURES_DIR))) }
    single { WorkerService(get()) }
    single { InitialDataService(get(), get()) }
    single { NotificationService(androidContext()) }
    single { LoadingDialogService() }
    single { RepositoryDispatcherService(androidContext().filesDir, get(), get(), get(), get()) }
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