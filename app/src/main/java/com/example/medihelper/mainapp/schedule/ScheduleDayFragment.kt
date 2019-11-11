package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.medihelper.localdatabase.AppDate
import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentScheduleDayBinding
import com.example.medihelper.localdatabase.pojo.PlannedMedicineItem
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ScheduleDayFragment : Fragment() {
    private val TAG = ScheduleDayFragment::class.simpleName

    var date: AppDate? = null

    private val viewModel: ScheduleDayViewModel by viewModel()
    private val directions by lazyOf(ScheduleFragmentDirections)

    fun onClickOpenPlannedMedicineOptions(plannedMedicineID: Int) {
        val parentFrag = parentFragment as ScheduleFragment
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
        viewModel.dateLive.value = date
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.morningPlannedMedicineItemListLive.observe(viewLifecycleOwner, Observer {
            (recycler_view_morning_schedule.adapter as PlannedMedicineAdapter).updateItemsList(it)
        })
        viewModel.afternoonPlannedMedicineItemListLive.observe(viewLifecycleOwner, Observer {
            (recycler_view_afternoon_schedule.adapter as PlannedMedicineAdapter).updateItemsList(it)
        })
        viewModel.eveningPlannedMedicineItemListLive.observe(viewLifecycleOwner, Observer {
            (recycler_view_evening_schedule.adapter as PlannedMedicineAdapter).updateItemsList(it)
        })
    }

    private fun setupRecyclerView() = listOf(
        recycler_view_morning_schedule,
        recycler_view_afternoon_schedule,
        recycler_view_evening_schedule
    ).forEach {
        it.adapter = PlannedMedicineAdapter()
        it.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    // Inner classes
    inner class PlannedMedicineAdapter : RecyclerAdapter<PlannedMedicineItem>(
        layoutResId = R.layout.recycler_item_planned_medicine,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.plannedMedicineID == newItem.plannedMedicineID }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val plannedMedicine = itemsList[position]
            holder.bind(plannedMedicine, this@ScheduleDayFragment)
        }
    }
}


