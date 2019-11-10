package com.example.medihelper.mainapp.dialog

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogSelectMedicineUnitBinding
import com.example.medihelper.service.MedicineService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.ext.android.inject

class SelectMedicineUnitDialog : AppBottomSheetDialog() {
    override val TAG = "SelectMedicineUnitDialog"

    val newMedicineUnitLive = MutableLiveData<String>()
    private val medicineService: MedicineService by inject()
    private var medicineUnitSelectedListener: ((medicineUnit: String) -> Unit)? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var binding: DialogSelectMedicineUnitBinding

    fun setMedicineUnitSelectedListener(listener: (medicineUnit: String) -> Unit) {
        medicineUnitSelectedListener = listener
    }

    fun onClickSelectMedicineUnit(medicineUnit: String) {
        medicineUnitSelectedListener?.invoke(medicineUnit)
        dismiss()
    }

    fun onClickAddMedicineType() {
        Log.i(TAG, "onClickAddMedicineType")
        TransitionManager.beginDelayedTransition(binding.rootLay)
        binding.layHeader.visibility = View.GONE
        binding.layAddUnit.visibility = View.VISIBLE
    }

    fun onClickCancelAddMedicineType() {
        TransitionManager.beginDelayedTransition(binding.rootLay)
        binding.layAddUnit.visibility = View.GONE
        binding.layHeader.visibility = View.VISIBLE
        newMedicineUnitLive.value = null
    }

    fun onClickConfirmAddMedicineType() = newMedicineUnitLive.value?.let { newMedicineUnit ->
        val medicineUnitList = medicineService.getMedicineUnitList().toMutableList()
        medicineUnitList.add(newMedicineUnit)
        medicineService.saveMedicineUnitList(medicineUnitList)
        onClickCancelAddMedicineType()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(context, R.layout.dialog_select_medicine_unit, null)
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
        binding.handler = this
        setupRecyclerView()
        observerData()
    }

    private fun observerData() {
        medicineService.getMedicineUnitListLive().observe(requireParentFragment().viewLifecycleOwner, Observer {
            (binding.recyclerViewMedicineUnit.adapter as MedicineUnitAdapter).updateItemsList(it)
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMedicineUnit.apply {
            adapter = MedicineUnitAdapter()
        }
    }

    inner class MedicineUnitAdapter : RecyclerAdapter<String>(
        layoutResId = R.layout.recycler_item_medicine_unit,
        areItemsTheSameFun = { oldItem, newItem -> oldItem == newItem }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val currItem = itemsList[position]
            holder.bind(currItem, this@SelectMedicineUnitDialog)
        }
    }
}