package com.example.medihelper.mainapp.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.ScheduledMedicine

class DayScheduleAdapter (private val context: Context) : RecyclerView.Adapter<DayScheduleAdapter.DayScheduleViewHolder>() {

   private val scheduledMedicinesList = ArrayList<ScheduledMedicine>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayScheduleViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_schedule, parent, false)
        return DayScheduleViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return scheduledMedicinesList.size
    }

    override fun onBindViewHolder(holder: DayScheduleViewHolder, position: Int) {

    }

    fun setScheduledMedicinesList(list: List<ScheduledMedicine>) {
        scheduledMedicinesList.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    class DayScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}