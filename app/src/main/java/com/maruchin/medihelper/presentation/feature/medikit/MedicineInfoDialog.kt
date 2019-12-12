package com.maruchin.medihelper.presentation.feature.medikit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogMedicineInfoBinding
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import kotlinx.android.synthetic.main.dialog_medicine_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicineInfoDialog :
    BaseBottomDialog<DialogMedicineInfoBinding>(R.layout.dialog_medicine_info, collapsing = true) {
    override val TAG: String
        get() = "MedicineInfoDialog"

    private val viewModel: MedicineInfoViewModel by viewModel()
    private val args: MedicineInfoDialogArgs by navArgs()

    fun onClickSelectSearchResult(urlString: String) {
        changeAdapterToMedicineInfo()
        viewModel.getMedicineInfo(urlString)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loadingInProgress.observe(viewLifecycleOwner, Observer { inProgress ->
            progress_bar.visibility = if (inProgress) View.VISIBLE else View.GONE
        })
        viewModel.searchResults.observe(viewLifecycleOwner, Observer { list ->
            val adapter = recycler_view_search_result.adapter
            if (adapter is SearchResultAdapter) {
                adapter.updateItemsList(list)
            }
        })
        viewModel.medicineInfo.observe(viewLifecycleOwner, Observer { list ->
            val adapter = recycler_view_search_result.adapter
            if (adapter is MedicineInfoAdapter) {
                adapter.updateItemsList(list)
            }
        })
    }

    private fun setupRecyclerView() {
        recycler_view_search_result.apply {
            adapter = SearchResultAdapter()
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private fun changeAdapterToMedicineInfo() {
        recycler_view_search_result.apply {
            adapter = MedicineInfoAdapter()
            super.setCollapsed()
        }
    }

    inner class SearchResultAdapter : RecyclerAdapter<MedicineInfoSearchResult>(
        layoutResId = R.layout.rec_item_medicine_info_search_result,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineName == newItem.medicineName }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val searchResult = itemsList[position]
            holder.bind(searchResult, this@MedicineInfoDialog)
        }
    }

    inner class MedicineInfoAdapter : RecyclerAdapter<MedicineInfo>(
        layoutResId = R.layout.rec_item_medicine_info,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.header == newItem.header }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val info = itemsList[position]
            holder.bind(info, handler =  this@MedicineInfoDialog, viewModel = viewModel)
        }
    }
}