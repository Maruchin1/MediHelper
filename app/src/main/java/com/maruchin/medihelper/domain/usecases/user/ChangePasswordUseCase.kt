package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.model.ChangePasswordErrors

interface ChangePasswordUseCase {

    suspend fun execute(params: Params): ChangePasswordErrors

    data class Params(
        val newPassword: String?,
        val newPasswordConfirm: String?
    )
}