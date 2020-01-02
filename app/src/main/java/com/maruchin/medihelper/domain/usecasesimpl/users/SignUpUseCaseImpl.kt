package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.data.utils.ProfileColor
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.SignUpErrors
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.SignUpUseCase
import com.maruchin.medihelper.domain.utils.SignUpValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class SignUpUseCaseImpl(
    private val userAuthRepo: UserAuthRepo,
    private val validator: SignUpValidator
) : SignUpUseCase, KoinComponent {

    //todo pomyśleć co zrobić z tym profileRepo bo taka wersja jest nietestowalna
    private val profileRepo: ProfileRepo by inject()

    override suspend fun execute(params: SignUpUseCase.Params): SignUpErrors = withContext(Dispatchers.Default) {
        val validatorParams = getValidatorParams(params)
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            signUpUser(params, errors)
        }

        return@withContext errors
    }

    private fun getValidatorParams(params: SignUpUseCase.Params): SignUpValidator.Params {
        return SignUpValidator.Params(
            email = params.email,
            password = params.password,
            passwordConfirm = params.passwordConfirm,
            userName = params.userName
        )
    }

    private suspend fun signUpUser(params: SignUpUseCase.Params, errors: SignUpErrors) {
        val userId = userAuthRepo.signUp(params.email!!, params.password!!, errors)
        if (userId == null) {
            throw SignUpUseCase.SignUpFailedException()
        }
        addUserMainProfile(params.userName!!)
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
}