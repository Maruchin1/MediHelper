package com.maruchin.medihelper.presentation.feature.options.reminders

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogHelpBinding
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import com.maruchin.medihelper.presentation.framework.BaseRecyclerConstAdapter
import com.maruchin.medihelper.presentation.framework.BaseViewHolder
import kotlinx.android.synthetic.main.dialog_help.*

class HelpDialog(
    private val helpItems: List<HelpItemData>
) : BaseBottomDialog<DialogHelpBinding>(R.layout.dialog_help, collapsing = true) {
    override val TAG: String
        get() = "HelpDialog"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHelpRecycler()
    }

    private fun setupHelpRecycler() {
        recycler_view_help.apply {
            adapter = HelpAdapter()
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private inner class HelpAdapter : BaseRecyclerConstAdapter<HelpItemData>(
        layoutResId = R.layout.rec_item_help,
        items = helpItems,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.header == newItem.header }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: HelpItemData) {
            holder.bind(item, handler = this@HelpDialog)
        }
    }
}