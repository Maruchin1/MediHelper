package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.user.*
import com.maruchin.medihelper.domain.usecasesimpl.users.*
import org.koin.dsl.module

val userUseCaseModule = module {
    factory {
        SignInUseCaseImpl(
            userAuthRepo = get(),
            validator = get()
        ) as SignInUseCase
    }
    factory {
        SignUpUseCaseImpl(
            userAuthRepo = get(),
            validator = get()
        ) as SignUpUseCase
    }
    factory {
        IsUserSignedInUseCaseImpl(
            userAuthRepo = get()
        ) as IsUserSignedInUseCase
    }
    factory { 
        SignOutUseCaseImpl(
            userAuthRepo = get()
        ) as SignOutUseCase
    }
    factory {
        ChangePasswordUseCaseImpl(
            userAuthRepo = get(),
            validator = get()
        ) as ChangePasswordUseCase
    }
    factory {
        GetCurrUserEmailUseCaseImpl(
            userAuthRepo = get()
        ) as GetCurrUserEmailUseCase
    }
}