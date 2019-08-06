package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.RecyclerItemPlannedMedicineBinding
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import kotlinx.android.synthetic.main.recycler_item_planned_medicine.view.*
import java.util.*
import kotlin.collections.ArrayList


class ScheduleDayFragment : Fragment() {
    private val TAG = ScheduleDayFragment::class.simpleName

    var date: Date? = null
    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        date?.let { dayDate ->
            viewModel.getPlannedMedicinesForDateListLive(dayDate)
                .observe(viewLifecycleOwner, Observer { medicinePlannedForDateList ->
                    Log.d(TAG, "date = $date, scheduledMedicinesList change = $medicinePlannedForDateList")
                    val adapter = recycler_view_scheduled_medicine_for_day.adapter as PlannedMedicineAdapter
                    adapter.setMedicinePlannedForDateList(medicinePlannedForDateList)
                })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_scheduled_medicine_for_day.adapter = PlannedMedicineAdapter()
        recycler_view_scheduled_medicine_for_day.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class PlannedMedicineAdapter : RecyclerView.Adapter<RecyclerItemViewHolder>() {

        private val medicinePlannedForDateArrayList = ArrayList<PlannedMedicine>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
            val binding: RecyclerItemPlannedMedicineBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.recycler_item_planned_medicine,
                parent,
                false
            )
            return RecyclerItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return medicinePlannedForDateArrayList.size
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicinePlannedForDate = medicinePlannedForDateArrayList[position]
            val plannedMedicineDisplayData = viewModel.getPlannedMedicineDisplayData(medicinePlannedForDate)
            holder.bind(plannedMedicineDisplayData)
        }

        fun setMedicinePlannedForDateList(list: List<PlannedMedicine>?) {
            medicinePlannedForDateArrayList.clear()
            if (list != null) {
                medicinePlannedForDateArrayList.addAll(list)
            }
            notifyDataSetChanged()
        }
    }
}
