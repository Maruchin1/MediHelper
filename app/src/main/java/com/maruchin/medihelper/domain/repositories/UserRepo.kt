package com.maruchin.medihelper.domain.repositories

import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.model.SignUpErrors

interface UserRepo {

    @Throws(
        IncorrectEmailException::class,
        IncorrectPasswordException::class,
        UndefinedAuthException::class
    )
    suspend fun signIn(email: String, password: String): String
    @Throws(
        IncorrectEmailException::class,
        UserAlreadyExistException::class,
        WeakPasswordException::class,
        UndefinedAuthException::class
    )
    suspend fun signUp(email: String, password: String): String
    suspend fun signOut()
    suspend fun changePassword(newPassword: String)
    suspend fun getCurrUser(): User?

    class IncorrectEmailException : Exception()
    class IncorrectPasswordException : Exception()
    class UndefinedAuthException : Exception()
    class UserAlreadyExistException : Exception()
    class WeakPasswordException : Exception()
}