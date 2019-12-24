package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.framework.BaseRepo

interface UserRepo : BaseRepo<User> {

    suspend fun signIn(email: String, password: String): String?
    suspend fun signUp(email: String, password: String): String?
    suspend fun signOut()
    suspend fun getCurrUserId(): String?
}