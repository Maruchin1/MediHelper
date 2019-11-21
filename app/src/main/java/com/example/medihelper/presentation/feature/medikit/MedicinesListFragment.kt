package com.example.medihelper.presentation.feature.medikit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.R
import com.example.medihelper.presentation.framework.RecyclerAdapter
import com.example.medihelper.presentation.framework.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentMedicinesListBinding
import com.example.medihelper.presentation.feature.MainActivity
import com.example.medihelper.presentation.framework.bind
import com.example.medihelper.presentation.model.MedicineItem
import kotlinx.android.synthetic.main.fragment_medicines_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicinesListFragment : Fragment() {

    private val viewModel: MedicinesListViewModel by viewModel()
    private val directions by lazyOf(MedicinesListFragmentDirections)

    fun onClickOpenMedicineDetails(medicineID: Int) = findNavController().navigate(
        directions.toMedicineDetailsFragment(medicineID)
    )

    fun onClickAddMedicine() = findNavController().navigate(directions.toAddEditMedicineFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentMedicinesListBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_medicines_list,
            viewModel = viewModel
        )
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
        viewModel.colorPrimary.observe(viewLifecycleOwner, Observer { colorResId ->
            colorResId?.let { (requireActivity() as MainActivity).setMainColor(it) }
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
