package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.alarm.AlarmViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarDayViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarViewModel
import com.maruchin.medihelper.presentation.feature.calendar.PlannedMedicineOptionsViewModel
import com.maruchin.medihelper.presentation.feature.calendar.ProfileViewModel
import com.maruchin.medihelper.presentation.feature.medikit.*
import com.maruchin.medihelper.presentation.feature.mediplan.AddEditMedicinePlanViewModel
import com.maruchin.medihelper.presentation.feature.profiles.AddEditPersonViewModel
import com.maruchin.medihelper.presentation.feature.profiles.PersonOptionsViewModel
import com.maruchin.medihelper.presentation.feature.plans.MedicinePlanHistoryViewModel
import com.maruchin.medihelper.presentation.feature.plans.MedicinePlanListViewModel
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
            deviceCamera = get()
        )
    }
    viewModel {
        PersonOptionsViewModel(
            personUseCases = get()
        )
    }
    viewModel {
        AddEditPersonViewModel(
            getProfileColorsUseCase = get(),
            saveProfileUseCase = get()
        )
    }
    viewModel {
        MedicinePlanListViewModel(
            selectedProfile = get(),
            getProfileItemUseCase = get()
        )
    }
    viewModel {
        MedicinePlanHistoryViewModel(
            personUseCases = get(),
            medicinePlanUseCases = get(),
            dateTimeUseCases = get()
        )
    }
    viewModel {
        CalendarViewModel(
            getCurrDateUseCase = get(),
            getProfileItemUseCase = get(),
            selectedProfile = get()
        )
    }
    viewModel {
        CalendarDayViewModel(
        )
    }
    viewModel {
        PlannedMedicineOptionsViewModel(
            personUseCases = get(),
            plannedMedicineUseCases = get(),
            medicineUseCases = get()
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
            getLiveAllProfilesItemsUseCase = get(),
            selectedProfile = get(),
            getLiveMedicinesPlansItemsByProfileUseCase = get()
        )
    }
    viewModel {
        AddEditMedicinePlanViewModel(
            getProfileItemUseCase = get(),
            getMedicineItemUseCase = get(),
            getCurrDateUseCase = get(),
            saveMedicinePlanUseCase = get()
        )
    }
}