package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import kotlinx.android.synthetic.main.recycler_item_scheduled_medicine_for_day.view.*
import java.util.*
import kotlin.collections.ArrayList


class ScheduleDayFragment : Fragment() {
    private val TAG = ScheduleDayFragment::class.simpleName

    var date: Date? = null
    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        date?.let { dayDate ->
            viewModel.getMedicinePlannedForDateListLive(dayDate)
                .observe(viewLifecycleOwner, Observer { medicinePlannedForDateList ->
                    Log.d(TAG, "date = $date, scheduledMedicinesList change = $medicinePlannedForDateList")
                    val adapter = recycler_view_scheduled_medicine_for_day.adapter as MedicinePlannedForDateAdapter
                    adapter.setMedicinePlannedForDateList(medicinePlannedForDateList)
                })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_scheduled_medicine_for_day.adapter = MedicinePlannedForDateAdapter()
        recycler_view_scheduled_medicine_for_day.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class MedicinePlannedForDateAdapter :
        RecyclerView.Adapter<MedicinePlannedForDateAdapter.MedicinePlannedForDateViewHolder>() {

        private val medicinePlannedForDateArrayList = ArrayList<PlannedMedicine>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinePlannedForDateViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_scheduled_medicine_for_day, parent, false)
            return MedicinePlannedForDateViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return medicinePlannedForDateArrayList.size
        }

        override fun onBindViewHolder(holder: MedicinePlannedForDateViewHolder, position: Int) {
            val medicinePlannedForDate = medicinePlannedForDateArrayList[position]
            val medicinePlannedForDateDisplayData = viewModel.getMedicinePlannedForDateDisplayData(medicinePlannedForDate)
            holder.view.run {
                txv_scheduled_time.text = medicinePlannedForDateDisplayData.time
                txv_medicine_name.text = medicinePlannedForDateDisplayData.medicineName
                txv_scheduled_dose.text = medicinePlannedForDateDisplayData.doseSize
                txv_schedule_status.text = medicinePlannedForDateDisplayData.statusOfTaking
                lay_header.setBackgroundColor(resources.getColor(medicinePlannedForDateDisplayData.statusOfTakingColorId))
            }
        }

        fun setMedicinePlannedForDateList(list: List<PlannedMedicine>?) {
            medicinePlannedForDateArrayList.clear()
            if (list != null) {
                medicinePlannedForDateArrayList.addAll(list)
            }
            notifyDataSetChanged()
        }

        inner class MedicinePlannedForDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var view = itemView
        }
    }
}
