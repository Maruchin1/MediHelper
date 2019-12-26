package com.maruchin.medihelper.domain.entities

sealed class AuthResult {

    class Success(val userId: String) : AuthResult()

    class Error(val message: String) : AuthResult()
}