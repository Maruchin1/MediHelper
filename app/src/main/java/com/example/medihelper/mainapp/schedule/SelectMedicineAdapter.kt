package com.example.medihelper.mainapp.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType


class SelectMedicineAdapter(
    private val context: Context,
    private val onClickMethod: (medicine: Medicine) -> Unit
) : RecyclerView.Adapter<SelectMedicineAdapter.SelectMedicineViewHolder>() {
    private val TAG = SelectMedicineAdapter::class.simpleName

    private var medicinesList = ArrayList<Medicine>()
    private var medicinesTypesList = ArrayList<MedicineType>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectMedicineViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.recycler_item_select_medicine, parent, false)
        return SelectMedicineViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return medicinesList.size
    }

    override fun onBindViewHolder(
        holder: SelectMedicineViewHolder,
        position: Int
    ) {
        val medicine = medicinesList[position]
        val medicineType = medicinesTypesList.find {
            it.medicineTypeID == medicine.medicineTypeID
        }
        val medicineTypeName = medicineType?.typeName ?: "brak typu"
        val stateFullString = "${medicine.currState}/${medicine.packageSize} $medicineTypeName"

        holder.apply {
            txvMedicineName.text = medicine.name
            txvMedicineState.text = stateFullString
            layClick.setOnClickListener { onClickMethod(medicine) }
        }
    }

    fun setMedicinesList(list: List<Medicine>) {
        medicinesList.clear()
        medicinesList.addAll(list)
        notifyDataSetChanged()
    }

    fun setMedicineTypesList(list: List<MedicineType>) {
        medicinesTypesList.clear()
        medicinesTypesList.addAll(list)
        notifyDataSetChanged()
    }

    class SelectMedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvMedicineName: TextView = itemView.findViewById(R.id.txv_medicine_name)
        val txvMedicineState: TextView = itemView.findViewById(R.id.txv_medicine_state)
        val layClick: LinearLayout = itemView.findViewById(R.id.lay_click)
    }
}