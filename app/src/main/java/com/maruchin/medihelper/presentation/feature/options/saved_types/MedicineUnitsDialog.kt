package com.maruchin.medihelper.presentation.feature.options.saved_types

import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicineUnitsDialog : SavedTypesDialog() {

    override val viewModel: MedicineUnitsViewModel by viewModel()
}