package com.maruchin.medihelper.presentation.feature.options.saved_types

import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicineTypesDialog : SavedTypesDialog() {

    override val viewModel: MedicineTypesViewModel by viewModel()
}