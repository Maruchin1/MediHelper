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
import com.example.medihelper.dialogs.SelectNumberDialog
import com.example.medihelper.dialogs.SelectTimeDialog
import com.example.medihelper.databinding.FragmentAddMedicinePlanBinding
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.schedule.daystype.DaysOfWeekFragment
import com.example.medihelper.mainapp.schedule.daystype.IntervalOfDaysFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeContinuousFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypePeriodFragment
import com.example.medihelper.mainapp.schedule.durationtype.ScheduleTypeOnceFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_medicine_plan.*
import kotlinx.android.synthetic.main.recycler_item_dose_hour.view.*


class AddMedicinePlanFragment : Fragment() {
    private val TAG = AddMedicinePlanFragment::class.simpleName

    private lateinit var planViewModel: AddMedicinePlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            planViewModel = ViewModelProviders.of(this).get(AddMedicinePlanViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddMedicinePlanBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_medicine_plan, container, false)
        binding.viewModel = planViewModel
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
                        planViewModel.saveScheduledMedicine()
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
        planViewModel.selectedMedicineLive.observe(viewLifecycleOwner, Observer {
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
        planViewModel.durationTypeLive.observe(viewLifecycleOwner, Observer { scheduleType ->
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
        planViewModel.daysTypeLive.observe(viewLifecycleOwner, Observer { scheduleDays ->
            if (scheduleDays != null) {
                when (scheduleDays) {
                    MedicinePlan.DaysType.EVERYDAY -> changeScheduleDaysFragment(null)
                    MedicinePlan.DaysType.DAYS_OF_WEEK -> changeScheduleDaysFragment(daysOfWeekFragment)
                    MedicinePlan.DaysType.INTERVAL_OF_DAYS -> changeScheduleDaysFragment(intervalOfDaysFragment)
                }
            }
        })
        planViewModel.doseHourListLive.observe(viewLifecycleOwner, Observer { doseHourList ->
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
                    planViewModel.durationTypeLive.value = MedicinePlan.DurationType.ONCE
                    planViewModel.daysTypeLive.value = MedicinePlan.DaysType.NONE
                }
                R.id.chip_period -> {
                    planViewModel.durationTypeLive.value = MedicinePlan.DurationType.PERIOD
                    planViewModel.daysTypeLive.value = MedicinePlan.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
                R.id.chip_continuous -> {
                    planViewModel.durationTypeLive.value = MedicinePlan.DurationType.CONTINUOUS
                    planViewModel.daysTypeLive.value = MedicinePlan.DaysType.EVERYDAY
                    chip_group_schedule_days.check(R.id.chip_everyday)
                }
            }
        }
        chip_group_schedule_type.check(R.id.chip_once)
    }

    private fun setupScheduleDaysChipGroup() {
        chip_group_schedule_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> planViewModel.daysTypeLive.value = MedicinePlan.DaysType.EVERYDAY
                R.id.chip_days_of_week -> planViewModel.daysTypeLive.value = MedicinePlan.DaysType.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> planViewModel.daysTypeLive.value = MedicinePlan.DaysType.INTERVAL_OF_DAYS
            }
        }
    }

    private fun setupDoseHourRecyclerView() {
        recycler_view_schedule_hours.adapter = DoseHourAdapter()
        recycler_view_schedule_hours.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class DoseHourAdapter : RecyclerView.Adapter<DoseHourAdapter.DoseHourViewHolder>() {

        private var doseHourList = ArrayList<MedicinePlan.TimeOfTaking>()

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
                txv_medicine_type.text = planViewModel.selectedMedicineTypeLive.value?.typeName ?: "brak typu"

                chip_hour.setOnClickListener {
                    openSelectTimeDialog(position, doseHour)
                }
                lay_dose_size.setOnClickListener {
                    openSelectNumberDialog(position, doseHour)
                }
                btn_delete.setOnClickListener {
                    planViewModel.removeDoseHour(doseHour)
                }
            }
        }

        fun setDoseHourList(list: List<MedicinePlan.TimeOfTaking>?) {
            doseHourList.clear()
            if (list != null) {
                doseHourList.addAll(list)
            }
            notifyDataSetChanged()
        }

        private fun openSelectTimeDialog(position: Int, timeOfTaking: MedicinePlan.TimeOfTaking) {
            val dialog = SelectTimeDialog()
            dialog.defaultTime = timeOfTaking.time
            dialog.setTimeSelectedListener { time ->
                planViewModel.updateDoseHour(position, timeOfTaking.copy(time = time))
            }
            dialog.show(childFragmentManager, SelectTimeDialog.TAG)
        }

        private fun openSelectNumberDialog(position: Int, timeOfTaking: MedicinePlan.TimeOfTaking) {
            val dialog = SelectNumberDialog()
            dialog.defaultNumber = timeOfTaking.doseSize
            dialog.setNumberSelectedListener { number ->
                Log.d(TAG, "numberSelected")
                planViewModel.updateDoseHour(position, timeOfTaking.copy(doseSize = number))
            }
            dialog.show(childFragmentManager, SelectNumberDialog.TAG)
        }

        inner class DoseHourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val view = itemView
        }
    }
}
