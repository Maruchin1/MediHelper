package com.maruchin.medihelper.data.di

import androidx.room.Room
import com.maruchin.medihelper.data.local.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomConnectedModule = module(override = true) {
    single {
        Room.databaseBuilder(androidContext(), RoomDatabase::class.java, RoomDatabase.CONNECTED_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}