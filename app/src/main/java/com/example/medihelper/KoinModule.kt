package com.example.medihelper

import android.os.Environment
import android.preference.PreferenceManager
import androidx.room.Room
import androidx.work.WorkManager
import com.example.medihelper.localdatabase.AppDatabase
import com.example.medihelper.localdatabase.DeletedHistory
import com.example.medihelper.localdatabase.DeletedHistoryImpl
import com.example.medihelper.localdatabase.MedicineScheduler
import com.example.medihelper.mainapp.alarm.AlarmViewModel
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanViewModel
import com.example.medihelper.mainapp.medicineplan.MedicinePlanHistoryViewModel
import com.example.medihelper.mainapp.medicineplan.MedicinePlanListViewModel
import com.example.medihelper.mainapp.medicine.AddEditMedicineViewModel
import com.example.medihelper.mainapp.medicine.MedicineDetailsViewModel
import com.example.medihelper.mainapp.medicine.MedicinesViewModel
import com.example.medihelper.mainapp.more.MoreViewModel
import com.example.medihelper.mainapp.more.loggeduser.LoggedUserViewModel
import com.example.medihelper.mainapp.more.loggeduser.NewPasswordViewModel
import com.example.medihelper.mainapp.more.loginregister.LoginRegisterViewModel
import com.example.medihelper.mainapp.more.patronconnect.ConnectedPersonViewModel
import com.example.medihelper.mainapp.more.patronconnect.PatronConnectViewModel
import com.example.medihelper.mainapp.person.AddEditPersonViewModel
import com.example.medihelper.mainapp.person.PersonOptionsViewModel
import com.example.medihelper.mainapp.person.PersonViewModel
import com.example.medihelper.mainapp.schedule.PlannedMedicineOptionsViewModel
import com.example.medihelper.mainapp.schedule.ScheduleDayViewModel
import com.example.medihelper.mainapp.schedule.ScheduleViewModel
import com.example.medihelper.remotedatabase.api.AuthenticationApi
import com.example.medihelper.remotedatabase.api.ConnectedPersonApi
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.serversync.EntityDtoMapper
import com.example.medihelper.serversync.LocalDatabaseDispatcher
import com.example.medihelper.service.*
import com.google.gson.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

private const val NAME_EXTERNAL_PICTURES_DIR = "name-external-pictures-dir"

val appModule = module {
    single { androidContext() as MainApplication }
    single { androidContext().filesDir }
    single(named(NAME_EXTERNAL_PICTURES_DIR)) { androidContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) }
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    single { WorkManager.getInstance(androidContext()) }
}

val mainDatabaseModule = module(override = true) {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.MAIN_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().medicineDao() }
    single { get<AppDatabase>().personDao() }
    single { get<AppDatabase>().medicinePlanDao() }
    single { get<AppDatabase>().timeDoseDao() }
    single { get<AppDatabase>().plannedMedicineDao() }
    single<DeletedHistory> { DeletedHistoryImpl(get()) }
}

val connectedPersonDatabaseModule = module(override = true) {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.CONNECTED_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().medicineDao() }
    single { get<AppDatabase>().personDao() }
    single { get<AppDatabase>().medicinePlanDao() }
    single { get<AppDatabase>().timeDoseDao() }
    single { get<AppDatabase>().plannedMedicineDao() }
    single<DeletedHistory> { DeletedHistoryImpl(get()) }
}

val testDatabaseModule = module(override = true) {
    single {
        Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
    single { get<AppDatabase>().medicineDao() }
    single { get<AppDatabase>().personDao() }
    single { get<AppDatabase>().medicinePlanDao() }
    single { get<AppDatabase>().timeDoseDao() }
    single { get<AppDatabase>().plannedMedicineDao() }
    single<DeletedHistory> { DeletedHistoryImpl(get()) }
}

val serverApiModule = module {
    single { appRetrofit.create(AuthenticationApi::class.java) }
    single { appRetrofit.create(RegisteredUserApi::class.java) }
    single { appRetrofit.create(ConnectedPersonApi::class.java) }
}

val viewModelModule = module {
    viewModel { MedicinesViewModel(get(), get(), get(), get()) }
    viewModel { AddEditMedicineViewModel(get(), get()) }
    viewModel { AddEditPersonViewModel(get()) }
    viewModel { PersonViewModel(get()) }
    viewModel { MedicinePlanHistoryViewModel(get(), get(), get()) }
    viewModel { MedicinePlanListViewModel(get(), get(), get(), get()) }
    viewModel { MedicineDetailsViewModel(get(), get(), get()) }
    viewModel { AddEditMedicinePlanViewModel(get(), get(), get(), get()) }
    viewModel { PlannedMedicineOptionsViewModel(get(), get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get()) }
    viewModel { MoreViewModel(get(), get()) }
    viewModel { LoginRegisterViewModel(get()) }
    viewModel { LoggedUserViewModel(get()) }
    viewModel { NewPasswordViewModel() }
    viewModel { PersonOptionsViewModel(get(), get()) }
    viewModel { PatronConnectViewModel(get()) }
    viewModel { ConnectedPersonViewModel(get(), get()) }
    viewModel { ScheduleDayViewModel(get(), get()) }
    viewModel { AlarmViewModel(get()) }
}

val serviceModule = module {
    single<PersonService> { PersonServiceImpl(get(), get(), get()) }
    single<MedicineService> { MedicineServiceImpl(get(), get(), get(named(NAME_EXTERNAL_PICTURES_DIR)), get(), get()) }
    single<MedicinePlanService> { MedicinePlanServiceImpl(get(), get(), get(), get()) }
    single<PlannedMedicineService> { PlannedMedicineServiceImpl(get(), get(), get(), get(), get()) }
    single<AlarmService> { AlarmServiceImpl(get(), get()) }
    single<DateTimeService> { DateTimeServiceImpl() }
    single<InitialDataService> { InitialDataServiceImpl(get(), get()) }
    single<LoadingScreenService> { LoadingScreenServiceImpl() }
    single<NotificationService> { NotificationServiceImpl(get()) }
    single<ServerApiService> { ServerApiServiceImpl(get(), get(), get(), get(), get(), get(), get()) }
}

val utilsModule = module {
    single { EntityDtoMapper(get(), get(), get()) }
    single { MedicineScheduler() }
    single { LocalDatabaseDispatcher(get(), get(), get(), get(), get()) }
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