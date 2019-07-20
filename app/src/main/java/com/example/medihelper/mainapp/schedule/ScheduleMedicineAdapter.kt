package com.example.medihelper.mainapp.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import org.w3c.dom.Text

class ScheduleMedicineAdapter (private val context: Context, private val viewModel: ScheduleViewModel) : RecyclerView.Adapter<ScheduleMedicineAdapter.DayScheduleViewHolder>() {

    private val scheduledMedicinesList = ArrayList<ScheduledMedicine>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayScheduleViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_schedule_medicine, parent, false)
        return DayScheduleViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return scheduledMedicinesList.size
    }

    override fun onBindViewHolder(holder: DayScheduleViewHolder, position: Int) {
        val scheduleMedicine = scheduledMedicinesList[position]
        viewModel.findMedicineById(scheduleMedicine.medicineID)?.let { medicine ->
            medicine.medicineTypeID?.let { medicineTypeID ->
                viewModel.findMedicineTypeById(medicineTypeID)?.let { medicineType ->
                    holder.apply {
                        txvTime.text = scheduleMedicine.time
                        txvMedicineName.text = medicine.name
                        val doseString = "${scheduleMedicine.doseSize} ${medicineType.typeName}"
                        txvMedicneDose.text = doseString
                    }
                }
            }
        }
    }

    fun setScheduledMedicinesList(list: List<ScheduledMedicine>) {
        scheduledMedicinesList.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    class DayScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvTime: TextView = itemView.findViewById(R.id.txv_time)
        val txvMedicineName: TextView = itemView.findViewById(R.id.txv_medicine_name)
        val txvMedicneDose: TextView = itemView.findViewById(R.id.txv_medicine_dose)
    }
}