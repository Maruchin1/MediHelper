package com.example.medihelper.mainapp.medicineplan


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentAddEditMedicinePlanBinding
import com.example.medihelper.dialogs.SelectTimeDialog
import com.example.medihelper.dialogs.SelectFloatNumberDialog
import com.example.medihelper.dialogs.SelectMedicineDialog
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.mainapp.medicineplan.daystype.DaysOfWeekFragment
import com.example.medihelper.mainapp.medicineplan.daystype.IntervalOfDaysFragment
import com.example.medihelper.mainapp.medicineplan.durationtype.ContinuousFragment
import com.example.medihelper.mainapp.medicineplan.durationtype.PeriodFragment
import com.example.medihelper.mainapp.medicineplan.durationtype.OnceFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.*
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddEditMedicinePlanFragment : AppFullScreenDialog() {
    private val TAG = "AddEditMedicinePlanFra"

    private val viewModel: AddEditMedicinePlanViewModel by viewModel()
    private val args: AddEditMedicinePlanFragmentArgs by navArgs()
    private val directions by lazyOf(AddEditMedicinePlanFragmentDirections)
    private val timeOfTakingAdapter by lazy { recycler_view_time_of_taking.adapter as TimeOfTakingAdapter }

    fun onClickSelectMedicine() = SelectMedicineDialog().apply {
        setMedicineSelectedListener { medicineID ->
            viewModel.selectedMedicineIDLive.value = medicineID
        }
        viewModel.colorPrimaryLive.value?.let { setColorPrimary(it) }
    }.show(childFragmentManager)

    fun onClickSelectPerson() = findNavController().navigate(AddEditMedicinePlanFragmentDirections.toPersonDialog())

    fun onClickSelectTime(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) = SelectTimeDialog().apply {
        defaultTime = timeOfTaking.time
        setTimeSelectedListener { time ->
            viewModel.updateTimeOfTaking(position, timeOfTaking.copy(time = time))
        }
        viewModel.colorPrimaryLive.value?.let { setColorPrimary(it) }
    }.show(childFragmentManager)

    fun onClickSelectDoseSize(position: Int, timeOfTaking: MedicinePlanEntity.TimeOfTaking) =
        SelectFloatNumberDialog().apply {
            title = "Wybierz dawkę leku"
            iconResID = R.drawable.ic_pill_black_36dp
            defaultNumber = timeOfTaking.doseSize
            setNumberSelectedListener { number ->
                Log.d(TAG, "numberSelected")
                viewModel.updateTimeOfTaking(position, timeOfTaking.copy(doseSize = number))
            }
            viewModel.colorPrimaryLive.value?.let { setColorPrimary(it) }
        }.show(childFragmentManager)

    fun onClickRemoveTimeOfTaking(timeOfTaking: MedicinePlanEntity.TimeOfTaking) =
        viewModel.removeTimeOfTaking(timeOfTaking)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentAddEditMedicinePlanBinding>(
            inflater = inflater,
            layoutResId = R.layout.fragment_add_edit_medicine_plan,
            container = container,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setupToolbar()
        setupDurationTypeChipGroup()
        setupDaysTypeChipGroup()
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
                    MedicinePlanEntity.DurationType.ONCE -> {
                        checkDurationTypeChipGroupItem(R.id.chip_once)
                        changeScheduleTypeFragment(onceFragment)
                    }
                    MedicinePlanEntity.DurationType.PERIOD -> {
                        checkDurationTypeChipGroupItem(R.id.chip_period)
                        changeScheduleTypeFragment(periodFragment)
                    }
                    MedicinePlanEntity.DurationType.CONTINUOUS -> {
                        checkDurationTypeChipGroupItem(R.id.chip_continuous)
                        changeScheduleTypeFragment(continuousFragment)
                    }
                }
            }
        })
        val daysOfWeekFragment = DaysOfWeekFragment()
        val intervalOfDaysFragment = IntervalOfDaysFragment()
        viewModel.daysTypeLive.observe(viewLifecycleOwner, Observer { scheduleDays ->
            if (scheduleDays != null) {
                when (scheduleDays) {
                    MedicinePlanEntity.DaysType.EVERYDAY -> {
                        checkDaysTypeChipGroupItem(R.id.chip_everyday)
                        changeScheduleDaysFragment(null)
                    }
                    MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> {
                        checkDaysTypeChipGroupItem(R.id.chip_days_of_week)
                        changeScheduleDaysFragment(daysOfWeekFragment)
                    }
                    MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> {
                        checkDaysTypeChipGroupItem(R.id.chip_interval_of_days)
                        changeScheduleDaysFragment(intervalOfDaysFragment)
                    }
                }
            }
        })
        viewModel.timeOfTakingListLive.observe(viewLifecycleOwner, Observer { doseHourList ->
            timeOfTakingAdapter.updateItemsList(doseHourList)
        })
        viewModel.colorPrimaryLive.observe(viewLifecycleOwner, Observer { colorResID ->
            dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), colorResID)
        })
        viewModel.errorMessageLive.observe(viewLifecycleOwner, Observer { errorMessage ->
            Snackbar.make(root_lay, errorMessage, Snackbar.LENGTH_SHORT).show()
        })
        viewModel.selectedMedicineAvailableLive.observe(viewLifecycleOwner, Observer {
            timeOfTakingAdapter.notifyDataSetChanged()
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

    private fun checkDurationTypeChipGroupItem(itemID: Int) {
        if (chip_group_duration_type.checkedChipId != itemID) {
            chip_group_duration_type.check(itemID)
        }
    }

    private fun checkDaysTypeChipGroupItem(itemID: Int) {
        if (chip_group_days_type.checkedChipId != itemID) {
            chip_group_days_type.check(itemID)
        }
    }

    private fun setupDurationTypeChipGroup() {
        chip_group_duration_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_once -> {
                    viewModel.durationTypeLive.value = MedicinePlanEntity.DurationType.ONCE
                    viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.NONE
                }
                R.id.chip_period -> {
                    viewModel.durationTypeLive.value = MedicinePlanEntity.DurationType.PERIOD
                    viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.EVERYDAY
                }
                R.id.chip_continuous -> {
                    viewModel.durationTypeLive.value = MedicinePlanEntity.DurationType.CONTINUOUS
                    viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.EVERYDAY
                }
            }
        }
    }

    private fun setupDaysTypeChipGroup() {
        chip_group_days_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.EVERYDAY
                R.id.chip_days_of_week -> viewModel.daysTypeLive.value = MedicinePlanEntity.DaysType.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> viewModel.daysTypeLive.value =
                    MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS
            }
        }
    }

    private fun setupTimeOfTakingRecyclerView() {
        recycler_view_time_of_taking.adapter = TimeOfTakingAdapter()
    }

    // Inner classes
    inner class TimeOfTakingAdapter : RecyclerAdapter<MedicinePlanEntity.TimeOfTaking>(
        R.layout.recycler_item_time_of_taking,
        null
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val timeOfTaking = itemsList[position]
            val timeOfTakingDisplayData = viewModel.getTimeOfTakingDisplayData(timeOfTaking)
            holder.bind(timeOfTakingDisplayData, this@AddEditMedicinePlanFragment, position)
        }
    }
}
