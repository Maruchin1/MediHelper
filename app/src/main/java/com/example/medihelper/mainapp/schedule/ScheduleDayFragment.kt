package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentScheduleDayBinding
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import java.util.*
class ScheduleDayFragment : Fragment() {
    private val TAG = ScheduleDayFragment::class.simpleName

    var date: Date? = null
    val plannedMedicinesAvailableLive = MutableLiveData(false)
    private val viewModel: ScheduleViewModel by activityViewModels()
    private val directions by lazyOf(ScheduleFragmentDirections)

    fun onClickOpenPlannedMedicineOptions(plannedMedicineID: Int) {
        val parentFrag = parentFragment as ScheduleFragment
        parentFrag.findNavController().navigate(directions.toPlannedMedicineOptionsDialog(plannedMedicineID))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentScheduleDayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_day, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        date?.let { dayDate ->
            viewModel.getPlannedMedicineItemListByDateLive(dayDate).observe(viewLifecycleOwner, Observer { plannedMedicineList ->
                Log.d(TAG, "date = $date, scheduledMedicinesList change = $plannedMedicineList")
                val adapter = recycler_view_scheduled_medicine_for_day.adapter as PlannedMedicineAdapter
                adapter.updateItemsList(plannedMedicineList)
                plannedMedicinesAvailableLive.value = !plannedMedicineList.isNullOrEmpty()
            })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_scheduled_medicine_for_day.adapter = PlannedMedicineAdapter()
    }

    // Inner classes
    inner class PlannedMedicineAdapter : RecyclerAdapter<PlannedMedicineItem>(
        R.layout.recycler_item_planned_medicine,
        object : DiffCallback<PlannedMedicineItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].plannedMedicineID == newList[newItemPosition].plannedMedicineID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val plannedMedicine = itemsList[position]
            holder.bind(plannedMedicine, this@ScheduleDayFragment)
        }
    }
}


