package com.maruchin.medihelper.presentation.feature.calendar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import com.maruchin.medihelper.databinding.FragmentScheduleDayBinding
import com.maruchin.medihelper.presentation.framework.bind
import com.maruchin.medihelper.presentation.model.PlannedMedicineItem
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CalendarDayFragment : Fragment() {
    private val TAG = CalendarDayFragment::class.simpleName

    var date: AppDate? = null

    private val viewModel: CalendarDayViewModel by viewModel()
    private val directions by lazyOf(CalendarFragmentDirections)

    fun onClickOpenPlannedMedicineOptions(plannedMedicineID: Int) {
        val parentFrag = parentFragment as CalendarFragment
        parentFrag.findNavController().navigate(directions.toPlannedMedicineOptionsDialog(plannedMedicineID))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentScheduleDayBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_schedule_day,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.date.value = date
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        recycler_view_morning_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.morningPlannedMedicineItemList)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_afternoon_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.afternoonPlannedMedicineItemList)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        recycler_view_evening_schedule.apply {
            adapter = PlannedMedicineAdapter(viewModel.eveningPlannedMedicineItemList)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    // Inner classes
    inner class PlannedMedicineAdapter(itemsSource: LiveData<List<PlannedMedicineItem>>) : RecyclerAdapter<PlannedMedicineItem>(
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


