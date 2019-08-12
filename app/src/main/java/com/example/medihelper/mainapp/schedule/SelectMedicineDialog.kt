package com.example.medihelper.mainapp.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.localdatabase.pojos.MedicineKitItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_medicine.*

class SelectMedicineDialog : BottomSheetDialogFragment() {

    private lateinit var viewModel: AddMedicinePlanViewModel

    fun onClickSelectMedicine(medicineID: Int) {
        viewModel.selectedMedicineIDLive.value = medicineID
        findNavController().run {
            if (currentDestination?.id == R.id.schedule_destination) {
                navigate(ScheduleFragmentDirections.toAddMedicinePlanDestination())
            }
        }
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddMedicinePlanViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_select_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_close.setOnClickListener { dismiss() }
        setupRecyclerView()
        setupAddMedicineButton()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        context?.let { context ->
            recycler_view_medicines.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = MedicineAdapter()
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun setupAddMedicineButton() {
        btn_add_medicine.setOnClickListener {
            findNavController().run {
                val direction = when (currentDestination?.id) {
                    R.id.schedule_destination -> ScheduleFragmentDirections.toAddMedicineDestination(-1)
                    R.id.add_medicine_plan_destination -> AddMedicinePlanFragmentDirections.toAddMedicineDestination(-1)
                    else -> null
                }
                direction?.let {
                    navigate(it)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.medicineKitItemListLive.observe(viewLifecycleOwner, Observer { medicineKitItemList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.updateItemsList(medicineKitItemList)
        })
    }

    companion object {
        val TAG = SelectMedicineDialog::class.simpleName
    }

    // Inner classes -------------------------------------------------------------------------------
    inner class MedicineAdapter : RecyclerAdapter<MedicineKitItem>(
        R.layout.recycler_item_select_medicine,
        object : DiffCallback<MedicineKitItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].medicineID == newList[newItemPosition].medicineID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicineKitItem = itemsList[position]
            val medicineDisplayData = viewModel.getMedicineDisplayData(medicineKitItem)
            holder.bind(medicineDisplayData, this@SelectMedicineDialog)
        }
    }
}