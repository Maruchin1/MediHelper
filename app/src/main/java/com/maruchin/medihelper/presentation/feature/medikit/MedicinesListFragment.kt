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
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import com.maruchin.medihelper.databinding.FragmentMedicinesListBinding
import com.maruchin.medihelper.presentation.MainActivity
import com.maruchin.medihelper.presentation.framework.BaseFragment
import com.maruchin.medihelper.presentation.model.MedicineItem
import kotlinx.android.synthetic.main.fragment_medicines_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicinesListFragment : BaseFragment<FragmentMedicinesListBinding>(R.layout.fragment_medicines_list) {
    private val TAG = "MedicinesListFragment"

    private val viewModel: MedicinesListViewModel by viewModel()
    private val directions by lazyOf(MedicinesListFragmentDirections)
    private val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

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
        setupToolbar()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recycler_view_medicines.apply {
            adapter = MedicineAdapter()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) fab_add.hide() else fab_add.show()
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.medicineItemList.observe(viewLifecycleOwner, Observer { medicineItemList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.updateItemsList(medicineItemList)
        })
    }

    private fun setupToolbar() {
        val itemSearch = toolbar.menu.findItem(R.id.btn_search)
        val searchView = itemSearch.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.nameQuery.value = newText
                return true
            }
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
