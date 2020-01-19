package com.maruchin.medihelper.presentation.feature.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentCalendarDayBinding
import com.maruchin.medihelper.presentation.feature.planned_medicine_options.PlannedMedicineOptionsDialog
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.fragment_calendar_day.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarDayFragment : BaseMainFragment<FragmentCalendarDayBinding>(R.layout.fragment_calendar_day) {
    private val TAG = CalendarDayFragment::class.simpleName

    lateinit var date: AppDate

    private val viewModel: CalendarDayViewModel by viewModel()

    fun onClickOpenPlannedMedicineOptions(plannedMedicineId: String) {
        PlannedMedicineOptionsDialog()
            .apply {
                this.plannedMedicineId = plannedMedicineId
            }.show(childFragmentManager)
    }

    fun onClickChangeMorningCollapsed() {
        root_lay.beginDelayedTransition()
        viewModel.morningSection.changeSectionCollapsed()
    }

    fun onClickChangeAfternoonCollapsed() {
        root_lay.beginDelayedTransition()
        viewModel.afternoonSection.changeSectionCollapsed()
    }

    fun onClickChangeEveningCollapsed() {
        root_lay.beginDelayedTransition()
        viewModel.eveningSection.changeSectionCollapsed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupCheckBoxesRecyclerViews()
        setupItemsRecyclerViews()
        observeViewModel()
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun initViewModel() {
        viewModel.initViewModel(date)
    }

    private fun setupCheckBoxesRecyclerViews() {
        recycler_view_morning_checkboxes.apply {
            adapter = PlannedMedicineChackBoxAdapter(viewModel.morningSection.itemsCheckBoxes)
        }
        recycler_view_afternoon_checkboxes.apply {
            adapter = PlannedMedicineChackBoxAdapter(viewModel.afternoonSection.itemsCheckBoxes)
        }
        recycler_view_evening_checkboxes.apply {
            adapter = PlannedMedicineChackBoxAdapter(viewModel.eveningSection.itemsCheckBoxes)
        }
    }

    private fun setupItemsRecyclerViews() {
        recycler_view_morning_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.morningSection.items)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_afternoon_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.afternoonSection.items)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_evening_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.eveningSection.items)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }


    private fun observeViewModel() {
        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            root_lay.beginDelayedFade()
        })
    }

    private inner class PlannedMedicineAdapter(
        itemsSource: LiveData<List<PlannedMedicineItemData>>
    ) : BaseRecyclerLiveAdapter<PlannedMedicineItemData>(
        layoutResId = R.layout.rec_item_planned_medicine,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = itemsSource
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: PlannedMedicineItemData) {
            holder.bind(item, this@CalendarDayFragment)
        }
    }

    private inner class PlannedMedicineChackBoxAdapter(
        itemsSource: LiveData<List<PlannedMedicineItemCheckBoxData>>
    ) : BaseRecyclerLiveAdapter<PlannedMedicineItemCheckBoxData>(
        layoutResId = R.layout.rec_item_planned_medicine_checkbox,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = itemsSource
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: PlannedMedicineItemCheckBoxData) {
            holder.bind(item, this@CalendarDayFragment)
        }
    }
}


