package com.example.medihelper.presentation.feature.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.R
import com.example.medihelper.databinding.DialogMedicinePlanHistoryBinding
import com.example.medihelper.presentation.framework.*
import com.example.medihelper.presentation.model.MedicinePlanHistoryItem
import kotlinx.android.synthetic.main.dialog_medicine_plan_history.*
import kotlinx.android.synthetic.main.recycler_item_history.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicinePlanHistoryDialog : AppDialog() {

    val viewModel: MedicinePlanHistoryViewModel by viewModel()
    val args: MedicinePlanHistoryDialogArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogMedicinePlanHistoryBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_medicine_plan_history,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setupHistoryRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.historyItemList.observe(viewLifecycleOwner, Observer { historyItemDisplayDataList ->
            val adapter = recycler_view_history.adapter as HistoryAdapter
            adapter.updateItemsList(historyItemDisplayDataList)
        })
    }

    private fun setupHistoryRecyclerView() {
        recycler_view_history.apply {
            adapter = HistoryAdapter()
            layoutManager = CenterLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    inner class HistoryAdapter : RecyclerAdapter<MedicinePlanHistoryItem>(
        layoutResId = R.layout.recycler_item_history,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.plannedDate == newItem.plannedDate }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val historyItemDisplayData = itemsList[position]
            holder.bind(historyItemDisplayData, this@MedicinePlanHistoryDialog)
            holder.view.lay_history_checkboxes.apply {
                removeAllViews()
                historyItemDisplayData.historyCheckboxList.forEach { historyCheckbox ->
                    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.view_history_checkbox, this, true)
                    binding.setVariable(BR.displayData, historyCheckbox)
                }
            }
        }
    }

}