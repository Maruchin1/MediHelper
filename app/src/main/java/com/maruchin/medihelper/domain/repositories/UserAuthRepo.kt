package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.model.SignInErrors
import com.maruchin.medihelper.domain.model.SignUpErrors

interface UserAuthRepo {

    suspend fun signIn(email: String, password: String, errors: SignInErrors): String?
    suspend fun signUp(email: String, password: String, errors: SignUpErrors): String?
    suspend fun signOut()
    suspend fun changePassword(newPassword: String)
    suspend fun getCurrUser(): User?
}