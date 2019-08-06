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
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.dialogs.SelectNumberDialog
import com.example.medihelper.dialogs.SelectTimeDialog
import com.example.medihelper.databinding.FragmentAddMedicinePlanBinding
import com.example.medihelper.databinding.RecyclerItemTimeOfTakingBinding
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.schedule.daystype.DaysOfWeekFragment
import com.example.medihelper.mainapp.schedule.daystype.IntervalOfDaysFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeContinuousFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypePeriodFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeOnceFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_medicine_plan.*
import kotlinx.android.synthetic.main.recycler_item_time_of_taking.view.*


class AddMedicinePlanFragment : Fragment() {
    private val TAG = AddMedicinePlanFragment::class.simpleName

    private lateinit var viewModel: AddMedicinePlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddMedicinePlanViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddMedicinePlanBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_medicine_plan, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
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
                    MedicinePlan.DurationType.ONCE -> changeScheduleTypeFragment(onceFragment)
                    MedicinePlan.DurationType.PERIOD -> changeScheduleTypeFragment(periodFragment)
                    MedicinePlan.DurationType.CONTINUOUS -> changeScheduleTypeFragment(continuousFragment)
                }
            }
        })
        val daysOfWeekFragment = DaysOfWeekFragment()
        val intervalOfDaysFragment = IntervalOfDaysFragment()
        viewModel.daysTypeLive.observe(viewLifecycleOwner, Observer { scheduleDays ->
            if (scheduleDays != null) {
                when (scheduleDays) {
                    MedicinePlan.DaysType.EVERYDAY -> changeScheduleDaysFragment(null)
                    MedicinePlan.DaysType.DAYS_OF_WEEK -> changeScheduleDaysFragment(daysOfWeekFragment)
                    MedicinePlan.DaysType.INTERVAL_OF_DAYS -> changeScheduleDaysFragment(intervalOfDaysFragment)
                }
            }
        })
        viewModel.doseHourListLive.observe(viewLifecycleOwner, Observer { doseHourList ->
            val adapter = recycler_view_schedule_hours.adapter as TimeOfTakingAdapter
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
                    viewModel.durationTypeLive.value = MedicinePlan.DurationType.ONCE
                    viewModel.daysTypeLive.value = MedicinePlan.DaysType.NONE
                }
                R.id.chip_period -> {
                    viewModel.durationTypeLive.value = MedicinePlan.DurationType.PERIOD
                    viewModel.daysTypeLive.value = MedicinePlan.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
                R.id.chip_continuous -> {
                    viewModel.durationTypeLive.value = MedicinePlan.DurationType.CONTINUOUS
                    viewModel.daysTypeLive.value = MedicinePlan.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
            }
        }
        chip_group_schedule_type.check(R.id.chip_once)
    }

    private fun setupScheduleDaysChipGroup() {
        chip_group_schedule_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> viewModel.daysTypeLive.value = MedicinePlan.DaysType.EVERYDAY
                R.id.chip_days_of_week -> viewModel.daysTypeLive.value = MedicinePlan.DaysType.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> viewModel.daysTypeLive.value = MedicinePlan.DaysType.INTERVAL_OF_DAYS
            }
        }
    }

    private fun setupDoseHourRecyclerView() {
        recycler_view_schedule_hours.adapter = TimeOfTakingAdapter()
        recycler_view_schedule_hours.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class TimeOfTakingAdapter(
        private val timeOfTakingArrayList: ArrayList<MedicinePlan.TimeOfTaking> = ArrayList()
    ) : RecyclerAdapter(R.layout.recycler_item_time_of_taking, timeOfTakingArrayList) {

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val timeOfTaking = timeOfTakingArrayList[position]
            val timeOfTakingDisplayData = viewModel.getTimeOfTakingDisplayData(timeOfTaking)
            holder.bind(timeOfTakingDisplayData)
            holder.view.apply {
                chip_hour.setOnClickListener {
                    openSelectTimeDialog(position, timeOfTaking)
                }
                lay_dose_size.setOnClickListener {
                    openSelectNumberDialog(position, timeOfTaking)
                }
                btn_delete.setOnClickListener {
                    viewModel.removeDoseHour(timeOfTaking)
                }
            }
        }

        fun setDoseHourList(list: List<MedicinePlan.TimeOfTaking>?) {
            timeOfTakingArrayList.clear()
            if (list != null) {
                timeOfTakingArrayList.addAll(list)
            }
            notifyDataSetChanged()
        }

        private fun openSelectTimeDialog(position: Int, timeOfTaking: MedicinePlan.TimeOfTaking) {
            val dialog = SelectTimeDialog()
            dialog.defaultTime = timeOfTaking.time
            dialog.setTimeSelectedListener { time ->
                viewModel.updateDoseHour(position, timeOfTaking.copy(time = time))
            }
            dialog.show(childFragmentManager, SelectTimeDialog.TAG)
        }

        private fun openSelectNumberDialog(position: Int, timeOfTaking: MedicinePlan.TimeOfTaking) {
            val dialog = SelectNumberDialog()
            dialog.defaultNumber = timeOfTaking.doseSize
            dialog.setNumberSelectedListener { number ->
                Log.d(TAG, "numberSelected")
                viewModel.updateDoseHour(position, timeOfTaking.copy(doseSize = number))
            }
            dialog.show(childFragmentManager, SelectNumberDialog.TAG)
        }
    }
}
