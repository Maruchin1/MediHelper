package com.maruchin.medihelper.presentation.feature.calendar


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentCalendarDayBinding
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.presentation.framework.BaseFragment
import kotlinx.android.synthetic.main.fragment_calendar_day.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CalendarDayFragment : BaseFragment<FragmentCalendarDayBinding>(R.layout.fragment_calendar_day) {
    private val TAG = CalendarDayFragment::class.simpleName

    lateinit var date: AppDate

    private val viewModel: CalendarDayViewModel by viewModel()
    private val directions by lazyOf(CalendarFragmentDirections)

    fun onClickOpenPlannedMedicineOptions(plannedMedicineId: String) {

//        val parentFrag = parentFragment as CalendarFragment
//        parentFrag.findNavController().navigate(directions.toPlannedMedicineOptionsDialog(plannedMedicineID))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initViewModel(calendarDayDate = date)
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        recycler_view_morning_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.morningPlannedMedicines)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_afternoon_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.afternoonPlannedMedicines)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_evening_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.eveningPlannedMedicines)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    // Inner classes
    private inner class PlannedMedicineAdapter(itemsSource: LiveData<List<PlannedMedicineItem>>) :
        RecyclerAdapter<PlannedMedicineItem>(
            layoutResId = R.layout.rec_item_planned_medicine,
            lifecycleOwner = viewLifecycleOwner,
            itemsSource = itemsSource,
            areItemsTheSameFun = { oldItem, newItem -> oldItem.plannedMedicineId == newItem.plannedMedicineId }
        ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val plannedMedicine = itemsList[position]
            holder.bind(plannedMedicine, this@CalendarDayFragment)
        }
    }
}


