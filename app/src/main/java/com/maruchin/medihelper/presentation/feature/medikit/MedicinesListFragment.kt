package com.maruchin.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicinesListBinding
import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.presentation.MainActivity
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.fragment_medicines_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicinesListFragment : BaseMainFragment<FragmentMedicinesListBinding>(R.layout.fragment_medicines_list) {
    private val TAG = "MedicinesListFragment"

    private val viewModel: MedicinesListViewModel by viewModel()
    private val directions by lazyOf(MedicinesListFragmentDirections)

    fun onClickOpenMedicineDetails(medicineId: String) {
        findNavController().navigate(directions.toMedicineDetailsFragment(medicineId))
    }

    fun onClickAddMedicine() = findNavController().navigate(directions.toAddEditMedicineFragment(null))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recycler_view_medicines.apply {
            adapter = MedicineAdapter()
            addHideFabOnScroll(fab_add)
        }
    }

    private fun observeViewModel() {
        viewModel.medicineItemList.observe(viewLifecycleOwner, Observer { medicineItemList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.updateItemsList(medicineItemList)
        })
    }

    // Inner classes
    inner class MedicineAdapter : RecyclerAdapter<MedicineItem>(
        layoutResId = R.layout.recycler_item_medicine,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineId == newItem.medicineId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicineItem = itemsList[position]
            holder.bind(medicineItem, this@MedicinesListFragment)
        }
    }
}
