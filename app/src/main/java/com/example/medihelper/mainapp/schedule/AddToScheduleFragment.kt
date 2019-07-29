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

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentAddToScheduleBinding
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.schedule.scheduledays.DaysOfWeekFragment
import com.example.medihelper.mainapp.schedule.scheduledays.IntervalOfDaysFragment
import com.example.medihelper.mainapp.schedule.scheduletype.ScheduleTypeContinuousFragment
import com.example.medihelper.mainapp.schedule.scheduletype.ScheduleTypePeriodFragment
import com.example.medihelper.mainapp.schedule.scheduletype.ScheduleTypeOnceFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_to_schedule.*


class AddToScheduleFragment : Fragment() {
    private val TAG = AddToScheduleFragment::class.simpleName

    private lateinit var viewModel: AddToScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddToScheduleViewModel::class.java)
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
        observeViewModel()
    }

    fun onClickSelectMedicine() {
        val dialog = SelectMedicineDialogFragment()
        dialog.show(childFragmentManager, SelectMedicineDialogFragment.TAG)
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentAddToScheduleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_to_schedule, container, false)
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
                    setOnClickListener { }
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
        viewModel.scheduleTypeLive.observe(viewLifecycleOwner, Observer { scheduleType ->
            if (scheduleType != null) {
                when (scheduleType) {
                    ScheduledMedicine.ScheduleType.ONCE -> changeScheduleTypeFragment(onceFragment)
                    ScheduledMedicine.ScheduleType.PERIOD -> changeScheduleTypeFragment(periodFragment)
                    ScheduledMedicine.ScheduleType.CONTINUOUS -> changeScheduleTypeFragment(continuousFragment)
                }
            }
        })
        val daysOfWeekFragment = DaysOfWeekFragment()
        val intervalOfDaysFragment = IntervalOfDaysFragment()
        viewModel.scheduleDaysLive.observe(viewLifecycleOwner, Observer { scheduleDays ->
            if (scheduleDays != null) {
                when (scheduleDays) {
                    ScheduledMedicine.ScheduleDays.EVERYDAY -> changeScheduleDaysFragment(null)
                    ScheduledMedicine.ScheduleDays.DAYS_OF_WEEK -> changeScheduleDaysFragment(daysOfWeekFragment)
                    ScheduledMedicine.ScheduleDays.INTERVAL_OF_DAYS -> changeScheduleDaysFragment(intervalOfDaysFragment)
                }
            }
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
                R.id.chip_once -> viewModel.scheduleTypeLive.value = ScheduledMedicine.ScheduleType.ONCE
                R.id.chip_period -> viewModel.scheduleTypeLive.value = ScheduledMedicine.ScheduleType.PERIOD
                R.id.chip_continuous -> viewModel.scheduleTypeLive.value = ScheduledMedicine.ScheduleType.CONTINUOUS
            }
        }
        chip_group_schedule_type.check(R.id.chip_once)
    }

    private fun setupScheduleDaysChipGroup() {
        chip_group_schedule_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> viewModel.scheduleDaysLive.value = ScheduledMedicine.ScheduleDays.EVERYDAY
                R.id.chip_days_of_week -> viewModel.scheduleDaysLive.value = ScheduledMedicine.ScheduleDays.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> viewModel.scheduleDaysLive.value = ScheduledMedicine.ScheduleDays.INTERVAL_OF_DAYS
            }
        }
        chip_group_schedule_days.check(R.id.chip_everyday)
    }
}
