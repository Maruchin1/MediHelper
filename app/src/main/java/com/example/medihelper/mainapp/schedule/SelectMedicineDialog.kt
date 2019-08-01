package com.example.medihelper.mainapp.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_medicine.*
import java.io.File

class SelectMedicineDialog : BottomSheetDialogFragment() {

    private lateinit var viewModel: AddScheduledMedicineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddScheduledMedicineViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_select_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_close.setOnClickListener { dismiss() }
        setupRecyclerView()
        setupAddMedicineButton()
        observeViewModel()
    }

    private fun setSelectedMedicine(medicine: Medicine) {
        viewModel.selectedMedicineLive.value = medicine
        findNavController().run {
            if (currentDestination?.id == R.id.schedule_destination) {
                navigate(ScheduleFragmentDirections.actionScheduleDestinationToAddToScheduleDestination())
            }
        }
        dismiss()
    }

    private fun setupRecyclerView() {
        context?.let { context ->
            recycler_view_scheduled_medicine_for_day.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SelectMedicineAdapter()
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun setupAddMedicineButton() {
        btn_add_medicine.setOnClickListener {
            findNavController().run {
                val direction = when (currentDestination?.id) {
                    R.id.schedule_destination -> ScheduleFragmentDirections.actionScheduleDestinationToAddMedicineDestination(-1)
                    R.id.add_to_schedule_destination -> AddScheduledMedicineFragmentDirections.actionAddToScheduleDestinationToAddMedicineDestination(-1)
                    else -> null
                }
                direction?.let {
                    navigate(it)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.medicinesListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (recycler_view_scheduled_medicine_for_day.adapter as SelectMedicineAdapter).setMedicinesList(it)
            }
        })
        viewModel.medicinesTypesListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (recycler_view_scheduled_medicine_for_day.adapter as SelectMedicineAdapter).setMedicineTypesList(it)
            }
        })
    }

    companion object {
        val TAG = SelectMedicineDialog::class.simpleName
    }

    // Inner classes -------------------------------------------------------------------------------

    inner class SelectMedicineAdapter : RecyclerView.Adapter<SelectMedicineAdapter.SelectMedicineViewHolder>() {
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
                layClick.setOnClickListener { setSelectedMedicine(medicine) }
                context?.let {
                    Glide.with(it)
                        .load(File(medicine.photoFilePath))
                        .centerCrop()
                        .into(imgMedicinePicture)
                }
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

        inner class SelectMedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txvMedicineName: TextView = itemView.findViewById(R.id.txv_medicine_name)
            val txvMedicineState: TextView = itemView.findViewById(R.id.txv_medicine_state)
            val imgMedicinePicture: ImageView = itemView.findViewById(R.id.img_medicine_picture)
            val layClick: View = itemView.findViewById(R.id.lay_click)
        }
    }
}