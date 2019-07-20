package com.example.medihelper.mainapp.medickit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.io.File

class KitAdapter(private val context: Context, private val kitFragment: KitFragment) :
    RecyclerView.Adapter<KitAdapter.KitViewHolder>() {

    private var medicinesList = ArrayList<Medicine>()
    private var medicineTypesList = ArrayList<MedicineType>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): KitViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.recycler_item_kit, viewGroup, false)
        return KitViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return medicinesList.size
    }

    override fun onBindViewHolder(viewHolder: KitViewHolder, position: Int) {
        val medicine = medicinesList[position]
        viewHolder.txvMedicineName.text = medicine.name

        val state = medicine.calcMedicineState()
        if (state != null) {
            val empty = 1 - state
            val stateString = "${medicine.currState}/${medicine.packageSize}"
            val medicineType = medicineTypesList.find { medicineType ->
                medicineType.medicineTypeID == medicine.medicineTypeID
            }
            viewHolder.apply {
                txvState.text = stateString
                txvType.text = medicineType?.typeName ?: "brak typu"
                setLayoutWeight(state, lineState)
                setLayoutWeight(empty, lineEmpty)
                lineState.setBackgroundResource(stateColorResId(state))
            }
        } else {
            viewHolder.apply {
                val gone = View.GONE
                layCurrStateLine.visibility = gone
                layCurrStateText.visibility = gone
            }
        }

        medicine.photoFilePath?.let { photoFilePath ->
            Glide.with(context)
                .load(File(photoFilePath))
                .centerCrop()
                .into(viewHolder.imgPhoto)
        }

        viewHolder.layClick.setOnClickListener {
            kitFragment.openMedicineDetailsFragment(medicine.medicineID!!)
        }
    }

    fun setMedicinesList(list: List<Medicine>) {
        medicinesList.clear()
        medicinesList.addAll(list)
        notifyDataSetChanged()
    }

    fun setMedicineTypesList(list: List<MedicineType>) {
        medicineTypesList.clear()
        medicineTypesList.addAll(list)
        notifyDataSetChanged()
    }

    private fun stateColorResId(state: Float): Int {
        val stateGoodLimit = KitViewModel.STATE_GOOD_LIMIT
        val stateMediumLimit = KitViewModel.STATE_MEDIUM_LIMIT
        return when {
            state >= stateGoodLimit -> R.color.colorStateGood
            state > stateMediumLimit -> R.color.colorStateMedium
            else -> R.color.colorStateSmall
        }
    }

    private fun setLayoutWeight(weight: Float, view: View) {
        val params = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            weight
        )
        view.layoutParams = params
    }

    class KitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvMedicineName: TextView = itemView.findViewById(R.id.txv_medicine_name)
        val lineState: View = itemView.findViewById(R.id.line_state)
        val lineEmpty: View = itemView.findViewById(R.id.line_empty)
        val txvState: TextView = itemView.findViewById(R.id.txv_state)
        val txvType: TextView = itemView.findViewById(R.id.txv_type)
        val layClick: FrameLayout = itemView.findViewById(R.id.lay_click)
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)
        val layCurrStateLine: LinearLayout = itemView.findViewById(R.id.lay_curr_state_line)
        val layCurrStateText: LinearLayout = itemView.findViewById(R.id.lay_curr_state_text)
    }
}