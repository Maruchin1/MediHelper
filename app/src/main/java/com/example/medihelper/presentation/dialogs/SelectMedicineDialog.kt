package com.example.medihelper.presentation.dialogs

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.presentation.framework.AppBottomSheetDialog
import com.example.medihelper.presentation.framework.RecyclerAdapter
import com.example.medihelper.presentation.framework.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogSelectMedicineBinding
import com.example.medihelper.presentation.feature.medikit.MedicinesListViewModel
import com.example.medihelper.presentation.model.MedicineItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectMedicineDialog : AppBottomSheetDialog() {
    override val TAG = "SelectMedicineDialog"

    private val viewModel: MedicinesListViewModel by viewModel()
    private var medicineSelectedListener: ((medicineID: Int) -> Unit)? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var binding: DialogSelectMedicineBinding

    fun onClickSelectMedicine(medicineID: Int) {
        medicineSelectedListener?.invoke(medicineID)
        dismiss()
    }

    fun setMedicineSelectedListener(listener: (medicineID: Int) -> Unit) {
        medicineSelectedListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(context, R.layout.dialog_select_medicine, null)
        binding = DataBindingUtil.bind(view)!!

        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setContentView(view)

        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = (Resources.getSystem().displayMetrics.heightPixels) / 2
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        onViewCreated(view, savedInstanceState)

        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMedicines.adapter = MedicineAdapter()
    }

    private fun observeData() {
        viewModel.medicineItemList.observe(
            requireParentFragment().viewLifecycleOwner,
            Observer { medicineItemList ->
                val adapter = binding.recyclerViewMedicines.adapter as MedicineAdapter
                adapter.updateItemsList(medicineItemList)
            })
    }

    private fun setupToolbar() {
        val itemSearch = binding.toolbar.menu.findItem(R.id.btn_search)
        val searchView = itemSearch.actionView as SearchView
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            when {
                hasFocus -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                else -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.nameQuery.value = newText
                return true
            }
        })
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btn_add -> findNavController().navigate(R.id.addEditMedicineFragment)
            }
            true
        }
    }

    // Inner classes -------------------------------------------------------------------------------
    inner class MedicineAdapter : RecyclerAdapter<MedicineItem>(
        layoutResId = R.layout.recycler_item_select_medicine,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineId == newItem.medicineId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicineItem = itemsList[position]
            holder.bind(medicineItem, this@SelectMedicineDialog)
        }
    }
}