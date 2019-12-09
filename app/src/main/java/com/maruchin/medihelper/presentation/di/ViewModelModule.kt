package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.alarm.AlarmViewModel
import com.maruchin.medihelper.presentation.feature.auth.LoginViewModel
import com.maruchin.medihelper.presentation.feature.auth.PatronConnectViewModel
import com.maruchin.medihelper.presentation.feature.auth.RegisterViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarDayViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarViewModel
import com.maruchin.medihelper.presentation.feature.calendar.PlannedMedicineOptionsViewModel
import com.maruchin.medihelper.presentation.feature.launcher.MainPersonViewModel
import com.maruchin.medihelper.presentation.feature.medikit.AddEditMedicineViewModel
import com.maruchin.medihelper.presentation.feature.medikit.MedicineDetailsViewModel
import com.maruchin.medihelper.presentation.feature.medikit.MedicinesListViewModel
import com.maruchin.medihelper.presentation.feature.options.NewPasswordViewModel
import com.maruchin.medihelper.presentation.feature.options.OptionsViewModel
import com.maruchin.medihelper.presentation.feature.personsprofiles.AddEditPersonViewModel
import com.maruchin.medihelper.presentation.feature.personsprofiles.PersonOptionsViewModel
import com.maruchin.medihelper.presentation.feature.personsprofiles.PersonViewModel
import com.maruchin.medihelper.presentation.feature.plans.AddEditMedicinePlanViewModel
import com.maruchin.medihelper.presentation.feature.plans.MedicinePlanHistoryViewModel
import com.maruchin.medihelper.presentation.feature.plans.MedicinePlanListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MedicinesListViewModel(
            serverConnectionUseCases = get(),
            medicineUseCases = get(),
            personUseCases = get()
        )
    }
    viewModel {
        MedicineDetailsViewModel(
            personUseCases = get(),
            serverConnectionUseCases = get(),
            medicineUseCases = get()
        )
    }
    viewModel {
        AddEditMedicineViewModel(
            medicineUseCases = get()
        )
    }
    viewModel {
        PersonViewModel(
            personUseCases = get()
        )
    }
    viewModel {
        PersonOptionsViewModel(
            personUseCases = get(),
            serverConnectionUseCases = get()
        )
    }
    viewModel {
        AddEditPersonViewModel(
            personUseCases = get(),
            serverConnectionUseCases = get()
        )
    }
    viewModel {
        MedicinePlanListViewModel(
            personUseCases = get(),
            serverConnectionUseCases = get(),
            medicinePlanUseCases = get(),
            dateTimeUseCases = get()
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
            serverConnectionUseCases = get(),
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
        PatronConnectViewModel(
            serverConnectionUseCases = get()
        )
    }
    viewModel {
        AlarmViewModel(
            plannedMedicineUseCases = get()
        )
    }
    viewModel {
        MainPersonViewModel(
            personUseCases = get()
        )
    }
    viewModel {
        LoginViewModel(
            serverConnectionUseCases = get(),
            personUseCases = get()
        )
    }
    viewModel {
        RegisterViewModel(
            serverConnectionUseCases = get()
        )
    }
    viewModel {
        OptionsViewModel(
            personUseCases = get(),
            serverConnectionUseCases = get()
        )
    }
    viewModel {
        NewPasswordViewModel()
    }
}