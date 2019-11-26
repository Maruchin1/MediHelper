package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.remote.ApiResponseMapper
import com.maruchin.medihelper.data.remote.api.AuthenticationApi
import com.maruchin.medihelper.data.remote.api.ConnectedPersonApi
import com.maruchin.medihelper.data.remote.api.RegisteredUserApi
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDataModule = module {
    single {
        get<Retrofit>().create(AuthenticationApi::class.java)
    }
    single {
        get<Retrofit>().create(RegisteredUserApi::class.java)
    }
    single {
        get<Retrofit>().create(ConnectedPersonApi::class.java)
    }
    single {
        ApiResponseMapper()
    }
}