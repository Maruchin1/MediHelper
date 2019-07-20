package com.example.medihelper.mainapp.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.MedicineType
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText

class DoseHourAdapter(
    private val context: Context,
    private val viewModel: AddToScheduleViewModel
) : RecyclerView.Adapter<DoseHourAdapter.DoseHourViewHolder>() {

    private var doseHoursList = ArrayList<DoseHour>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoseHourViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.recycler_item_dose_hour, parent, false)
        return DoseHourViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return doseHoursList.size
    }

    override fun onBindViewHolder(holder: DoseHourViewHolder, position: Int) {
        val doseHour = doseHoursList[position]
        holder.chipHour.text = doseHour.hour
        viewModel.selectedMedicineLive.value?.let { medicine ->
            medicine.medicineTypeID?.let { medicineTypeID ->
                holder.txvMedicineType.text = viewModel.findMedicineTypeName(medicineTypeID)
            }
        }
    }

    fun setDoseHoursList(list: List<DoseHour>) {
        doseHoursList.clear()
        doseHoursList.addAll(list)
        notifyDataSetChanged()
    }

    class DoseHourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chipHour: Chip = itemView.findViewById(R.id.chip_hour)
        val etxNumber: TextInputEditText = itemView.findViewById(R.id.etx_number)
        val txvMedicineType: TextView = itemView.findViewById(R.id.txv_medicine_type)
    }
}