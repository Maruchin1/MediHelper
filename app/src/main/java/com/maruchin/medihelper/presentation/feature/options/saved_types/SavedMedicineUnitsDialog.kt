package com.maruchin.medihelper.presentation.feature.options.saved_types

import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedMedicineUnitsDialog : SavedTypesDialog() {

    override val viewModel: SavedMedicineUnitsViewModel by viewModel()
}