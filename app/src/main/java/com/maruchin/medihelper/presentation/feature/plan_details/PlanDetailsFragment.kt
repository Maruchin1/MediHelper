package com.maruchin.medihelper.presentation.feature.plan_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentPlanDetailsBinding
import com.maruchin.medihelper.presentation.dialogs.ConfirmDialog
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import com.maruchin.medihelper.presentation.framework.BaseRecyclerLiveAdapter
import com.maruchin.medihelper.presentation.framework.BaseViewHolder
import com.maruchin.medihelper.presentation.framework.beginDelayedTransition
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_plan_details.*
import kotlinx.android.synthetic.main.rec_item_history_item.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanDetailsFragment : BaseMainFragment<FragmentPlanDetailsBinding>(R.layout.fragment_plan_details) {

    private val viewModel: PlanDetailsViewModel by viewModel()
    private val args: PlanDetailsFragmentArgs by navArgs()
    private val loadingScreen: LoadingScreen by inject()

    fun onClickEditPlan() {
        val direction = PlanDetailsFragmentDirections.toAddEditMedicinePlanFragment(
            profileId = null,
            medicineId = null,
            medicinePlanId = viewModel.medicinePlanId
        )
        findNavController().navigate(direction)
    }

    fun onClickDeletePlan() {
        ConfirmDialog(
            title = "Usuń plan",
            message = "Plan przyjmowania leku zostanie usunięty, wraz z odpowiadającymi mu wpisami w kalendarzu. Czy chcesz kontynuować?",
            iconResId = R.drawable.round_delete_black_36
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnConfirmClickListener {
                viewModel.deletePlan()
            }
        }.show(childFragmentManager)
    }

    fun onClickOpenMedicineDetails() {
        val direction = PlanDetailsFragmentDirections.toMedicineDetailsFragment(viewModel.medicineId)
        findNavController().navigate(direction)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        enableLightStatusBar()
        bindLoadingScreen()
        setupToolbarNavigation()
        setupToolbarMenu()
        setupTimeDoseRecyclerView()
        setupHistoryRecyclerView()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel.initViewModel(args.medicinePlanId)
    }

    private fun enableLightStatusBar() {
        super.setLightStatusBar(true)
    }

    private fun bindLoadingScreen() {
        loadingScreen.bind(this, viewModel.loadingInProgress)
    }

    private fun setupToolbarNavigation() {
        val navController = findNavController()
        toolbar.setupWithNavController(navController)
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btn_edit -> onClickEditPlan()
                R.id.btn_delete -> onClickDeletePlan()
            }
            true
        }
    }

    private fun setupTimeDoseRecyclerView() {
        recycler_view_time_dose.apply {
            adapter = TimeDoseAdapter()
        }
    }

    private fun setupHistoryRecyclerView() {
        recycler_view_history.apply {
            adapter = HistoryAdapter()
        }
    }

    private fun observeViewModel() {
        viewModel.todayHistoryPosition.observe(viewLifecycleOwner, Observer { position ->
           scrollToToday(position)
        })
        viewModel.actionDetailsLoaded.observe(viewLifecycleOwner, Observer {
            lay_details.beginDelayedTransition()
        })
        viewModel.actionHistoryLoaded.observe(viewLifecycleOwner, Observer {
            lay_history.beginDelayedTransition()
        })
        viewModel.actionPlanDeleted.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    private fun scrollToToday(position: Int) {
        var positionWithPastOffset = position - 3
        if (positionWithPastOffset < 0) {
            positionWithPastOffset = 0
        }
        recycler_view_history.scrollToPosition(positionWithPastOffset)
    }

    private inner class TimeDoseAdapter : BaseRecyclerLiveAdapter<TimeDoseData>(
        layoutResId = R.layout.rec_item_time_dose,
        itemsSource = viewModel.timesDoses,
        lifecycleOwner = viewLifecycleOwner
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: TimeDoseData) {
            holder.bind(displayData = item, handler = this@PlanDetailsFragment)
        }
    }

    private inner class HistoryAdapter : BaseRecyclerLiveAdapter<HistoryItemData>(
        layoutResId = R.layout.rec_item_history_item,
        itemsSource = viewModel.history,
        lifecycleOwner = viewLifecycleOwner,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.date == newItem.date }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: HistoryItemData) {
            holder.bind(displayData = item, handler = this@PlanDetailsFragment, viewModel = viewModel)
            holder.view.lay_history_checkboxes.apply {
                removeAllViews()
                item.checkBoxes.forEach { checkbox ->
                    val binding = DataBindingUtil.inflate<ViewDataBinding>(
                        layoutInflater,
                        R.layout.view_history_checkbox,
                        this,
                        true
                    )
                    binding.setVariable(BR.displayData, checkbox)
                }
            }
        }
    }
}