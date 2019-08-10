package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.BR

import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentMedicinePlanListBinding
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import kotlinx.android.synthetic.main.fragment_medicine_plan_list.*
import kotlinx.android.synthetic.main.recycler_item_medicine_plan.view.*
import kotlinx.android.synthetic.main.recycler_item_planned_medicine_for_plan_item.view.*


class MedicinePlanListFragment : Fragment() {

    var medicinePlanType: ScheduleViewModel.MedicinePlanType? = null
    private lateinit var viewModel: ScheduleViewModel

    fun onClickDeleteMedicinePlan(medicinePlanID: Int) {
        val dialog = ConfirmDialog().apply {
            title = "Usuń lek z planu"
            message = "Wybrany lek zostanie usunięty z planu. Czy chcesz kontynuować?"
            iconResId = R.drawable.round_delete_black_48
            setOnConfirmClickListener {
                viewModel.deleteMedicinePlan(medicinePlanID)
            }
        }
        dialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    fun onClickOpenPlannedMedicineOptions(plannedMedicineID: Int) {
        val dialog = PlannedMedicineOptionsDialog()
        dialog.plannedMedicineID = plannedMedicineID
        dialog.show(childFragmentManager, dialog.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMedicinePlanListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_plan_list, container, false)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        medicinePlanType?.let {
            viewModel.getMedicinePlanItemListLive(it).observe(viewLifecycleOwner, Observer { medicinePlanList ->
                val adapter = recycler_view_medicine_plan.adapter as MedicinePlanAdapter
                adapter.setItemsList(medicinePlanList)
            })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_medicine_plan.adapter = MedicinePlanAdapter()
        recycler_view_medicine_plan.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class MedicinePlanAdapter : RecyclerAdapter<MedicinePlanItem>(R.layout.recycler_item_medicine_plan) {

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicinePlanItem = itemsArrayList[position]
            val medicinePlanDisplayData = viewModel.getMedicinePlanDisplayData(medicinePlanItem)
            holder.bind(medicinePlanDisplayData, this@MedicinePlanListFragment)

            val plannedMedicinesGroupedByDateList = viewModel.getPlannedMedicinesGroupedByDateList(medicinePlanItem.plannedMedicineList)
            holder.view.recycler_view_planned_medicine_grouped_by_date.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = PlannedMedicineGroupedByDateAdapter().apply {
                    setItemsList(plannedMedicinesGroupedByDateList)
                }
            }
        }
    }

    inner class PlannedMedicineGroupedByDateAdapter : RecyclerAdapter<ScheduleViewModel.PlannedMedicinesGroupedByDate>(R.layout.recycler_item_planned_medicine_for_plan_item) {

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val plannedMedicinesGroupedByDate= itemsArrayList[position]
            holder.bind(plannedMedicinesGroupedByDate.date)

            holder.view.lay_checkboxes.apply {
                removeAllViews()
                plannedMedicinesGroupedByDate.plannedMedicineCheckboxList.sortedBy { plannedMedicineCheckboxList ->
                    plannedMedicineCheckboxList.plannedTime
                }.forEach { plannedMedicineCheckbox ->
                    val plannedMedicineCheckboxDisplayData = viewModel.getPlannedMedicineCheckboxDisplayData(plannedMedicineCheckbox)
                    val binding = DataBindingUtil.inflate<ViewDataBinding>(
                        layoutInflater,
                        R.layout.view_planned_medicine_checkbox,
                        this,
                        true)
                    binding.setVariable(BR.displayData, plannedMedicineCheckboxDisplayData)
                    binding.setVariable(BR.handler, this@MedicinePlanListFragment)
                }
            }
        }
    }
}
