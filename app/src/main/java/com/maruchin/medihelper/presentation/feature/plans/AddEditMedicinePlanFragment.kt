package com.maruchin.medihelper.presentation.feature.plans


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
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditMedicinePlanBinding
import com.maruchin.medihelper.presentation.dialogs.SelectTimeDialog
import com.maruchin.medihelper.presentation.dialogs.SelectFloatNumberDialog
import com.maruchin.medihelper.presentation.dialogs.SelectMedicineDialog
import com.maruchin.medihelper.domain.entities.DaysType
import com.maruchin.medihelper.domain.entities.DurationType
import com.maruchin.medihelper.presentation.feature.plans.daystype.DaysOfWeekFragment
import com.maruchin.medihelper.presentation.feature.plans.daystype.IntervalOfDaysFragment
import com.maruchin.medihelper.presentation.feature.plans.durationtype.ContinuousFragment
import com.maruchin.medihelper.presentation.feature.plans.durationtype.PeriodFragment
import com.maruchin.medihelper.presentation.feature.plans.durationtype.OnceFragment
import com.maruchin.medihelper.presentation.framework.*
import com.maruchin.medihelper.presentation.model.TimeDoseFormItem
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
            viewModel.selectedMedicineId.value = medicineID
        }
        viewModel.colorPrimaryId.value?.let { setColorPrimary(it) }
    }.show(childFragmentManager)

    fun onClickSelectPerson() = findNavController().navigate(directions.toPersonDialog())

    fun onClickSelectTime(position: Int, timeDoseFormItem: TimeDoseFormItem) = SelectTimeDialog().apply {
        defaultTime = timeDoseFormItem.time
        setTimeSelectedListener { time ->
            viewModel.updateTimeOfTaking(position, timeDoseFormItem.copy(time = time))
        }
        viewModel.colorPrimaryId.value?.let { setColorPrimary(it) }
    }.show(childFragmentManager)

    fun onClickSelectDoseSize(position: Int, timeDoseFormItem: TimeDoseFormItem) =
        SelectFloatNumberDialog().apply {
            title = "Wybierz dawkę leku"
            iconResID = R.drawable.ic_pill_black_36dp
            defaultNumber = timeDoseFormItem.doseSize
            setNumberSelectedListener { number ->
                Log.d(TAG, "numberSelected")
                viewModel.updateTimeOfTaking(position, timeDoseFormItem.copy(doseSize = number))
            }
            viewModel.colorPrimaryId.value?.let { setColorPrimary(it) }
        }.show(childFragmentManager)

    fun onClickRemoveTimeOfTaking(timeDoseFormItem: TimeDoseFormItem) = viewModel.removeTimeOfTaking(timeDoseFormItem)

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
        val daysOfWeekFragment = DaysOfWeekFragment()
        val intervalOfDaysFragment = IntervalOfDaysFragment()
        viewModel.durationType.observe(viewLifecycleOwner, Observer { durationType ->
            when (durationType) {
                DurationType.ONCE -> {
                    checkDurationTypeChipGroupItem(R.id.chip_once)
                    changeScheduleTypeFragment(onceFragment)
                }
                DurationType.PERIOD -> {
                    checkDurationTypeChipGroupItem(R.id.chip_period)
                    changeScheduleTypeFragment(periodFragment)
                }
                DurationType.CONTINUOUS -> {
                    checkDurationTypeChipGroupItem(R.id.chip_continuous)
                    changeScheduleTypeFragment(continuousFragment)
                }
            }
        })
        viewModel.daysType.observe(viewLifecycleOwner, Observer { daysType ->
            when (daysType) {
                DaysType.EVERYDAY -> {
                    checkDaysTypeChipGroupItem(R.id.chip_everyday)
                    changeScheduleDaysFragment(null)
                }
                DaysType.DAYS_OF_WEEK -> {
                    checkDaysTypeChipGroupItem(R.id.chip_days_of_week)
                    changeScheduleDaysFragment(daysOfWeekFragment)
                }
                DaysType.INTERVAL_OF_DAYS -> {
                    checkDaysTypeChipGroupItem(R.id.chip_interval_of_days)
                    changeScheduleDaysFragment(intervalOfDaysFragment)
                }
            }
        })
        viewModel.timeDoseList.observe(viewLifecycleOwner, Observer { timeDoseList ->
            timeOfTakingAdapter.updateItemsList(timeDoseList)
        })
        viewModel.colorPrimaryId.observe(viewLifecycleOwner, Observer { colorId ->
            dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), colorId)
        })
        viewModel.errorGlobal.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                showShortSnackbar(root_lay, errorMessage)
            }
        })
        viewModel.selectedMedicineShortInfo.observe(viewLifecycleOwner, Observer {
            viewModel.refreshTimeDoseList(it.unit)
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
                    viewModel.durationType.value = DurationType.ONCE
                    viewModel.daysType.value = null
                }
                R.id.chip_period -> {
                    viewModel.durationType.value = DurationType.PERIOD
                    viewModel.daysType.value = DaysType.EVERYDAY
                }
                R.id.chip_continuous -> {
                    viewModel.durationType.value = DurationType.CONTINUOUS
                    viewModel.daysType.value = DaysType.EVERYDAY
                }
            }
        }
    }

    private fun setupDaysTypeChipGroup() {
        chip_group_days_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> viewModel.daysType.value = DaysType.EVERYDAY
                R.id.chip_days_of_week -> viewModel.daysType.value = DaysType.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> viewModel.daysType.value = DaysType.INTERVAL_OF_DAYS
            }
        }
    }

    private fun setupTimeOfTakingRecyclerView() {
        recycler_view_time_of_taking.adapter = TimeOfTakingAdapter()
    }

    // Inner classes
    inner class TimeOfTakingAdapter : RecyclerAdapter<TimeDoseFormItem>(
        R.layout.recycler_item_time_of_taking,
        null
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val timeDose = itemsList[position]
            holder.bind(timeDose, this@AddEditMedicinePlanFragment, position = position)
        }
    }
}
