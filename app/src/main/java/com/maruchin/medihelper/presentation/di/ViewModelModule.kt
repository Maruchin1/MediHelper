package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.alarm.AlarmViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarDayViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarViewModel
import com.maruchin.medihelper.presentation.feature.calendar.PlannedMedicineOptionsViewModel
import com.maruchin.medihelper.presentation.feature.profiles.ProfileViewModel
import com.maruchin.medihelper.presentation.feature.medikit.*
import com.maruchin.medihelper.presentation.feature.mediplan.AddEditMedicinePlanViewModel
import com.maruchin.medihelper.presentation.feature.mediplan.MedicinePlanDetailsViewModel
import com.maruchin.medihelper.presentation.feature.profiles.AddEditProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MedicinesListViewModel(
            getAllMedicinesItemsUseCase = get(),
            picturesRef = get()
        )
    }
    viewModel {
        MedicineDetailsViewModel(
            getMedicineDetailsUseCase = get(),
            deleteMedicineUseCase = get(),
            calcDaysRemainUseCase = get(),
            picturesRef = get()
        )
    }
    viewModel {
        AddEditMedicineViewModel(
            getMedicineUnitsUseCase = get(),
            saveMedicineUseCase = get(),
            getMedicineEditDataUseCase = get(),
            deviceCamera = get(),
            picturesRef = get()
        )
    }
    viewModel {
        AddEditProfileViewModel(
            getProfileColorsUseCase = get(),
            getProfileEditDataUseCase = get(),
            saveProfileUseCase = get()
        )
    }
    viewModel {
        CalendarViewModel(
            getCurrDateUseCase = get(),
            getProfileItemUseCase = get(),
            updateAllPlannedMedicinesStatusUseCase = get(),
            selectedProfile = get()
        )
    }
    viewModel {
        CalendarDayViewModel(
            getLivePlannedMedicinesItemsByDateUseCase = get(),
            selectedProfile = get()
        )
    }
    viewModel {
        PlannedMedicineOptionsViewModel(
            getPlannedMedicineDetailsUseCase = get(),
            changePlannedMedicineTakenUseCase = get(),
            changePlannedMedicineTimeUseCase = get(),
            selectedProfile = get()
        )
    }
    viewModel {
        AlarmViewModel(
            plannedMedicineUseCases = get()
        )
    }
    viewModel {
        MedicineInfoViewModel(
            searchForMedicineInfoUseCase = get(),
            getMedicineInfoUseCase = get()
        )
    }
    viewModel {
        ProfileViewModel(
            selectedProfile = get(),
            getProfileItemUseCase = get(),
            getLiveAllProfilesItemsUseCase = get(),
            getLiveMedicinesPlansItemsByProfileUseCase = get(),
            deleteProfileUseCase = get()
        )
    }
    viewModel {
        AddEditMedicinePlanViewModel(
            getMedicinePlanEditDataUseCase = get(),
            getProfileItemUseCase = get(),
            getMedicineItemUseCase = get(),
            getCurrDateUseCase = get(),
            saveMedicinePlanUseCase = get()
        )
    }
    viewModel {
        MedicinePlanDetailsViewModel(
            getMedicinePlanDetailsUseCase = get(),
            deleteMedicinePlanUseCase = get()
        )
    }
}