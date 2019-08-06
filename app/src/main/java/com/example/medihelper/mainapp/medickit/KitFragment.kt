package com.example.medihelper.mainapp.medickit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide

import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentKitBinding
import com.example.medihelper.databinding.RecyclerItemMedicineBinding
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_kit.*
import kotlinx.android.synthetic.main.fragment_kit.btn_back_to_menu
import kotlinx.android.synthetic.main.recycler_item_medicine.view.*
import java.io.File


class KitFragment : Fragment() {
    private val TAG = KitFragment::class.simpleName

    private lateinit var viewModel: KitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(KitViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentKitBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_kit, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_back_to_menu.setOnClickListener { findNavController().popBackStack() }
        setupMainActivity()
        setupRecyclerView()
        observeViewModel()
    }

    private fun openAddMedicineFragment() {
        val action = KitFragmentDirections.actionKitDestinationToAddMedicineFragment(-1)
        findNavController().navigate(action)
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                setTransparentStatusBar(false)
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    show()
                    shrink()
                    setIconResource(R.drawable.round_add_white_48)
                    text = ""
                    setOnClickListener { openAddMedicineFragment() }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recycler_view_medicines.apply {
            adapter = MedicineAdapter()
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun observeViewModel() {
        viewModel.medicineTypesListLive.observe(viewLifecycleOwner, Observer { })
        viewModel.medicinesListLive.observe(viewLifecycleOwner, Observer { medicineList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.setMedicineList(medicineList)
        })
    }

    // Inner classes
    inner class MedicineAdapter : RecyclerView.Adapter<RecyclerItemViewHolder>() {

        private val medicinesArrayList = ArrayList<Medicine>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
            val binding: RecyclerItemMedicineBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.recycler_item_medicine,
                parent,
                false
            )
            return RecyclerItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return medicinesArrayList.size
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicine = medicinesArrayList[position]
            val medicineDisplayData = viewModel.getMedicineDisplayData(medicine)
            holder.bind(medicineDisplayData)
            holder.view.lay_click.setOnClickListener {
                openMedicineDetailsFragment(medicine.medicineID)
            }
            context?.run {
                Glide.with(this)
                    .load(File(medicine.photoFilePath))
                    .centerCrop()
                    .into(holder.view.img_photo)
            }
        }

        fun setMedicineList(list: List<Medicine>?) {
            medicinesArrayList.clear()
            if (list != null) {
                medicinesArrayList.addAll(list)
            }
            notifyDataSetChanged()
        }

        private fun openMedicineDetailsFragment(medicineID: Int) {
            val action = KitFragmentDirections.actionKitDestinationToMedicineDetailsFragment(medicineID)
            findNavController().navigate(action)
        }
    }
}
