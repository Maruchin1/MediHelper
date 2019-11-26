package com.maruchin.medihelper.data.di

import androidx.room.Room
import com.maruchin.medihelper.data.local.RoomDatabase
import org.koin.dsl.module

val roomTestModule = module(override = true) {
    single {
        Room.inMemoryDatabaseBuilder(get(), RoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}