package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.add_edit_medicine.AddEditMedicineViewModel
import com.maruchin.medihelper.device.reminder.alarm.AlarmViewModel
import com.maruchin.medihelper.presentation.feature.authentication.LoginViewModel
import com.maruchin.medihelper.presentation.feature.authentication.RegisterViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarDayViewModel
import com.maruchin.medihelper.presentation.feature.calendar.CalendarViewModel
import com.maruchin.medihelper.presentation.feature.medicines_list.MedicinesListViewModel
import com.maruchin.medihelper.presentation.feature.planned_medicine_options.PlannedMedicineOptionsViewModel
import com.maruchin.medihelper.presentation.feature.profiles_menu.ProfileViewModel
import com.maruchin.medihelper.presentation.feature.medicine_details.*
import com.maruchin.medihelper.presentation.feature.add_edit_plan.AddEditPlanViewModel
import com.maruchin.medihelper.presentation.feature.plan_details.PlanDetailsViewModel
import com.maruchin.medihelper.presentation.feature.options.account.ChangePasswordViewModel
import com.maruchin.medihelper.presentation.feature.options.OptionsViewModel
import com.maruchin.medihelper.presentation.feature.add_edit_profile.AddEditProfileViewModel
import com.maruchin.medihelper.presentation.feature.options.reminders.ReminderModeViewModel
import com.maruchin.medihelper.presentation.feature.options.saved_types.MedicineTypesViewModel
import com.maruchin.medihelper.presentation.feature.options.saved_types.MedicineUnitsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MedicinesListViewModel(
            getAllMedicinesItemsUseCase = get(),
            picturesStorageRef = get(),
            medicinesSorter = get(),
            medicinesFilter = get()
        )
    }
    viewModel {
        MedicineDetailsViewModel(
            getMedicineDetailsUseCase = get(),
            deleteMedicineUseCase = get(),
            picturesStorageRef = get()
        )
    }
    viewModel {
        AddEditMedicineViewModel(
            getMedicineDefaultsUseCase = get(),
            saveMedicineUseCase = get(),
            getMedicineEditDataUseCase = get(),
            deviceCamera = get(),
            picturesStorageRef = get()
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
            setPlannedMedicineTakenUseCase = get()
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
            getLivePlansItemsByProfileUseCase = get(),
            deleteProfileUseCase = get(),
            deviceCalendar = get()
        )
    }
    viewModel {
        AddEditPlanViewModel(
            getPlanEditDataUseCase = get(),
            getProfileSimpleItemUseCase = get(),
            getMedicineSimpleItemUseCase = get(),
            savePlanUseCase = get(),
            deviceCalendar = get()
        )
    }
    viewModel {
        PlanDetailsViewModel(
            getPlanDetailsUseCase = get(),
            getPlanHistoryUseCase = get(),
            deleteSinglePlanUseCase = get(),
            deviceCalendar = get()
        )
    }
    viewModel {
        LoginViewModel(
            signInUseCase = get()
        )
    }
    viewModel {
        OptionsViewModel(
            getCurrUserEmailUseCase = get(),
            areLiveRemindersEnabledUseCase = get(),
            setRemindersEnabledUseCase = get(),
            signOutUseCase = get(),
            getLiveReminderModeUseCase = get(),
            remindersHelp = get()
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
    viewModel {
        MedicineTypesViewModel(
            getLiveMedicineTypesUseCase = get(),
            deleteMedicineTypeUseCase = get(),
            addMedicineTypeUseCase = get()
        )
    }
    viewModel {
        MedicineUnitsViewModel(
            getLiveMedicineUnitsUseCase = get(),
            deleteMedicineUnitUseCase = get(),
            addMedicineUnitUseCase = get()
        )
    }
    viewModel {
        ReminderModeViewModel(
            getReminderModeUseCase = get(),
            setReminderModeUseCase = get()
        )
    }
}