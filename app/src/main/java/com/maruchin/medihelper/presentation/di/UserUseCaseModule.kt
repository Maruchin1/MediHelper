package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.user.*
import org.koin.dsl.module

val userUseCaseModule = module {
    factory {
        SignInUseCase(
            userAuthRepo = get(),
            validator = get()
        )
    }
    factory {
        SignUpUseCase(
            userAuthRepo = get(),
            validator = get()
        )
    }
    factory {
        IsUserSignedInUseCase(
            userAuthRepo = get()
        )
    }
    factory { 
        SignOutUseCase(
            userAuthRepo = get()
        )
    }
    factory {
        ChangePasswordUseCase(
            userAuthRepo = get(),
            validator = get()
        )
    }
    factory {
        GetCurrUserUseCase(
            userAuthRepo = get()
        )
    }
}