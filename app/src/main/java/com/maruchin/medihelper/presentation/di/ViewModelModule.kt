package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.alarm.AlarmViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarDayViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarViewModel
import com.maruchin.medihelper.presentation.feature.calendar.PlannedMedicineOptionsViewModel
import com.maruchin.medihelper.presentation.feature.medikit.AddEditMedicineViewModel
import com.maruchin.medihelper.presentation.feature.medikit.MedicineDetailsViewModel
import com.maruchin.medihelper.presentation.feature.medikit.MedicinesListViewModel
import com.maruchin.medihelper.presentation.feature.profiles.AddEditPersonViewModel
import com.maruchin.medihelper.presentation.feature.profiles.PersonOptionsViewModel
import com.maruchin.medihelper.presentation.feature.profiles.SelectProfileViewModel
import com.maruchin.medihelper.presentation.feature.plans.AddEditMedicinePlanViewModel
import com.maruchin.medihelper.presentation.feature.plans.MedicinePlanHistoryViewModel
import com.maruchin.medihelper.presentation.feature.plans.MedicinePlanListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MedicinesListViewModel(
            getAllMedicinesItemsUseCase = get()
        )
    }
    viewModel {
        MedicineDetailsViewModel(
            getMedicineDetailsUseCase = get(),
            deleteMedicineUseCase = get(),
            getMedicinePictureUseCase = get()
        )
    }
    viewModel {
        AddEditMedicineViewModel(
            getMedicineUnitsUseCase = get(),
            saveMedicineUseCase = get(),
            getMedicineEditDataUseCase = get(),
            captureMedicinePhotoUseCase = get()
        )
    }
    viewModel {
        SelectProfileViewModel(
            getAllProfilesItemsLiveUseCase = get(),
            selectedProfile = get()
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
            getProfileSimpleItemUseCase = get()
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
        AddEditMedicinePlanViewModel(
            personUseCases = get(),
            medicineUseCases = get(),
            medicinePlanUseCases = get(),
            dateTimeUseCases = get()
        )
    }
    viewModel {
        CalendarViewModel(
            personUseCases = get(),
            dateTimeUseCases = get(),
            plannedMedicineUseCases = get()
        )
    }
    viewModel {
        CalendarDayViewModel(
            personUseCases = get(),
            plannedMedicineUseCases = get()
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
}