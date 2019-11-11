package com.example.medihelper

import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.WorkManager
import com.example.medihelper.localdatabase.AppDatabase
import com.example.medihelper.localdatabase.AppSharedPref
import com.example.medihelper.localdatabase.AppSharedPrefImpl
import com.example.medihelper.remotedatabase.api.AuthenticationApi
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.LoginResponseDto
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.AppMode
import com.example.medihelper.service.ServerApiService
import com.example.medihelper.service.ServerApiServiceImpl
import com.google.common.truth.Truth
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.inject
import retrofit2.Retrofit

class ServerApiServiceAndroidTest : AppAndroidTest() {

    private val testModule = module {
        single<AppSharedPref> {
            AppSharedPrefImpl(androidContext())
        }
        single<AuthenticationApi> { get<Retrofit>().create(AuthenticationApi::class.java) }
        single<RegisteredUserApi> { get<Retrofit>().create(RegisteredUserApi::class.java) }
        single { get<AppDatabase>().personDao() }
        single { get<AppDatabase>().medicineDao() }
        single { WorkManager.getInstance(androidContext()) }
        single<ServerApiService> { ServerApiServiceImpl(get(), get(), get(), get(), get(), get()) }
    }

    private val mockWebServer = MockWebServer()
    private val serverApiService: ServerApiService by inject()

    @Before
    fun before() {
        mockWebServer.start(8080)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        startKoin {
            androidContext(context)
            modules(listOf(testRoomModule, testRetrofitModule, testModule))
        }
    }

    @After
    fun after() {
        stopKoin()
        mockWebServer.shutdown()
    }

    @Test
    fun getAppMode_NoTokenAndNoEmail_ReturnOffline() {
        val appMode = serverApiService.getAppMode()
        Truth.assertThat(appMode).isEqualTo(AppMode.OFFLINE)
    }

    @Test
    fun loginUser_Success_AppModeLogged() {
        val responseBody = LoginResponseDto(authToken = "token", isDataAvailable = false)
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(responseBody))
        mockWebServer.enqueue(response)

        val returnedPair = runBlocking {
            serverApiService.loginUser(email = "test@email.com", password = "abc")
        }
        val isDataAvailable = returnedPair.first
        val apiResponse = returnedPair.second

        Truth.assertThat(apiResponse).isEqualTo(ApiResponse.OK)
        Truth.assertThat(isDataAvailable).isFalse()

        val appMode = serverApiService.getAppMode()
        Truth.assertThat(appMode).isEqualTo(AppMode.LOGGED)
    }
}