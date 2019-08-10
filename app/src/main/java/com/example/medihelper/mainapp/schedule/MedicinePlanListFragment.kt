package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentMedicinePlanListBinding
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import kotlinx.android.synthetic.main.fragment_medicine_plan_list.*


class MedicinePlanListFragment : Fragment() {

    var medicinePlanType: ScheduleViewModel.MedicinePlanType? = null
    private lateinit var viewModel: ScheduleViewModel

    fun onClickDeleteMedicinePlan(medicinePlanID: Int) {
        val dialog = ConfirmDialog().apply {
            title = "Usuń lek z planu"
            message = "Wybrany lek zostanie usunięty z planu. Czy chcesz kontynuować?"
            iconResId = R.drawable.round_delete_black_48
            setOnConfirmClickListener {
                viewModel.deleteMedicinePlan(medicinePlanID)
            }
        }
        dialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMedicinePlanListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_plan_list, container, false)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        medicinePlanType?.let {
            viewModel.getMedicinePlanItemListLive(it).observe(viewLifecycleOwner, Observer { medicinePlanList ->
                val adapter = recycler_view_medicine_plan.adapter as MedicinePlanAdapter
                adapter.setItemsList(medicinePlanList)
            })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_medicine_plan.adapter = MedicinePlanAdapter()
        recycler_view_medicine_plan.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class MedicinePlanAdapter : RecyclerAdapter<MedicinePlanItem>(R.layout.recycler_item_medicine_plan) {

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicinePlanItem = itemsArrayList[position]
            val medicinePlanDisplayData = viewModel.getMedicinePlanDisplayData(medicinePlanItem)
            holder.bind(medicinePlanDisplayData, this@MedicinePlanListFragment)
        }
    }
}
