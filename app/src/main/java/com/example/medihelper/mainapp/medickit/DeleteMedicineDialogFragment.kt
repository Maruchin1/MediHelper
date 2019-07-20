package com.example.medihelper.mainapp.medickit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_delete_medicine.*

class DeleteMedicineDialogFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: MedicineDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(MedicineDetailsViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_delete_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_yes.setOnClickListener {
            viewModel.deleteMedicine()
            findNavController().popBackStack()
        }
        btn_no.setOnClickListener { dismiss() }
    }

    companion object {
        val TAG = DeleteMedicineDialogFragment::class.simpleName
    }
}