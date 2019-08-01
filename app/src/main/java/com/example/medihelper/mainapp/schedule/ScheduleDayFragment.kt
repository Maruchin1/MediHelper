package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.AppDateTimeUtil
import androidx.lifecycle.Observer
import com.example.medihelper.R
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import kotlinx.android.synthetic.main.recycler_item_scheduled_medicine_for_day.view.*
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        date?.let {
            viewModel.getScheduledMedicinesByDate(it).observe(viewLifecycleOwner, Observer { scheduledMedicineList ->
                Log.d(TAG, "date = $date, scheduledMedicinesList change = $scheduledMedicineList")
                val adapter = recycler_view_scheduled_medicine_for_day.adapter as ScheduledMedicineForDayAdapter
                adapter.setScheduledMedicineForDayList(viewModel.getScheduledMedicinesForDay(scheduledMedicineList))
            })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_scheduled_medicine_for_day.adapter = ScheduledMedicineForDayAdapter()
        recycler_view_scheduled_medicine_for_day.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class ScheduledMedicineForDayAdapter :
        RecyclerView.Adapter<ScheduledMedicineForDayAdapter.ScheduledMedicineForDayViewHolder>() {

        private val scheduledMedicineForDayList = ArrayList<ScheduleViewModel.ScheduledMedicineForDay>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledMedicineForDayViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_scheduled_medicine_for_day, parent, false)
            return ScheduledMedicineForDayViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return scheduledMedicineForDayList.size
        }

        override fun onBindViewHolder(holder: ScheduledMedicineForDayViewHolder, position: Int) {
            val scheduledMedicineForDay = scheduledMedicineForDayList[position]
            holder.view.run {
                txv_scheduled_time.text = AppDateTimeUtil.timeToString(scheduledMedicineForDay.time)
                txv_scheduled_dose.text = scheduledMedicineForDay.doseSize.toString()
                txv_medicine_name.text = scheduledMedicineForDay.medicineName
            }
        }

        fun setScheduledMedicineForDayList(list: List<ScheduleViewModel.ScheduledMedicineForDay>?) {
            scheduledMedicineForDayList.clear()
            if (list != null) {
                scheduledMedicineForDayList.addAll(list)
            }
            notifyDataSetChanged()
        }

        inner class ScheduledMedicineForDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var view = itemView
        }
    }
}
