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
import com.maruchin.medihelper.presentation.model.PlannedMedicineItemData
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupRecyclerViews()
        observeViewModel()
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun initViewModel() {
        viewModel.initViewModel(date)
    }

    private fun setupRecyclerViews() {
        recycler_view_morning_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.morningItems)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_afternoon_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.afternoonItems)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_evening_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.eveningItems)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }


    private fun observeViewModel() {
        viewModel.dataLoaded.observe(viewLifecycleOwner, Observer {
            onDataLoaded()
        })
    }

    private fun onDataLoaded() {
        root_lay.beginDelayedFade()
    }

    // Inner classes
    private inner class PlannedMedicineAdapter(itemsSource: LiveData<List<PlannedMedicineItemData>>) :
        BaseRecyclerAdapter<PlannedMedicineItemData>(
            layoutResId = R.layout.rec_item_planned_medicine,
            lifecycleOwner = viewLifecycleOwner,
            itemsSource = itemsSource,
            areItemsTheSameFun = { oldItem, newItem -> oldItem.plannedMedicineId == newItem.plannedMedicineId }
        ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val plannedMedicine = itemsList[position]
            holder.bind(plannedMedicine, this@CalendarDayFragment)
        }
    }
}


