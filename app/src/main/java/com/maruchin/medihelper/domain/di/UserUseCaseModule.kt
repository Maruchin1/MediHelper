package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.user.*
import com.maruchin.medihelper.domain.usecasesimpl.users.*
import org.koin.dsl.module

val userUseCaseModule = module {
    factory {
        SignInUseCaseImpl(
            userRepo = get(),
            validator = get()
        ) as SignInUseCase
    }
    factory {
        SignUpUseCaseImpl(
            userRepo = get(),
            validator = get()
        ) as SignUpUseCase
    }
    factory {
        IsUserSignedInUseCaseImpl(
            userRepo = get()
        ) as IsUserSignedInUseCase
    }
    factory { 
        SignOutUseCaseImpl(
            userRepo = get()
        ) as SignOutUseCase
    }
    factory {
        ChangePasswordUseCaseImpl(
            userRepo = get(),
            validator = get()
        ) as ChangePasswordUseCase
    }
    factory {
        GetCurrUserEmailUseCaseImpl(
            userRepo = get()
        ) as GetCurrUserEmailUseCase
    }
    factory {
        InitDefaultsUseCaseImpl(
            profileRepo = get(),
            medicineUnitRepo = get(),
            medicineTypeRepo = get()
        ) as InitDefaultsUseCase
    }
}