package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.transition.TransitionManager
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.AppDateTime
import com.example.medihelper.BR

import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentMedicinePlanListBinding
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.android.synthetic.main.fragment_medicine_plan_list.recycler_view_medicine_plan
import kotlinx.android.synthetic.main.recycler_item_medicine_plan.view.*
import kotlinx.android.synthetic.main.recycler_item_medicine_plan.view.recycler_view_planned_medicine_checkbox
import kotlinx.android.synthetic.main.recycler_item_planned_medicine_for_plan_item.view.*


class MedicinePlanListFragment : Fragment() {

    var medicinePlanType: ScheduleViewModel.MedicinePlanType? = null
    set(value) {
        field = value
        when (medicinePlanType) {
            ScheduleViewModel.MedicinePlanType.ENDED -> unavailableMessage =  "Brak zakończonych planów"
            ScheduleViewModel.MedicinePlanType.ONGOING -> unavailableMessage =  "Brak trwających planów"
        }
    }
    var unavailableMessage = ""
    val medicinePlanAvailableLive = MutableLiveData(false)
    private lateinit var viewModel: ScheduleViewModel


    fun onClickDeleteMedicinePlan(medicinePlanID: Int) {
        val dialog = ConfirmDialog().apply {
            title = "Usuń lek z planu"
            message = "Wybrany lek zostanie usunięty z planu. Czy chcesz kontynuować?"
            iconResId = R.drawable.round_delete_black_36
            setOnConfirmClickListener {
                viewModel.deleteMedicinePlan(medicinePlanID)
            }
        }
        dialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
                adapter.updateItemsList(medicinePlanList)
                medicinePlanAvailableLive.value = !medicinePlanList.isNullOrEmpty()
            })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_medicine_plan.adapter = MedicinePlanAdapter()
    }

    // Inner classes
    inner class MedicinePlanAdapter : RecyclerAdapter<MedicinePlanItem>(
        R.layout.recycler_item_medicine_plan,
        object : DiffCallback<MedicinePlanItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].medicinePlanID == newList[newItemPosition].medicinePlanID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicinePlanItem = itemsList[position]
            val medicinePlanDisplayData = viewModel.getMedicinePlanDisplayData(medicinePlanItem)
            holder.bind(medicinePlanDisplayData, this@MedicinePlanListFragment)

            holder.view.recycler_view_planned_medicine_checkbox.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = PlannedMedicineGroupedByDateAdapter().apply {
                    updateItemsList(viewModel.getPlannedMedicinesGroupedByDateList(medicinePlanItem.plannedMedicineList))
                }
                GravitySnapHelper(Gravity.START).attachToRecyclerView(this)
            }

            holder.view.run {
                recycler_view_planned_medicine_checkbox.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = PlannedMedicineGroupedByDateAdapter().apply {
                        updateItemsList(viewModel.getPlannedMedicinesGroupedByDateList(medicinePlanItem.plannedMedicineList))
                    }
                }
                btn_show_hide_history.setOnClickListener {
                    TransitionManager.beginDelayedTransition(this@MedicinePlanListFragment.recycler_view_medicine_plan)
                    when (recycler_view_planned_medicine_checkbox.visibility) {
                        View.GONE -> {
                            recycler_view_planned_medicine_checkbox.visibility = View.VISIBLE
                            btn_show_hide_history.apply {
                                text = "Ukryj historię"
                                setIconResource(R.drawable.round_keyboard_arrow_up_24)
                            }
                        }
                        View.VISIBLE -> {
                            recycler_view_planned_medicine_checkbox.visibility = View.GONE
                            btn_show_hide_history.apply {
                                text = "Pokaż historię"
                                setIconResource(R.drawable.round_keyboard_arrow_down_24)
                            }
                        }
                    }
                }
            }
        }
    }

    inner class PlannedMedicineGroupedByDateAdapter : RecyclerAdapter<ScheduleViewModel.PlannedMedicineCheckboxGroupedByDate>(
        R.layout.recycler_item_planned_medicine_for_plan_item,
        object : DiffCallback<ScheduleViewModel.PlannedMedicineCheckboxGroupedByDate>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return AppDateTime.compareDates(oldList[oldItemPosition].date, newList[newItemPosition].date) == 0
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val plannedMedicinesGroupedByDate = itemsList[position]
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
                        true
                    )
                    binding.setVariable(BR.displayData, plannedMedicineCheckboxDisplayData)
                    binding.setVariable(BR.handler, this@MedicinePlanListFragment)
                }
            }
        }
    }
}
