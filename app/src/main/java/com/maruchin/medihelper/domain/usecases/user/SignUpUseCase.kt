package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.data.ProfileColor
import com.maruchin.medihelper.domain.entities.AuthResult
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.utils.SignUpValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpUseCase(
    private val userRepo: UserRepo,
    private val profileRepo: ProfileRepo,
    private val validator: SignUpValidator
) {
    suspend fun execute(params: Params): SignUpValidator.Errors = withContext(Dispatchers.Default) {
        val validatorParams = SignUpValidator.Params(
            email = params.email,
            password = params.password,
            passwordConfirm = params.passwordConfirm,
            userName = params.userName
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            val authResult = userRepo.signUp(params.email!!, params.password!!)

            if (authResult is AuthResult.Success) {
                addUserEntity(authResult.userId, params.userName!!, params.email)
                addUserMainProfile(params.userName)
            } else if (authResult is AuthResult.Error) {
                errors.globalMessage = authResult.message
            }
        }

        return@withContext errors
    }

    private suspend fun addUserEntity(userId: String, userName: String, email: String) {
        val newUser = User(
            entityId = userId,
            userName = userName,
            email = email
        )
        userRepo.addNew(newUser)
    }

    private suspend fun addUserMainProfile(userName: String) {
        val mainProfile = Profile(
            entityId = "",
            name = userName,
            color = ProfileColor.MAIN.colorString,
            mainPerson = true
        )
        profileRepo.addNew(mainProfile)
    }

    data class Params(
        val email: String?,
        val password: String?,
        val passwordConfirm: String?,
        val userName: String?
    )
}