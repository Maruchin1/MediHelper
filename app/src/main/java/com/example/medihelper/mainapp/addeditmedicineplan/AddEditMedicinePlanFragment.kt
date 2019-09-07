package com.example.medihelper.mainapp.addeditmedicineplan


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.custom.TAG
import com.example.medihelper.databinding.FragmentAddEditMedicinePlanBinding
import com.example.medihelper.dialogs.SelectNumberDialog
import com.example.medihelper.dialogs.SelectTimeDialog
import com.example.medihelper.dialogs.SelectFloatNumberDialog
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.mainapp.addeditmedicineplan.daystype.DaysOfWeekFragment
import com.example.medihelper.mainapp.addeditmedicineplan.daystype.IntervalOfDaysFragment
import com.example.medihelper.mainapp.addeditmedicineplan.durationtype.ContinuousFragment
import com.example.medihelper.mainapp.addeditmedicineplan.durationtype.PeriodFragment
import com.example.medihelper.mainapp.addeditmedicineplan.durationtype.OnceFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.*
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.toolbar


class AddEditMedicinePlanFragment : AppFullScreenDialog() {

    private val viewModel: AddEditMedicinePlanViewModel by activityViewModels()
    private val args: AddEditMedicinePlanFragmentArgs by navArgs()
    private val directions by lazyOf(AddEditMedicinePlanFragmentDirections)

    fun onClickSelectMedicine() = findNavController().navigate(directions.toSelectMedicineDialog())

    fun onClickSelectPerson() = findNavController().navigate(directions.toPersonDialog())

    fun onClickSelectTime(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        val dialog = SelectTimeDialog().apply {
            defaultTime = timeOfTaking.time
            setTimeSelectedListener { time ->
                viewModel.updateTimeOfTaking(position, timeOfTaking.copy(time = time))
            }
        }
        dialog.show(childFragmentManager, SelectTimeDialog.TAG)
    }

    fun onClickSelectDoseSize(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) {
        val dialog = SelectFloatNumberDialog().apply {
            title = "Wybierz dawkę leku"
            iconResID = R.drawable.ic_pill_black_36dp
            defaultNumber = timeOfTaking.doseSize
            setNumberSelectedListener { number ->
                Log.d(TAG, "numberSelected")
                viewModel.updateTimeOfTaking(position, timeOfTaking.copy(doseSize = number))
            }
        }
        dialog.show(childFragmentManager, SelectNumberDialog.TAG)
    }

    fun onClickRemoveTimeOfTaking(timeOfTaking: MedicinePlanEntity.TimeOfTaking) = viewModel.removeTimeOfTaking(timeOfTaking)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAddEditMedicinePlanBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_medicine_plan, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setupToolbar()
        setupScheduleTypeChipGroup()
        setupScheduleDaysChipGroup()
        setupTimeOfTakingRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_save -> {
                    val medicinePlanSaved = viewModel.saveMedicinePlan()
                    if (medicinePlanSaved) {
                        dismiss()
                    }
                }
            }
            true
        }
    }

    private fun observeViewModel() {
        val onceFragment = OnceFragment()
        val periodFragment = PeriodFragment()
        val continuousFragment = ContinuousFragment()
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
            adapter.updateItemsList(doseHourList.toList())
        })
        viewModel.colorPrimaryLive.observe(viewLifecycleOwner, Observer { colorResID ->
            context?.run {
                dialog?.window?.statusBarColor = ContextCompat.getColor(this, colorResID)
            }
        })
        viewModel.errorSelectedMedicineAction.observe(viewLifecycleOwner, Observer {
            Snackbar.make(root_lay, "Nie wybrano leku", Snackbar.LENGTH_SHORT).show()
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
    }

    // Inner classes
    inner class TimeOfTakingAdapter : RecyclerAdapter<MedicinePlanEntity.TimeOfTaking>(R.layout.recycler_item_time_of_taking, null) {

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val timeOfTaking = itemsList[position]
            val timeOfTakingDisplayData = viewModel.getTimeOfTakingDisplayData(timeOfTaking)
            holder.bind(timeOfTakingDisplayData, this@AddEditMedicinePlanFragment, position)
        }
    }
}
