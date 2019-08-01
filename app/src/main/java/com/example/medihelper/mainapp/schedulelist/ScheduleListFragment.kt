package com.example.medihelper.mainapp.schedulelist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentScheduleListBinding
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import kotlinx.android.synthetic.main.recycler_item_schedule_list.view.*

class ScheduleListFragment : Fragment() {

    private lateinit var viewModel: ScheduleListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleListViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recycler_view_scheduled_medicine_for_day.adapter = ScheduleListAdapter()
        recycler_view_scheduled_medicine_for_day.layoutManager = LinearLayoutManager(context)
        recycler_view_scheduled_medicine_for_day.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    private fun observeViewModel() {
        viewModel.scheduledMedicineListLive.observe(viewLifecycleOwner, Observer { scheduledMedicineList ->
            val adapter = recycler_view_scheduled_medicine_for_day.adapter as ScheduleListAdapter
            adapter.setScheduledMedicineList(scheduledMedicineList)
        })
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentScheduleListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    inner class ScheduleListAdapter : RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder>() {

        private val scheduledMedicineList = ArrayList<ScheduledMedicine>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_schedule_list, parent, false)
            return ScheduleListViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return scheduledMedicineList.size
        }

        override fun onBindViewHolder(holder: ScheduleListViewHolder, position: Int) {
            val scheduledMedicine = scheduledMedicineList[position]
            holder.view.run {
                txv_medicine_id.text = scheduledMedicine.medicineID.toString()
                txv_start_date.text = scheduledMedicine.startDate.toString()
                txv_end_date.text = scheduledMedicine.endDate.toString()
                txv_schedule_type.text = scheduledMedicine.durationType.toString()
                txv_days_of_week.text = scheduledMedicine.daysOfWeek.toString()
                txv_interval_of_days.text = scheduledMedicine.intervalOfDays.toString()
                txv_schedule_days.text = scheduledMedicine.daysType.toString()
                txv_dose_hour_list.text = scheduledMedicine.timeOfTakingList.toString()
            }
        }

        fun setScheduledMedicineList(list: List<ScheduledMedicine>?) {
            scheduledMedicineList.clear()
            if (list != null) {
                scheduledMedicineList.addAll(list)
            }
            notifyDataSetChanged()
        }

        inner class ScheduleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val view = itemView
        }
    }
}
