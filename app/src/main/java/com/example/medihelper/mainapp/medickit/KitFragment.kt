package com.example.medihelper.mainapp.medickit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentKitBinding
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_kit.*


class KitFragment : Fragment() {
    private val TAG = KitFragment::class.simpleName

    private lateinit var viewModel: KitViewModel

    fun onClickOpenMedicineDetails(medicineID: Int) {
        val action = KitFragmentDirections.toMedicineDetailsDestination(medicineID)
        findNavController().navigate(action)
    }

    fun onClickBackToMenu() = findNavController().popBackStack()

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
        setupMainActivity()
        setupRecyclerView()
        observeViewModel()
    }

    private fun openAddMedicineFragment() {
        val action = KitFragmentDirections.toAddMedicineDestination(-1)
        findNavController().navigate(action)
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                setTransparentStatusBar(false)
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
        viewModel.run {
            medicineKitItemListLive.observe(viewLifecycleOwner, Observer { medicineKitItemList ->
                val adapter = recycler_view_medicines.adapter as MedicineAdapter
                adapter.updateItemsList(medicineKitItemList)
            })
        }
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
            holder.bind(medicineDisplayData, this@KitFragment)
        }
    }
}
