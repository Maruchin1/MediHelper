package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.user.IsUserSignedInUseCase
import com.maruchin.medihelper.domain.usecases.user.SignInUseCase
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase
import com.maruchin.medihelper.domain.usecases.user.SignUpUseCase
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
            profileRepo = get(),
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
}