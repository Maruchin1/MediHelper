package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.FirestoreRepo
import com.maruchin.medihelper.data.mappers.UserMapper
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserRepo
import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val mapper: UserMapper
) : FirestoreRepo<User>(
    collectionRef = db.collection("users"),
    mapper = mapper
), UserRepo {

    override suspend fun signIn(email: String, password: String): String? {
        auth.signInWithEmailAndPassword(email, password).await()
        return auth.currentUser?.uid
    }

    override suspend fun signUp(email: String, password: String): String? {
        auth.createUserWithEmailAndPassword(email, password).await()
        return auth.currentUser?.uid
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}