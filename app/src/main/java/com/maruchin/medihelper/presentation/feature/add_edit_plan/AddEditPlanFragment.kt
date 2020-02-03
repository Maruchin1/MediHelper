package com.maruchin.medihelper.presentation.feature.add_edit_plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditPlanBinding
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.presentation.dialogs.SelectDateDialog
import com.maruchin.medihelper.presentation.dialogs.SelectFloatNumberDialog
import com.maruchin.medihelper.presentation.dialogs.SelectNumberDialog
import com.maruchin.medihelper.presentation.dialogs.SelectTimeDialog
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import com.maruchin.medihelper.presentation.framework.BaseRecyclerLiveAdapter
import com.maruchin.medihelper.presentation.framework.BaseViewHolder
import com.maruchin.medihelper.presentation.framework.shrinkOnScroll
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_add_edit_plan.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEditPlanFragment :
    BaseMainFragment<FragmentAddEditPlanBinding>(R.layout.fragment_add_edit_plan) {

    private val viewModel: AddEditPlanViewModel by viewModel()
    private val args: AddEditPlanFragmentArgs by navArgs()
    private val loadingScreen: LoadingScreen by inject()

    fun onClickSelectStartDate() {
        SelectDateDialog(
            defaultDate = viewModel.startDate.value
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnDateSelectedListener { date ->
                viewModel.startDate.value = date
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectEndDate() {
        SelectDateDialog(
            defaultDate = viewModel.endDate.value
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnDateSelectedListener { date ->
                viewModel.endDate.value = date
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectInterval() {
        SelectNumberDialog(
            title = "Odstęp dni",
            iconResId = R.drawable.round_event_white_36,
            defaultNumber = viewModel.interval.value?.daysCount
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnNumberSelectedListener { number ->
                viewModel.setInterval(number)
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectIntakeDays() {
        SelectNumberDialog(
            title = "Dni przyjmowania",
            iconResId = R.drawable.round_event_white_36,
            defaultNumber = viewModel.sequence.value?.intakeCount
        ).apply {
            setColorPrimary(viewModel.profile.value?.color)
            setOnNumberSelectedListener { number ->
                viewModel.setIntakeDays(number)
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectNoIntakeDays() {
        SelectNumberDialog(
            title = "Dni przerwy",
            iconResId = R.drawable.round_event_white_36,
            defaultNumber = viewModel.sequence.value?.notIntakeCount
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnNumberSelectedListener { number ->
                viewModel.setNoIntakeDays(number)
            }
        }.show(childFragmentManager)
    }

    fun onClickAddTimeDose() {
        TransitionManager.beginDelayedTransition(root_lay)
        viewModel.addTimeDose()
    }

    fun onClickSelectTime(timeDoseEditData: TimeDoseEditData) {
        SelectTimeDialog(
            defaultTime = timeDoseEditData.time
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnTimeSelectedListener { time ->
                viewModel.updateTimeDose(timeDoseEditData.copy(time = time))
            }
        }.show(childFragmentManager)
    }

    fun onClickSelectDoseSize(timeDoseEditData: TimeDoseEditData) {
        SelectFloatNumberDialog(
            title = "Wybierz dawkę leku",
            iconResId = R.drawable.ic_pill_black_36dp,
            defaultNumber = timeDoseEditData.doseSize
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnNumberSelectedListener { number ->
                viewModel.updateTimeDose(timeDoseEditData.copy(doseSize = number))
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
        initViewModel()
        bindLoadingScreen()
        setupFab()
        setupDurationTypeSelection()
        setupIntakeDaysSelection()
        setupTimeDoseRecyclerView()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel.initViewModel(
            medicinePlanId = args.medicinePlanId,
            profileId = args.profileId,
            medicineId = args.medicineId
        )
    }

    private fun bindLoadingScreen() {
        loadingScreen.bind(this, viewModel.loadingInProgress)
    }

    private fun setupFab() {
        fab_save.shrinkOnScroll(scroll_view)
    }

    private fun setupDurationTypeSelection() {
        chip_group_duration_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_once -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.planType.value = Plan.Type.ONE_DAY
                }
                R.id.chip_period -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.planType.value = Plan.Type.PERIOD
                }
                R.id.chip_continuous -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.planType.value = Plan.Type.CONTINUOUS
                }
            }
        }
    }

    private fun setupIntakeDaysSelection() {
        chip_group_intake_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = IntakeDaysType.EVERYDAY
                }
                R.id.chip_days_of_week -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = IntakeDaysType.DAYS_OF_WEEK
                }
                R.id.chip_interval -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = IntakeDaysType.INTERVAL
                }
                R.id.chip_sequence -> {
                    TransitionManager.beginDelayedTransition(root_lay)
                    viewModel.intakeDaysType.value = IntakeDaysType.SEQUENCE
                }
            }
        }
    }

    private fun setupTimeDoseRecyclerView() {
        recycler_view_time_dose.apply {
            adapter = TimeDoseAdapter()
        }
    }

    private fun observeViewModel() {
        viewModel.planType.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                Plan.Type.ONE_DAY -> checkDurationType(R.id.chip_once)
                Plan.Type.PERIOD -> checkDurationType(R.id.chip_period)
                Plan.Type.CONTINUOUS -> checkDurationType(R.id.chip_continuous)
            }
        })
        viewModel.intakeDaysType.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                IntakeDaysType.EVERYDAY -> checkIntakeDays(R.id.chip_everyday)
                IntakeDaysType.DAYS_OF_WEEK -> checkIntakeDays(R.id.chip_days_of_week)
                IntakeDaysType.INTERVAL -> checkIntakeDays(R.id.chip_interval)
                IntakeDaysType.SEQUENCE -> checkIntakeDays(R.id.chip_sequence)
            }
        })
        viewModel.actionDataLoaded.observe(viewLifecycleOwner, Observer {
            super.setLightStatusBar(false)
            super.setStatusBarColorLive(viewModel.colorPrimary)
            toolbar.setupWithNavController(findNavController())
            startPostponedEnterTransition()
        })
        viewModel.actionMedicinePlanSaved.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
        viewModel.errorGlobal.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                showSnackbar(root_lay, error)
            }
        })
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

    private inner class TimeDoseAdapter : BaseRecyclerLiveAdapter<TimeDose>(
        layoutResId = R.layout.rec_item_time_dose_edit,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.timeDoseList
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: TimeDose) {
            val formItem = TimeDoseEditData.fromDomainModel(item, position)
            holder.bind(
                displayData = formItem,
                handler = this@AddEditPlanFragment,
                viewModel = viewModel
            )
        }
    }
}