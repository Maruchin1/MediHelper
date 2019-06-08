package com.example.medihelper.mainapp.schedule

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.Medicine
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_fragment_select_medicine.*

class SelectMedicineDialogFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: AddToScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddToScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_select_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setSelectedMedicine(medicine: Medicine) {
        viewModel.selectedMedicineLive.value = medicine
        dismiss()
    }

    private fun setupRecyclerView() {
        context?.let { context ->
            recycler_view.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SelectMedicineAdapter(
                    context,
                    this@SelectMedicineDialogFragment::setSelectedMedicine
                )
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun observeViewModel() {
        viewModel.medicinesListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (recycler_view.adapter as SelectMedicineAdapter).setMedicinesList(it)
            }
        })
        viewModel.medicinesTypesListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (recycler_view.adapter as SelectMedicineAdapter).setMedicineTypesList(it)
            }
        })
    }

    companion object {
        val TAG = SelectMedicineDialogFragment::class.simpleName
    }
}