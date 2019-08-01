package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.AppDateTimeUtil

import com.example.medihelper.R
import com.example.medihelper.SelectNumberDialog
import com.example.medihelper.SelectTimeDialog
import com.example.medihelper.databinding.FragmentAddScheduledMedicineBinding
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.schedule.daystype.DaysOfWeekFragment
import com.example.medihelper.mainapp.schedule.daystype.IntervalOfDaysFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeContinuousFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypePeriodFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeOnceFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_scheduled_medicine.*
import kotlinx.android.synthetic.main.recycler_item_dose_hour.view.*


class AddScheduledMedicineFragment : Fragment() {
    private val TAG = AddScheduledMedicineFragment::class.simpleName

    private lateinit var viewModel: AddScheduledMedicineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddScheduledMedicineViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
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
        setupMainActivity()
        setupScheduleTypeChipGroup()
        setupScheduleDaysChipGroup()
        setupDoseHourRecyclerView()
        observeViewModel()
    }

    fun onClickSelectMedicine() {
        val dialog = SelectMedicineDialog()
        dialog.show(childFragmentManager, SelectMedicineDialog.TAG)
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentAddScheduledMedicineBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_scheduled_medicine, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    setIconResource(R.drawable.round_save_white_48)
                    text = "Zapisz"
                    extend()
                    setOnClickListener {
                        viewModel.saveScheduledMedicine()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun observeViewModel() {
        viewModel.selectedMedicineLive.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                lay_selected_medicine.visibility = View.INVISIBLE
                txv_medicine_not_selected.visibility = View.VISIBLE
            } else {
                txv_medicine_not_selected.visibility = View.INVISIBLE
                lay_selected_medicine.visibility = View.VISIBLE
            }
        })
        val onceFragment = ScheduleTypeOnceFragment()
        val periodFragment = ScheduleTypePeriodFragment()
        val continuousFragment = ScheduleTypeContinuousFragment()
        viewModel.durationTypeLive.observe(viewLifecycleOwner, Observer { scheduleType ->
            if (scheduleType != null) {
                when (scheduleType) {
                    ScheduledMedicine.DurationType.ONCE -> changeScheduleTypeFragment(onceFragment)
                    ScheduledMedicine.DurationType.PERIOD -> changeScheduleTypeFragment(periodFragment)
                    ScheduledMedicine.DurationType.CONTINUOUS -> changeScheduleTypeFragment(continuousFragment)
                }
            }
        })
        val daysOfWeekFragment = DaysOfWeekFragment()
        val intervalOfDaysFragment = IntervalOfDaysFragment()
        viewModel.daysTypeLive.observe(viewLifecycleOwner, Observer { scheduleDays ->
            if (scheduleDays != null) {
                when (scheduleDays) {
                    ScheduledMedicine.DaysType.EVERYDAY -> changeScheduleDaysFragment(null)
                    ScheduledMedicine.DaysType.DAYS_OF_WEEK -> changeScheduleDaysFragment(daysOfWeekFragment)
                    ScheduledMedicine.DaysType.INTERVAL_OF_DAYS -> changeScheduleDaysFragment(intervalOfDaysFragment)
                }
            }
        })
        viewModel.doseHourListLive.observe(viewLifecycleOwner, Observer { doseHourList ->
            val adapter = recycler_view_schedule_hours.adapter as DoseHourAdapter
            adapter.setDoseHourList(doseHourList)
        })
    }

    private fun changeScheduleTypeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_schedule_type, fragment)
            .commit()
    }

    private fun changeScheduleDaysFragment(fragment: Fragment?) {
        if (fragment == null) {
            childFragmentManager.findFragmentById(R.id.frame_schedule_days)?.let { currFragment ->
                childFragmentManager.beginTransaction()
                    .remove(currFragment)
                    .commit()
            }
        } else {
            childFragmentManager.beginTransaction()
                .replace(R.id.frame_schedule_days, fragment)
                .commit()
        }
    }

    private fun setupScheduleTypeChipGroup() {
        chip_group_schedule_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_once -> {
                    viewModel.durationTypeLive.value = ScheduledMedicine.DurationType.ONCE
                    viewModel.daysTypeLive.value = ScheduledMedicine.DaysType.NONE
                }
                R.id.chip_period -> {
                    viewModel.durationTypeLive.value = ScheduledMedicine.DurationType.PERIOD
                    viewModel.daysTypeLive.value = ScheduledMedicine.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
                R.id.chip_continuous -> {
                    viewModel.durationTypeLive.value = ScheduledMedicine.DurationType.CONTINUOUS
                    viewModel.daysTypeLive.value = ScheduledMedicine.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
            }
        }
        chip_group_schedule_type.check(R.id.chip_once)
    }

    private fun setupScheduleDaysChipGroup() {
        chip_group_schedule_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> viewModel.daysTypeLive.value = ScheduledMedicine.DaysType.EVERYDAY
                R.id.chip_days_of_week -> viewModel.daysTypeLive.value = ScheduledMedicine.DaysType.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> viewModel.daysTypeLive.value = ScheduledMedicine.DaysType.INTERVAL_OF_DAYS
            }
        }
    }

    private fun setupDoseHourRecyclerView() {
        recycler_view_schedule_hours.adapter = DoseHourAdapter()
        recycler_view_schedule_hours.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class DoseHourAdapter : RecyclerView.Adapter<DoseHourAdapter.DoseHourViewHolder>() {

        private var doseHourList = ArrayList<ScheduledMedicine.TimeOfTaking>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoseHourViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_dose_hour, parent, false)
            return DoseHourViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return doseHourList.size
        }

        override fun onBindViewHolder(holder: DoseHourViewHolder, position: Int) {
            val doseHour = doseHourList[position]
            holder.view.apply {
                chip_hour.text = AppDateTimeUtil.timeToString(doseHour.time)
                txv_dose_size.text = doseHour.doseSize.toString()
                txv_medicine_type.text = viewModel.selectedMedicineTypeLive.value?.typeName ?: "brak typu"

                chip_hour.setOnClickListener {
                    openSelectTimeDialog(position, doseHour)
                }
                lay_dose_size.setOnClickListener {
                    openSelectNumberDialog(position, doseHour)
                }
                btn_delete.setOnClickListener {
                    viewModel.removeDoseHour(doseHour)
                }
            }
        }

        fun setDoseHourList(list: List<ScheduledMedicine.TimeOfTaking>?) {
            doseHourList.clear()
            if (list != null) {
                doseHourList.addAll(list)
            }
            notifyDataSetChanged()
        }

        private fun openSelectTimeDialog(position: Int, timeOfTaking: ScheduledMedicine.TimeOfTaking) {
            val dialog = SelectTimeDialog()
            dialog.defaultTime = timeOfTaking.time
            dialog.setTimeSelectedListener { time ->
                viewModel.updateDoseHour(position, timeOfTaking.copy(time = time))
            }
            dialog.show(childFragmentManager, SelectTimeDialog.TAG)
        }

        private fun openSelectNumberDialog(position: Int, timeOfTaking: ScheduledMedicine.TimeOfTaking) {
            val dialog = SelectNumberDialog()
            dialog.defaultNumber = timeOfTaking.doseSize
            dialog.setNumberSelectedListener { number ->
                Log.d(TAG, "numberSelected")
                viewModel.updateDoseHour(position, timeOfTaking.copy(doseSize = number))
            }
            dialog.show(childFragmentManager, SelectNumberDialog.TAG)
        }

        inner class DoseHourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val view = itemView
        }
    }
}
