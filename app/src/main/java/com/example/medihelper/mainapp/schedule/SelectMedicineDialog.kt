package com.example.medihelper.mainapp.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.RecyclerItemSelectMedicineBinding
import com.example.medihelper.localdatabase.entities.Medicine
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_medicine.*
import kotlinx.android.synthetic.main.recycler_item_select_medicine.view.*
import java.io.File

class SelectMedicineDialog : BottomSheetDialogFragment() {

    private lateinit var viewModel: AddMedicinePlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddMedicinePlanViewModel::class.java)
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
            recycler_view_medicines.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = MedicineAdapter()
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun setupAddMedicineButton() {
        btn_add_medicine.setOnClickListener {
            findNavController().run {
                val direction = when (currentDestination?.id) {
                    R.id.schedule_destination -> ScheduleFragmentDirections.actionScheduleDestinationToAddMedicineDestination(-1)
                    R.id.add_to_schedule_destination -> AddMedicinePlanFragmentDirections.actionAddToScheduleDestinationToAddMedicineDestination(-1)
                    else -> null
                }
                direction?.let {
                    navigate(it)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.medicineListLive.observe(viewLifecycleOwner, Observer { medicineList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.setMedicinesList(medicineList)
        })
        viewModel.medicineTypeListLive.observe(viewLifecycleOwner, Observer {  })
    }

    companion object {
        val TAG = SelectMedicineDialog::class.simpleName
    }

    // Inner classes -------------------------------------------------------------------------------
    inner class MedicineAdapter : RecyclerView.Adapter<RecyclerItemViewHolder>() {
        private var medicinesList = ArrayList<Medicine>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
            val binding: RecyclerItemSelectMedicineBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.recycler_item_select_medicine,
                parent,
                false
            )
            return RecyclerItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return medicinesList.size
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicine = medicinesList[position]
            val medicineDisplayData = viewModel.getMedicineDisplayData(medicine)
            holder.bind(medicineDisplayData)
            holder.view.lay_click.setOnClickListener { setSelectedMedicine(medicine) }
            context?.let {
                Glide.with(it)
                    .load(File(medicine.photoFilePath))
                    .centerCrop()
                    .into(holder.view.img_medicine_picture)
            }
        }

        fun setMedicinesList(list: List<Medicine>?) {
            medicinesList.clear()
            if (list != null) {
                medicinesList.addAll(list)
            }
            notifyDataSetChanged()
        }
    }
}