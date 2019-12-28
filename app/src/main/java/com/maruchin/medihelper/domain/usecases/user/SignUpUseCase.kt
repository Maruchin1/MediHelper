package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.data.utils.ProfileColor
import com.maruchin.medihelper.domain.entities.AuthResult
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.utils.SignUpValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class SignUpUseCase(
    private val userAuthRepo: UserAuthRepo,
    private val validator: SignUpValidator
) : KoinComponent {

    private val profileRepo: ProfileRepo by inject()

    suspend fun execute(params: Params): SignUpValidator.Errors = withContext(Dispatchers.Default) {
        val validatorParams = SignUpValidator.Params(
            email = params.email,
            password = params.password,
            passwordConfirm = params.passwordConfirm,
            userName = params.userName
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            val authResult = userAuthRepo.signUp(params.email!!, params.password!!)

            if (authResult is AuthResult.Success) {
                addUserMainProfile(params.userName!!)
            } else if (authResult is AuthResult.Error) {
                errors.globalMessage = authResult.message
            }
        }

        return@withContext errors
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