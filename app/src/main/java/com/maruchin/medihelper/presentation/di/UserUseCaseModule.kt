package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.user.*
import org.koin.dsl.module

val userUseCaseModule = module {
    factory {
        SignInUseCase(
            userRepo = get(),
            validator = get()
        )
    }
    factory {
        SignUpUseCase(
            userRepo = get(),
            validator = get()
        )
    }
    factory {
        IsUserSignedInUseCase(
            userRepo = get()
        )
    }
    factory { 
        SignOutUseCase(
            userRepo = get()
        )
    }
    factory {
        ChangePasswordUseCase(
            userRepo = get(),
            validator = get()
        )
    }
    factory {
        GetCurrUserUseCase(
            userRepo = get()
        )
    }
}