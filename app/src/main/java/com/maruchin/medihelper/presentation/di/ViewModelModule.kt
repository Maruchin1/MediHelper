package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.add_edit_medicine.AddEditMedicineViewModel
import com.maruchin.medihelper.presentation.feature.alarm.AlarmViewModel
import com.maruchin.medihelper.presentation.feature.authentication.LoginViewModel
import com.maruchin.medihelper.presentation.feature.authentication.RegisterViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarDayViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarViewModel
import com.maruchin.medihelper.presentation.feature.medicines_list.MedicinesListViewModel
import com.maruchin.medihelper.presentation.feature.planned_medicine_options.PlannedMedicineOptionsViewModel
import com.maruchin.medihelper.presentation.feature.profiles_menu.ProfileViewModel
import com.maruchin.medihelper.presentation.feature.medicine_details.*
import com.maruchin.medihelper.presentation.feature.add_edit_medicine_plan.AddEditMedicinePlanViewModel
import com.maruchin.medihelper.presentation.feature.medicine_plan_details.MedicinePlanDetailsViewModel
import com.maruchin.medihelper.presentation.feature.options.ChangePasswordViewModel
import com.maruchin.medihelper.presentation.feature.options.OptionsViewModel
import com.maruchin.medihelper.presentation.feature.add_edit_profile.AddEditProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MedicinesListViewModel(
            getAllMedicinesItemsUseCase = get(),
            picturesRef = get(),
            medicinesSorter = get(),
            medicinesFilter = get()
        )
    }
    viewModel {
        MedicineDetailsViewModel(
            getMedicineDetailsUseCase = get(),
            deleteMedicineUseCase = get(),
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
            getProfileSimpleItemUseCase = get(),
            selectedProfile = get(),
            deviceCalendar = get()
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
            setPlannedMedicineTakenUseCase = get(),
            changePlannedMedicineTimeUseCase = get(),
            getPlannedMedicineNotifDataUseCase = get()
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
            getLiveAllProfilesItemsUseCase = get(),
            getLiveMedicinesPlansItemsByProfileUseCase = get(),
            deleteProfileUseCase = get()
        )
    }
    viewModel {
        AddEditMedicinePlanViewModel(
            getMedicinePlanEditDataUseCase = get(),
            getProfileSimpleItemUseCase = get(),
            getMedicineSimpleItemUseCase = get(),
            saveMedicinePlanUseCase = get(),
            deviceCalendar = get()
        )
    }
    viewModel {
        MedicinePlanDetailsViewModel(
            getMedicinePlanDetailsUseCase = get(),
            getMedicinePlanHistoryUseCase = get(),
            deleteSingleMedicinePlanUseCase = get()
        )
    }
    viewModel {
        LoginViewModel(
            signInUseCase = get()
        )
    }
    viewModel {
        OptionsViewModel(
            getCurrUserUseCase = get(),
            getLivePlannedMedicineReminderModeUseCase = get(),
            signOutUseCase = get(),
            changePlannedMedicineReminderModeUseCase = get()
        )
    }
    viewModel {
        RegisterViewModel(
            signUpUseCase = get()
        )
    }
    viewModel {
        ChangePasswordViewModel(
            changePasswordUseCase = get()
        )
    }
}