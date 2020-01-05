package com.maruchin.medihelper.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.maruchin.medihelper.data.utils.AppFirebase
import org.koin.dsl.module

val firebaseModule = module {
    single {
        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = settings
        }
    }
    single {
        FirebaseAuth.getInstance()
    }
    single {
        FirebaseStorage.getInstance()
    }
    single {
        AppFirebase(
            auth = get(),
            db = get(),
            storage = get()
        )
    }
}