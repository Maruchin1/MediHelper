package com.maruchin.medihelper.presentation.feature.medicine_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogMedicineInfoBinding
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import com.maruchin.medihelper.presentation.framework.BaseRecyclerLiveAdapter
import com.maruchin.medihelper.presentation.framework.BaseViewHolder
import kotlinx.android.synthetic.main.dialog_medicine_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicineInfoDialog :
    BaseBottomDialog<DialogMedicineInfoBinding>(R.layout.dialog_medicine_info, collapsing = true) {
    override val TAG: String
        get() = "MedicineInfoDialog"
    override val viewModel: MedicineInfoViewModel by viewModel()

    private val args: MedicineInfoDialogArgs by navArgs()

    fun onClickSelectSearchResult(urlString: String) {
        changeAdapterToMedicineInfo()
        viewModel.getMedicineInfo(urlString)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setupRecyclerView()
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
        }
    }

    inner class SearchResultAdapter : BaseRecyclerLiveAdapter<MedicineInfoSearchResult>(
        layoutResId = R.layout.rec_item_medicine_info_search_result,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.searchResults,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicineName == newItem.medicineName }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: MedicineInfoSearchResult) {
            holder.bind(item, this@MedicineInfoDialog)
        }
    }

    inner class MedicineInfoAdapter : BaseRecyclerLiveAdapter<MedicineInfo>(
        layoutResId = R.layout.rec_item_medicine_info,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.medicineInfo,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.header == newItem.header }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: MedicineInfo) {
            holder.bind(item, handler =  this@MedicineInfoDialog, viewModel = viewModel)
        }
    }
}