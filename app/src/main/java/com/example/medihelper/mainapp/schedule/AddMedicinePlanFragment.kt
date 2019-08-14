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
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.dialogs.SelectNumberDialog
import com.example.medihelper.dialogs.SelectTimeDialog
import com.example.medihelper.databinding.FragmentAddMedicinePlanBinding
import com.example.medihelper.dialogs.SelectPersonDialog
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.schedule.daystype.DaysOfWeekFragment
import com.example.medihelper.mainapp.schedule.daystype.IntervalOfDaysFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeContinuousFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypePeriodFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeOnceFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_medicine_plan.*


class AddMedicinePlanFragment : Fragment() {
    private val TAG = AddMedicinePlanFragment::class.simpleName

    private val args: AddMedicinePlanFragmentArgs by navArgs()
    private lateinit var viewModel: AddMedicinePlanViewModel

    fun onClickSelectMedicine() {
        val dialog = SelectMedicineDialog().apply {
            setMedicineSelectedListener { medicineID ->
                viewModel.selectedMedicineIDLive.value = medicineID
            }
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }

    fun onClickSelectPerson() {
        val dialog = SelectPersonDialog().apply {
            setPersonSelectedListener { personID ->
                viewModel.selectedPersonIDLive.value = personID
            }
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }

    fun onClickSelectTime(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        val dialog = SelectTimeDialog()
        dialog.defaultTime = timeOfTaking.time
        dialog.setTimeSelectedListener { time ->
            viewModel.updateTimeOfTaking(position, timeOfTaking.copy(time = time))
        }
        dialog.show(childFragmentManager, SelectTimeDialog.TAG)
    }

    fun onClickSelectDoseSize(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        val dialog = SelectNumberDialog()
        dialog.defaultNumber = timeOfTaking.doseSize
        dialog.setNumberSelectedListener { number ->
            Log.d(TAG, "numberSelected")
            viewModel.updateTimeOfTaking(position, timeOfTaking.copy(doseSize = number))
        }
        dialog.show(childFragmentManager, SelectNumberDialog.TAG)
    }

    fun onClickRemoveTimeOfTaking(timeOfTaking: MedicinePlanEntity.TimeOfTaking) = viewModel.removeTimeOfTaking(timeOfTaking)

    fun onClickSaveNewMedicinePlan() {
        viewModel.saveMedicinePlan()
        findNavController().popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddMedicinePlanViewModel::class.java)
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
        setupTimeOfTakingRecyclerView()
        setDefaultArguments()
        observeViewModel()
    }

    private fun setDefaultArguments() {
        viewModel.selectedPersonIDLive.value = args.personID
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.hide()
            }
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun observeViewModel() {
        val onceFragment = ScheduleTypeOnceFragment()
        val periodFragment = ScheduleTypePeriodFragment()
        val continuousFragment = ScheduleTypeContinuousFragment()
        viewModel.durationTypeLive.observe(viewLifecycleOwner, Observer { scheduleType ->
            if (scheduleType != null) {
                when (scheduleType) {
                    MedicinePlanEntity.DurationType.ONCE -> changeScheduleTypeFragment(onceFragment)
                    MedicinePlanEntity.DurationType.PERIOD -> changeScheduleTypeFragment(periodFragment)
                    MedicinePlanEntity.DurationType.CONTINUOUS -> changeScheduleTypeFragment(continuousFragment)
                }
            }
        })
        val daysOfWeekFragment = DaysOfWeekFragment()
        val intervalOfDaysFragment = IntervalOfDaysFragment()
        viewModel.daysTypeLive.observe(viewLifecycleOwner, Observer { scheduleDays ->
            if (scheduleDays != null) {
                when (scheduleDays) {
                    MedicinePlanEntity.DaysType.EVERYDAY -> changeScheduleDaysFragment(null)
                    MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> changeScheduleDaysFragment(daysOfWeekFragment)
                    MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> changeScheduleDaysFragment(intervalOfDaysFragment)
                }
            }
        })
        viewModel.timeOfTakingListLive.observe(viewLifecycleOwner, Observer { doseHourList ->
            val adapter = recycler_view_schedule_hours.adapter as TimeOfTakingAdapter
            adapter.updateItemsList(doseHourList)
        })
        viewModel.personSimpleItemLive.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "person change = $it")
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
                    viewModel.durationTypeLive.value = MedicinePlanEntity.DurationType.ONCE
                    viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.NONE
                }
                R.id.chip_period -> {
                    viewModel.durationTypeLive.value = MedicinePlanEntity.DurationType.PERIOD
                    viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
                R.id.chip_continuous -> {
                    viewModel.durationTypeLive.value = MedicinePlanEntity.DurationType.CONTINUOUS
                    viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
            }
        }
        chip_group_schedule_type.check(R.id.chip_once)
    }

    private fun setupScheduleDaysChipGroup() {
        chip_group_schedule_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.EVERYDAY
                R.id.chip_days_of_week -> viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS
            }
        }
    }

    private fun setupTimeOfTakingRecyclerView() {
        recycler_view_schedule_hours.adapter = TimeOfTakingAdapter()
        recycler_view_schedule_hours.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class TimeOfTakingAdapter : RecyclerAdapter<MedicinePlanEntity.TimeOfTaking>(R.layout.recycler_item_time_of_taking, null) {

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val timeOfTaking = itemsList[position]
            val timeOfTakingDisplayData = viewModel.getTimeOfTakingDisplayData(timeOfTaking)
            holder.bind(timeOfTakingDisplayData, this@AddMedicinePlanFragment, position)
        }
    }
}
