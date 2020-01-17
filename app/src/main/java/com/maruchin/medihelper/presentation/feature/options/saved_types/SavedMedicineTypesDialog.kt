package com.maruchin.medihelper.presentation.feature.options.saved_types

import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedMedicineTypesDialog : SavedTypesDialog() {

    override val viewModel: SavedMedicineTypesViewModel by viewModel()
}