package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentScheduleListBinding
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import kotlinx.android.synthetic.main.recycler_item_scheduled_medicine.view.*

class ScheduleListFragment : Fragment() {
    private val TAG = ScheduleListFragment::class.simpleName

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
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recycler_view_scheduled_medicine.adapter = ScheduledMedicineAdapter()
        recycler_view_scheduled_medicine.layoutManager = LinearLayoutManager(context)
        recycler_view_scheduled_medicine.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    private fun observeViewModel() {
        viewModel.scheduledMedicineListLive.observe(viewLifecycleOwner, Observer { scheduledMedicineList ->
            val adapter = recycler_view_scheduled_medicine.adapter as ScheduledMedicineAdapter
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

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    // Inner classes
    inner class ScheduledMedicineAdapter : RecyclerView.Adapter<ScheduledMedicineAdapter.ScheduledMedicineViewHolder>() {

        private val scheduledMedicineList = ArrayList<ScheduledMedicine>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledMedicineViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_scheduled_medicine, parent, false)
            return ScheduledMedicineViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return scheduledMedicineList.size
        }

        override fun onBindViewHolder(holder: ScheduledMedicineViewHolder, position: Int) {
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

        inner class ScheduledMedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val view = itemView
        }
    }
}
