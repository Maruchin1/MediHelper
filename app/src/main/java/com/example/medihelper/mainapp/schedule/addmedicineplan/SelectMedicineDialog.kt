package com.example.medihelper.mainapp.schedule.addmedicineplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogSelectMedicineBinding
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_medicine.*
import java.io.File

class SelectMedicineDialog : BottomSheetDialogFragment() {
    val TAG = SelectMedicineDialog::class.simpleName

    private val viewModel: AddMedicinePlanViewModel by activityViewModels()

    fun onClickSelectMedicine(medicineID: Int) {
        viewModel.selectedMedicineIDLive.value = medicineID
        dismiss()
    }

    fun onClickAddNewMedicine() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogSelectMedicineBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_medicine, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
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

    private fun observeData() {
        AppRepository.getMedicineItemListLive().observe(viewLifecycleOwner, Observer { medicineKitItemList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.updateItemsList(medicineKitItemList)
        })
    }

    private fun getMedicineDisplayData(medicineItem: MedicineItem): MedicineItemDisplayData {
        return MedicineItemDisplayData(
            medicineID = medicineItem.medicineID,
            medicineName = medicineItem.medicineName,
            medicineState = "${medicineItem.currState}/${medicineItem.packageSize} ${medicineItem.medicineUnit}",
            medicineImageFile = medicineItem.photoFilePath?.let { File(it) }
        )
    }

    // Inner classes -------------------------------------------------------------------------------
    inner class MedicineAdapter : RecyclerAdapter<MedicineItem>(
        R.layout.recycler_item_select_medicine,
        object : DiffCallback<MedicineItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].medicineID == newList[newItemPosition].medicineID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicineKitItem = itemsList[position]
            val medicineDisplayData = getMedicineDisplayData(medicineKitItem)
            holder.bind(medicineDisplayData, this@SelectMedicineDialog)
        }
    }

    data class MedicineItemDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val medicineState: String,
        val medicineImageFile: File?
    )
}