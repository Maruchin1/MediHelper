package com.maruchin.medihelper.presentation.feature.mediplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditMedicinePlanBinding
import com.maruchin.medihelper.presentation.dialogs.SelectDateDialog
import com.maruchin.medihelper.presentation.framework.BaseFragment
import com.maruchin.medihelper.presentation.framework.shrinkOnScroll
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEditMedicinePlanFragment :
    BaseFragment<FragmentAddEditMedicinePlanBinding>(R.layout.fragment_add_edit_medicine_plan) {

    private val viewModel: AddEditMedicinePlanViewModel by viewModel()
    private val args: AddEditMedicinePlanFragmentArgs by navArgs()

    fun onClickSelectStartDate() {
        SelectDateDialog().apply {
            defaultDate = viewModel.startDate.value
            setDateSelectedListener { date ->
                viewModel.startDate.value = date
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectEndDate() {
        SelectDateDialog().apply {
            defaultDate = viewModel.endDate.value
            setDateSelectedListener { date ->
                viewModel.endDate.value = date
            }
        }.show(childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.setLightStatusBar(false)
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setupToolbarNavigation()
        viewModel.setArgs(args)
        setupFab()
        setupDurationTypeChipGroup()
        setupIntakeDaysChipGroup()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.durationType.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                AddEditMedicinePlanViewModel.DurationType.ONCE -> checkDurationType(R.id.chip_once)
                AddEditMedicinePlanViewModel.DurationType.PERIOD -> checkDurationType(R.id.chip_period)
                AddEditMedicinePlanViewModel.DurationType.CONTINUOUS -> checkDurationType(R.id.chip_continuous)
            }
        })
        viewModel.intakeDays.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                AddEditMedicinePlanViewModel.IntakeDays.EVERYDAY -> checkIntakeDays(R.id.chip_everyday)
                AddEditMedicinePlanViewModel.IntakeDays.DAYS_OF_WEEK -> checkIntakeDays(R.id.chip_days_of_week)
                AddEditMedicinePlanViewModel.IntakeDays.INTERVAL -> checkIntakeDays(R.id.chip_interval)
                AddEditMedicinePlanViewModel.IntakeDays.SEQUENCE -> checkIntakeDays(R.id.chip_sequence)
            }
        })
    }

    private fun setupDurationTypeChipGroup() {
        chip_group_duration_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_once -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.durationType.value = AddEditMedicinePlanViewModel.DurationType.ONCE
                }
                R.id.chip_period -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.durationType.value = AddEditMedicinePlanViewModel.DurationType.PERIOD
                }
                R.id.chip_continuous -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.durationType.value = AddEditMedicinePlanViewModel.DurationType.CONTINUOUS
                }
            }
        }
    }

    private fun setupIntakeDaysChipGroup() {
        chip_group_intake_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> {
                    viewModel.intakeDays.value = AddEditMedicinePlanViewModel.IntakeDays.EVERYDAY
                }
                R.id.chip_days_of_week -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDays.value = AddEditMedicinePlanViewModel.IntakeDays.DAYS_OF_WEEK
                }
                R.id.chip_interval -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDays.value = AddEditMedicinePlanViewModel.IntakeDays.INTERVAL
                }
                R.id.chip_sequence -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDays.value = AddEditMedicinePlanViewModel.IntakeDays.SEQUENCE
                }
            }
        }
    }

    private fun checkDurationType(itemId: Int) {
        if (chip_group_duration_type.checkedChipId != itemId) {
            chip_group_duration_type.check(itemId)
        }
    }

    private fun checkIntakeDays(itemId: Int) {
        if (chip_group_intake_days.checkedChipId != itemId) {
            chip_group_intake_days.check(itemId)
        }
    }

    private fun setupFab() {
        fab_save.shrinkOnScroll(scroll_view)
    }
}