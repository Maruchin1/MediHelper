package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentScheduleListBinding
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import kotlinx.android.synthetic.main.fragment_schedule_list.*

class ScheduleListFragment : Fragment() {
    private val TAG = ScheduleListFragment::class.simpleName

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentScheduleListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recycler_view_scheduled_medicine.adapter = MedicinePlanAdapter()
        recycler_view_scheduled_medicine.layoutManager = LinearLayoutManager(context)
    }

    private fun observeViewModel() {
        viewModel.getMedicinePlaListLive().observe(viewLifecycleOwner, Observer { medicinePlanList ->
            val adapter = recycler_view_scheduled_medicine.adapter as MedicinePlanAdapter
            adapter.setItemsList(medicinePlanList)
        })
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    // Inner classes
    inner class MedicinePlanAdapter : RecyclerAdapter<MedicinePlanItem>(R.layout.recycler_item_medicine_plan) {

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicinePlan = itemsArrayList[position]
            val medicinePlanDisplayData = viewModel.getMedicinePlanDisplayData(medicinePlan)
            holder.bind(medicinePlanDisplayData, this@ScheduleListFragment)
        }
    }
}
