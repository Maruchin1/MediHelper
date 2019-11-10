package com.example.medihelper.mainapp.medicine


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentMedicinesBinding
import com.example.medihelper.localdatabase.pojo.MedicineItem
import com.example.medihelper.mainapp.MainActivity
import kotlinx.android.synthetic.main.fragment_medicines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicinesFragment : Fragment() {

    private val viewModel: MedicinesViewModel by viewModel()
    private val directions by lazyOf(MedicinesFragmentDirections)

    fun onClickOpenMedicineDetails(medicineID: Int) =
        findNavController().navigate(directions.toMedicineDetailsFragment(medicineID))

    fun onClickAddMedicine() = findNavController().navigate(directions.toAddEditMedicineFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMedicinesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicines, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
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
        viewModel.medicineItemListLive.observe(viewLifecycleOwner, Observer { medicineItemList ->
            val adapter = recycler_view_medicines.adapter as MedicineAdapter
            adapter.updateItemsList(medicineItemList)
        })
        viewModel.colorPrimaryLive.observe(viewLifecycleOwner, Observer { colorResId ->
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
                viewModel.searchQueryLive.value = newText
                return true
            }
        })
    }

    // Inner classes
    inner class MedicineAdapter : RecyclerAdapter<MedicineItem>(
        layoutResId = R.layout.recycler_item_medicine,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineID == newItem.medicineID }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicineItem = itemsList[position]
            val medicineDisplayData = viewModel.getMedicineItemDisplayData(medicineItem)
            holder.bind(medicineDisplayData, this@MedicinesFragment)
        }
    }
}
