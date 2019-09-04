package com.example.medihelper.mainapp.medicines


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SearchViewBindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentMedicinesBinding
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.mainapp.MainActivity
import kotlinx.android.synthetic.main.fragment_medicines.*


class MedicinesFragment : Fragment() {

    private val viewModel: MedicinesViewModel by viewModels()
    private val directions by lazyOf(MedicinesFragmentDirections)

    fun onClickOpenMedicineDetails(medicineID: Int)  = findNavController().navigate(directions.toMedicineDetailsFragment(medicineID))

    fun onClickAddMedicine() = findNavController().navigate(directions.toAddMedicineFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMedicinesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_medicines, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMainActivity()
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupMainActivity() {
        (requireActivity() as MainActivity).setStatusBarColor(R.color.colorPrimary)
    }

    private fun setupRecyclerView() {
        recycler_view_medicines.apply {
            adapter = MedicineAdapter()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            medicineItemListLive.observe(viewLifecycleOwner, Observer { medicineItemList ->
                val adapter = recycler_view_medicines.adapter as MedicineAdapter
                adapter.updateItemsList(medicineItemList)
            })
        }
    }

    private fun setupSearchView() {
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        R.layout.recycler_item_medicine,
        object : DiffCallback<MedicineItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].medicineID == newList[newItemPosition].medicineID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicine = itemsList[position]
            val medicineDisplayData = viewModel.getMedicineKitItemDisplayData(medicine)
            holder.bind(medicineDisplayData, this@MedicinesFragment)
        }
    }
}
