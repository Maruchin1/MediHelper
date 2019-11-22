package com.example.medihelper.data.di

import androidx.room.Room
import com.example.medihelper.data.local.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomMainModule = module(override = true) {
    single {
        Room.databaseBuilder(androidContext(), RoomDatabase::class.java, RoomDatabase.MAIN_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}