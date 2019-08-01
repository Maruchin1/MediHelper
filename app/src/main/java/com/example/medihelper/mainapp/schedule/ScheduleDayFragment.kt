package com.example.medihelper.mainapp.schedule


import android.os.Bundle
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
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import kotlinx.android.synthetic.main.recycler_item_schedule_medicine.view.*
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList


class ScheduleDayFragment : Fragment() {

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
        date?.let { txv_date.text = AppDateTimeUtil.dateToString(it) }
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        date?.let {
            viewModel.getScheduledMedicinesByDate(it).observe(viewLifecycleOwner, Observer { scheduledMedicineList ->
                val adapter = recycler_view.adapter as ScheduledMedicineForDayAdapter
                adapter.setScheduledMedicineForDayList(viewModel.getScheduledMedicinesForDay(scheduledMedicineList))
            })
        }
    }

    private fun setupRecyclerView() {
        recycler_view.adapter = ScheduledMedicineForDayAdapter()
        recycler_view.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class ScheduledMedicineForDayAdapter :
        RecyclerView.Adapter<ScheduledMedicineForDayAdapter.ScheduledMedicineForDayViewHolder>() {

        private val scheduledMedicineForDayList = ArrayList<ScheduledMedicineForDay>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledMedicineForDayViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_schedule_medicine, parent, false)
            return ScheduledMedicineForDayViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return scheduledMedicineForDayList.size
        }

        override fun onBindViewHolder(holder: ScheduledMedicineForDayViewHolder, position: Int) {
            val scheduledMedicineForDay = scheduledMedicineForDayList[position]
            holder.view.run {
                txv_time.text = AppDateTimeUtil.timeToString(scheduledMedicineForDay.time)
                txv_medicine_dose.text = scheduledMedicineForDay.doseSize.toString()
            }
        }

        fun setScheduledMedicineForDayList(list: List<ScheduledMedicineForDay>?) {
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

    data class ScheduledMedicineForDay(
        val scheduledMedicine: ScheduledMedicine,
        var doseSize: Int,
        var time: Time
    )
}
