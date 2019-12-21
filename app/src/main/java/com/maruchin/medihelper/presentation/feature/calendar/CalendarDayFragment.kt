package com.maruchin.medihelper.presentation.feature.calendar


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.transition.TransitionManager
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentCalendarDayBinding
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.fragment_calendar_day.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CalendarDayFragment : BaseFragment<FragmentCalendarDayBinding>(R.layout.fragment_calendar_day) {
    private val TAG = CalendarDayFragment::class.simpleName

    lateinit var date: AppDate

    private val viewModel: CalendarDayViewModel by viewModel()

    fun onClickOpenPlannedMedicineOptions(plannedMedicineId: String) {
        PlannedMedicineOptionsDialog().apply {
            this.plannedMedicineId = plannedMedicineId
        }.show(childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initData(calendarDayDate = date)
        setupRecyclerViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.noMedicinesForDay.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "plannedMedicineAvailable = $it")
            root_lay.beginDelayedFade()
        })
    }

    private fun setupRecyclerViews() {
        recycler_view_morning_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.morningPlannedMedicines)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_afternoon_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.afternoonPlannedMedicines)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_evening_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.eveningPlannedMedicines)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    // Inner classes
    private inner class PlannedMedicineAdapter(itemsSource: LiveData<List<PlannedMedicineItem>>) :
        BaseRecyclerAdapter<PlannedMedicineItem>(
            layoutResId = R.layout.rec_item_planned_medicine,
            lifecycleOwner = viewLifecycleOwner,
            itemsSource = itemsSource,
            areItemsTheSameFun = { oldItem, newItem -> oldItem.plannedMedicineId == newItem.plannedMedicineId }
        ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val plannedMedicine = itemsList[position]
            holder.bind(plannedMedicine, this@CalendarDayFragment)
        }
    }
}


