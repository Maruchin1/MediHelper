package com.example.medihelper

import androidx.room.Room
import com.example.medihelper.localdatabase.AppDatabase
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val testRoomModule = module(override = true) {
    single {
        Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}

val testRetrofitModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
    }
}