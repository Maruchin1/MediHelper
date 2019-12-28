package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.AuthResult
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.framework.BaseRepo

interface UserAuthRepo {

    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signUp(email: String, password: String): AuthResult
    suspend fun signOut()
    suspend fun changePassword(newPassword: String)
    suspend fun getCurrUser(): User?
}