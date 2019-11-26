package com.maruchin.medihelper.data.di

import androidx.room.Room
import com.maruchin.medihelper.data.local.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomMainModule = module(override = true) {
    single {
        Room.databaseBuilder(androidContext(), RoomDatabase::class.java, RoomDatabase.MAIN_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}