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
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentScheduleListBinding
import com.example.medihelper.localdatabase.entities.MedicinePlan
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import kotlinx.android.synthetic.main.recycler_item_scheduled_medicine.view.*

class ScheduleListFragment : Fragment() {
    private val TAG = ScheduleListFragment::class.simpleName

    private lateinit var viewModel: ScheduleViewModel

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
        viewModel.medicinePlanListLive.observe(viewLifecycleOwner, Observer { medicinePlanList ->
            val adapter = recycler_view_scheduled_medicine.adapter as MedicinePlanAdapter
            adapter.setMedicinePlanList(medicinePlanList)
        })
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    // Inner classes
    inner class MedicinePlanAdapter : RecyclerView.Adapter<MedicinePlanAdapter.MedicinePlanViewHolder>() {

        private val medicinePlanList = ArrayList<MedicinePlan>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinePlanViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_scheduled_medicine, parent, false)
            return MedicinePlanViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return medicinePlanList.size
        }

        override fun onBindViewHolder(holder: MedicinePlanViewHolder, position: Int) {
            val medicinePlan = medicinePlanList[position]
            val medicinePlanDisplayData = viewModel.getMedicinePlanDisplayData(medicinePlan)
            holder.view.run {
                txv_medicine_name.text = medicinePlanDisplayData.medicineName
                txv_duration_type.text = medicinePlanDisplayData.durationType
                txv_duration_dates.text = medicinePlanDisplayData.durationDates
                txv_days_type.text = medicinePlanDisplayData.daysType
                txv_time_of_taking.text = medicinePlanDisplayData.timeOfTaking

                btn_delete.setOnClickListener { openConfirmDeleteDialog(medicinePlan) }
            }
        }

        fun setMedicinePlanList(list: List<MedicinePlan>?) {
            medicinePlanList.clear()
            if (list != null) {
                medicinePlanList.addAll(list)
            }
            notifyDataSetChanged()
        }

        private fun openConfirmDeleteDialog(medicinePlan: MedicinePlan) {
            val dialog = ConfirmDialog().apply {
                title = "Usuń lek z planu"
                message = "Wybrany lek zostanie usunięty z planu. Czy chcesz kontynuować?"
                iconResId = R.drawable.round_delete_black_48
                setOnConfirmClickListener {
                    viewModel.deleteMedicinePlan(medicinePlan)
                }
            }
            dialog.show(childFragmentManager, ConfirmDialog.TAG)
        }

        inner class MedicinePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val view = itemView
        }
    }
}
