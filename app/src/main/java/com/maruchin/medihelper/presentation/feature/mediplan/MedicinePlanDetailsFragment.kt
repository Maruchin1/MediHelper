package com.maruchin.medihelper.presentation.feature.mediplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicinePlanDetailsBinding
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.presentation.dialogs.ConfirmDialog
import com.maruchin.medihelper.presentation.framework.BaseFragment
import com.maruchin.medihelper.presentation.framework.BaseRecyclerAdapter
import com.maruchin.medihelper.presentation.framework.BaseViewHolder
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_medicine_plan_details.*
import kotlinx.android.synthetic.main.rec_item_history_item.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicinePlanDetailsFragment :
    BaseFragment<FragmentMedicinePlanDetailsBinding>(R.layout.fragment_medicine_plan_details) {

    private val viewModel: MedicinePlanDetailsViewModel by viewModel()
    private val args: MedicinePlanDetailsFragmentArgs by navArgs()
    private val directions by lazy { MedicinePlanDetailsFragmentDirections }
    private val loadingScreen: LoadingScreen by inject()

    fun onClickEditPlan() {
        findNavController().navigate(
            directions.toAddEditMedicinePlanFragment(
                profileId = null,
                medicineId = null,
                medicinePlanId = viewModel.medicinePlanId
            )
        )
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
        findNavController().navigate(directions.toMedicineDetailsFragment(viewModel.medicineId))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        super.setupToolbarNavigation()
        viewModel.setArgs(args)
        loadingScreen.bind(this, viewModel.loadingInProgress)

        setupTimeDoseRecyclerView()
        setupHistoryRecyclerView()
        setupToolbarMenu()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionDataLoaded.observe(viewLifecycleOwner, Observer {
            startPostponedEnterTransition()
            super.setLightStatusBar(true)
        })
        viewModel.actionPlanDeleted.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
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

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btn_edit -> onClickEditPlan()
                R.id.btn_delete -> onClickDeletePlan()
            }
            true
        }
    }

    private inner class TimeDoseAdapter : BaseRecyclerAdapter<TimeDose>(
        layoutResId = R.layout.rec_item_time_dose,
        itemsSource = viewModel.timesDoses,
        lifecycleOwner = viewLifecycleOwner
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(displayData = item, handler = this@MedicinePlanDetailsFragment, viewModel = viewModel)
        }
    }

    private inner class HistoryAdapter : BaseRecyclerAdapter<HistoryItem>(
        layoutResId = R.layout.rec_item_history_item,
        itemsSource = viewModel.historyItems,
        lifecycleOwner = viewLifecycleOwner,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.date == newItem.date }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(displayData = item.date, handler = this@MedicinePlanDetailsFragment)
            holder.view.lay_history_checkboxes.apply {
                removeAllViews()
                item.checkboxesList.forEach { checkbox  ->
                    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.view_history_checkbox, this, true)
                    binding.setVariable(BR.displayData, checkbox)
                }
            }
        }
    }
}