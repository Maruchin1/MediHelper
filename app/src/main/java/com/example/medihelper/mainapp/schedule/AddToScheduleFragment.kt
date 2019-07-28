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
import com.example.medihelper.SelectDateDialogFragment
import com.example.medihelper.databinding.FragmentAddToScheduleBinding
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import com.example.medihelper.mainapp.MainActivity
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
//        setupDoseTimeChipGroup()
        observeViewModel()
    }

    fun onClickSelectMedicine(view: View) {
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

    private fun changeScheduleTypeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_schedule_type, fragment)
            .commit()
    }

//    private fun setupDoseTimeChipGroup() {
//        val singleFragment = DoseTimeSingleFragment()
//        val everydayFragment = DoseTimeEverydayFragment()
//        val weekDaysFragment = DoseTimeWeekDaysFragment()
//        val intervalFragment = DoseTimeIntervalFragment()
//        chip_group_dose_days.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.chip_single -> changeDoseTimeFragment(singleFragment)
//                R.id.chip_everyday -> changeDoseTimeFragment(everydayFragment)
//                R.id.chip_week_days -> changeDoseTimeFragment(weekDaysFragment)
//                R.id.chip_interval -> changeDoseTimeFragment(intervalFragment)
//            }
//        }
//        chip_group_dose_days.check(R.id.chip_single)
//    }


    private fun changeDoseTimeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_dose_time_options, fragment)
            .commit()
    }
}
