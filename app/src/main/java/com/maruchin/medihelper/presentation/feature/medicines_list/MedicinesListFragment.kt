package com.maruchin.medihelper.presentation.feature.medicines_list


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
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.fragment_medicines_list.*
import kotlinx.android.synthetic.main.rec_item_medicine.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicinesListFragment : BaseHomeFragment<FragmentMedicinesListBinding>(R.layout.fragment_medicines_list) {
    private val TAG = "MedicinesListFragment"

    private val viewModel: MedicinesListViewModel by viewModel()

    fun onClickOpenMedicineDetails(medicineId: String, view: View) {
        val imageView = view.findViewById<ImageView>(R.id.img_photo)
        val extras = FragmentNavigatorExtras(
            imageView to "medicine_image_details"
        )
        val direction =
            MedicinesListFragmentDirections.toMedicineDetailsFragment(
                medicineId
            )
        findNavController().navigate(direction, extras)
    }

    fun onClickAddMedicine() {
        val direction =
            MedicinesListFragmentDirections.toAddEditMedicineFragment(
                null
            )
        findNavController().navigate(direction)
    }

    fun onClickOpenFilters() {
        drawer.openDrawer(GravityCompat.END)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBingingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableLightStatusBar()
        setupRecyclerView()
        setupToolbarMenu()
    }

    private fun setBingingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun disableLightStatusBar() {
        super.setLightStatusBar(false)
    }

    private fun setupRecyclerView() {
        recycler_view_medicines.apply {
            adapter = MedicineAdapter()
            fab_add.hideOnScroll(this)
        }
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            matchClickedToolbarMenuItem(menuItem.itemId)
            true
        }
    }

    private fun matchClickedToolbarMenuItem(menuItemId: Int) {
        when (menuItemId) {
            R.id.btn_search -> {
            }
            R.id.btn_filters -> onClickOpenFilters()
        }
    }

    // Inner classes
    inner class MedicineAdapter : BaseRecyclerAdapter<MedicineItemData>(
        layoutResId = R.layout.rec_item_medicine,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.medicineItemList,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineId == newItem.medicineId }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val medicineItem = itemsList[position]
            holder.itemView.img_photo.transitionName = "medicine_image_item_$position"
            holder.bind(medicineItem, this@MedicinesListFragment, viewModel)
        }
    }
}
