package com.maruchin.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicinesListBinding
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.fragment_medicines_list.*
import kotlinx.android.synthetic.main.rec_item_medicine.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicinesListFragment : BaseMainFragment<FragmentMedicinesListBinding>(R.layout.fragment_medicines_list) {
    private val TAG = "MedicinesListFragment"

    private val viewModel: MedicinesListViewModel by viewModel()
    private val directions by lazyOf(MedicinesListFragmentDirections)

    fun onClickOpenMedicineDetails(medicineId: String, view: View) {
        val imageView = view.findViewById<ImageView>(R.id.img_photo)
        val extras = FragmentNavigatorExtras(
            imageView to "medicine_image_details"
        )
        findNavController().navigate(directions.toMedicineDetailsFragment(medicineId), extras)
    }

    fun onClickAddMedicine() = findNavController().navigate(directions.toAddEditMedicineFragment(null))

    fun onClickOpenFilters() {
        drawer.openDrawer(GravityCompat.END)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        super.setLightStatusBar(false)
        setupToolbarMenu()
    }

    private fun setupRecyclerView() {
        recycler_view_medicines.apply {
            adapter = MedicineAdapter()
            fab_add.hideOnScroll(this)
        }
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_search -> {  }
                R.id.btn_filters -> { onClickOpenFilters() }
            }
            return@setOnMenuItemClickListener true
        }
    }

    // Inner classes
    inner class MedicineAdapter : RecyclerAdapter<MedicineItem>(
        layoutResId = R.layout.rec_item_medicine,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.medicineItemList,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineId == newItem.medicineId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicineItem = itemsList[position]
            holder.itemView.img_photo.transitionName = "medicine_image_item_$position"
            holder.bind(medicineItem, this@MedicinesListFragment, viewModel)
        }
    }
}
