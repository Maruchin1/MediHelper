package com.maruchin.medihelper.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.FirestoreRepo
import com.maruchin.medihelper.data.mappers.UserMapper
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserRepo

class UserRepoImpl(
    private val db: FirebaseFirestore,
    private val mapper: UserMapper
) : FirestoreRepo<User>(
    collectionRef = db.collection("users"),
    mapper = mapper
), UserRepo