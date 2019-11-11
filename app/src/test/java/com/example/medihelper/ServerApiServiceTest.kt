package com.example.medihelper

import androidx.work.WorkManager
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.dao.MedicineDao
import com.example.medihelper.localdata.dao.PersonDao
import com.example.medihelper.remotedata.api.AuthenticationApi
import com.example.medihelper.remotedata.api.RegisteredUserApi
import com.example.medihelper.remotedata.dto.LoginResponseDto
import com.example.medihelper.remotedata.dto.UserCredentialsDto
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.AppMode
import com.example.medihelper.service.ServerApiService
import com.example.medihelper.service.ServerApiServiceImpl
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ServerApiServiceTest : KoinTest {

    @Mock
    private lateinit var appSharedPref: AppSharedPref
    @Mock
    private lateinit var authenticationApi: AuthenticationApi
    @Mock
    private lateinit var registeredUserApi: RegisteredUserApi
    @Mock
    private lateinit var personDao: PersonDao
    @Mock
    private lateinit var medicineDao: MedicineDao
    @Mock
    private lateinit var workManager: WorkManager

    private val testModule = module {
        single { appSharedPref }
        single { authenticationApi }
        single { registeredUserApi }
        single { personDao }
        single { medicineDao }
        single { workManager }
        single<ServerApiService> { ServerApiServiceImpl(get(), get(), get(), get(), get(), get()) }
    }

    private val serverApiService: ServerApiService by inject()

    @Before
    fun before() {
        startKoin { modules(testModule) }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun getAppMode_NoTokenAndNoEmail_ReturnOffline() {
        Mockito.`when`(appSharedPref.getAuthToken()).thenReturn(null)
        Mockito.`when`(appSharedPref.getUserEmail()).thenReturn(null)

        val appMode = serverApiService.getAppMode()
        Truth.assertThat(appMode).isEqualTo(AppMode.OFFLINE)
    }

    @Test
    fun getAppMode_TokenAndEmail_ReturnLogged() {
        Mockito.`when`(appSharedPref.getAuthToken()).thenReturn("token")
        Mockito.`when`(appSharedPref.getUserEmail()).thenReturn("test@mail.com")

        val appMode = serverApiService.getAppMode()
        Truth.assertThat(appMode).isEqualTo(AppMode.LOGGED)
    }

    @Test
    fun loginUser_Success_NoRemoteData() {
        runBlocking {
            val email = "test@email.com"
            val password = "abc"

            val mockedOutput = LoginResponseDto(authToken = "auth-token", isDataAvailable = false)
            Mockito.`when`(authenticationApi.loginUser(UserCredentialsDto(email, password)))
                .thenReturn(mockedOutput)

            val returnedPair = serverApiService.loginUser(email, password)

            val isDataAvailable = returnedPair.first
            val apiResponse = returnedPair.second

            Truth.assertThat(apiResponse).isEqualTo(ApiResponse.OK)
            Truth.assertThat(isDataAvailable).isFalse()
        }
    }
}