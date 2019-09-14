package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogSelectMedicineBinding
import com.example.medihelper.localdatabase.pojos.MedicineItem
import kotlinx.android.synthetic.main.dialog_select_medicine.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectMedicineDialog : AppBottomSheetDialog() {
    override val TAG = "SelectMedicineDialog"

    private val viewModel: SelectMedicineViewModel by viewModel()
    private var medicineSelectedListener: ((medicineID: Int) -> Unit)? = null

    fun onClickSelectMedicine(medicineID: Int) {
        medicineSelectedListener?.invoke(medicineID)
        dismiss()
    }

    fun onClickAddNewMedicine() = findNavController().navigate(R.id.addEditMedicineFragment)

    fun setMedicineSelectedListener(listener: (medicineID: Int) -> Unit) {
        medicineSelectedListener = listener
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
                adapter = MedicineAdapter()
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun observeData() {
        viewModel.medicineItemListLive.observe(viewLifecycleOwner, Observer { medicineItemList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.updateItemsList(medicineItemList)
        })
    }

    // Inner classes -------------------------------------------------------------------------------
    inner class MedicineAdapter : RecyclerAdapter<MedicineItem>(
        layoutResId = R.layout.recycler_item_select_medicine,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineID == newItem.medicineID }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicineItem = itemsList[position]
            val medicineDisplayData = viewModel.getMedicineDisplayData(medicineItem)
            holder.bind(medicineDisplayData, this@SelectMedicineDialog)
        }
    }
}