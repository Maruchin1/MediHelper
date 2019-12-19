package com.maruchin.medihelper.presentation.feature.mediplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditMedicinePlanBinding
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.presentation.dialogs.SelectDateDialog
import com.maruchin.medihelper.presentation.dialogs.SelectFloatNumberDialog
import com.maruchin.medihelper.presentation.dialogs.SelectNumberDialog
import com.maruchin.medihelper.presentation.dialogs.SelectTimeDialog
import com.maruchin.medihelper.presentation.framework.BaseFragment
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import com.maruchin.medihelper.presentation.framework.shrinkOnScroll
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_add_edit_medicine_plan.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEditMedicinePlanFragment :
    BaseFragment<FragmentAddEditMedicinePlanBinding>(R.layout.fragment_add_edit_medicine_plan) {

    private val viewModel: AddEditMedicinePlanViewModel by viewModel()
    private val args: AddEditMedicinePlanFragmentArgs by navArgs()
    private val loadingScreen: LoadingScreen by inject()

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

    fun onClickSelectInterval() {
        SelectNumberDialog().apply {
            defaultNumber = viewModel.interval.value?.daysCount
            title = "Odstęp dni"
            setNumberSelectedListener { number ->
                viewModel.setInterval(number)
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectIntakeDays() {
        SelectNumberDialog().apply {
            defaultNumber = viewModel.sequence.value?.intakeCount
            title = "Dni przyjmowania"
            setNumberSelectedListener { number ->
                viewModel.setIntakeDays(number)
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectNoIntakeDays() {
        SelectNumberDialog().apply {
            defaultNumber = viewModel.sequence.value?.notIntakeCount
            title = "Dni przerwy"
            setNumberSelectedListener { number ->
                viewModel.setNoIntakeDays(number)
            }
        }.show(childFragmentManager)
    }

    fun onClickAddTimeDose() {
        TransitionManager.beginDelayedTransition(root_lay)
        viewModel.addTimeDose()
    }

    fun onClickSelectTime(timeDoseFormItem: AddEditMedicinePlanViewModel.TimeDoseFormItem) {
        SelectTimeDialog().apply {
            defaultTime = timeDoseFormItem.time
            setTimeSelectedListener { time ->
                viewModel.updateTimeDose(timeDoseFormItem.copy(time = time))
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectDoseSize(timeDoseFormItem: AddEditMedicinePlanViewModel.TimeDoseFormItem) {
        SelectFloatNumberDialog().apply {
            title = "Wybierz dawkę leku"
            iconResID = R.drawable.ic_pill_black_36dp
            defaultNumber = timeDoseFormItem.doseSize
            setNumberSelectedListener { number ->
                viewModel.updateTimeDose(timeDoseFormItem.copy(doseSize = number))
            }
        }.show(childFragmentManager)
    }

    fun onClickDeleteTimeDose(position: Int) {
        TransitionManager.beginDelayedTransition(root_lay)
        viewModel.deleteTimeDose(position)
    }

    fun onClickSaveMedicinePlan() {
        viewModel.saveMedicinePlan()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        super.setStatusBarColorLive(viewModel.colorPrimary)
        viewModel.setArgs(args)
        setupFab()
        setupDurationTypeChipGroup()
        setupIntakeDaysChipGroup()
        setupTimeDoseRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.planType.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                MedicinePlan.Type.ONCE -> checkDurationType(R.id.chip_once)
                MedicinePlan.Type.PERIOD -> checkDurationType(R.id.chip_period)
                MedicinePlan.Type.CONTINUOUS -> checkDurationType(R.id.chip_continuous)
            }
        })
        viewModel.intakeDaysType.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                AddEditMedicinePlanViewModel.IntakeDaysType.EVERYDAY -> checkIntakeDays(R.id.chip_everyday)
                AddEditMedicinePlanViewModel.IntakeDaysType.DAYS_OF_WEEK -> checkIntakeDays(R.id.chip_days_of_week)
                AddEditMedicinePlanViewModel.IntakeDaysType.INTERVAL -> checkIntakeDays(R.id.chip_interval)
                AddEditMedicinePlanViewModel.IntakeDaysType.SEQUENCE -> checkIntakeDays(R.id.chip_sequence)
            }
        })
        viewModel.actionDataLoaded.observe(viewLifecycleOwner, Observer {
            super.setLightStatusBar(false)
            super.setupToolbarNavigation()
            startPostponedEnterTransition()
        })
        viewModel.loadingInProgress.observe(viewLifecycleOwner, Observer { loadingInProgress ->
            if (loadingInProgress) {
                loadingScreen.showLoadingScreen(childFragmentManager)
            } else {
                loadingScreen.closeLoadingScreen()
            }
        })
        viewModel.actionMedicinePlanSaved.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    private fun setupDurationTypeChipGroup() {
        chip_group_duration_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_once -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.planType.value = MedicinePlan.Type.ONCE
                }
                R.id.chip_period -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.planType.value = MedicinePlan.Type.PERIOD
                }
                R.id.chip_continuous -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.planType.value = MedicinePlan.Type.CONTINUOUS
                }
            }
        }
    }

    private fun setupIntakeDaysChipGroup() {
        chip_group_intake_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = AddEditMedicinePlanViewModel.IntakeDaysType.EVERYDAY
                }
                R.id.chip_days_of_week -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = AddEditMedicinePlanViewModel.IntakeDaysType.DAYS_OF_WEEK
                }
                R.id.chip_interval -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = AddEditMedicinePlanViewModel.IntakeDaysType.INTERVAL
                }
                R.id.chip_sequence -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = AddEditMedicinePlanViewModel.IntakeDaysType.SEQUENCE
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

    private fun setupTimeDoseRecyclerView() {
        recycler_view_time_dose.apply {
            adapter = TimeDoseAdapter()
        }
    }

    private inner class TimeDoseAdapter : RecyclerAdapter<TimeDose>(
        layoutResId = R.layout.rec_item_time_dose_edit,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.timeDoseList
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val item = itemsList[position]
            val formItem = AddEditMedicinePlanViewModel.TimeDoseFormItem(item, position)

            holder.bind(
                displayData = formItem,
                handler = this@AddEditMedicinePlanFragment,
                viewModel = viewModel
            )
        }
    }
}