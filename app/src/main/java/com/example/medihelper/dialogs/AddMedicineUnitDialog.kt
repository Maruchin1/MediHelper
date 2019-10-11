package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.DialogAddMedicineUnitBinding

class AddMedicineUnitDialog : AppBottomSheetDialog() {
    override val TAG = "AddMedicineUnitDialog"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogAddMedicineUnitBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_add_medicine_unit
        )
    }
}