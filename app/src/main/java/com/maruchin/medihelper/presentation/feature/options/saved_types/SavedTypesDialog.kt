package com.maruchin.medihelper.presentation.feature.options.saved_types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogSavedTypesBinding
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import com.maruchin.medihelper.presentation.framework.BaseRecyclerAdapter
import com.maruchin.medihelper.presentation.framework.BaseRecyclerLiveAdapter
import com.maruchin.medihelper.presentation.framework.BaseViewHolder
import kotlinx.android.synthetic.main.dialog_saved_types.*

abstract class SavedTypesDialog :
    BaseBottomDialog<DialogSavedTypesBinding>(R.layout.dialog_saved_types, collapsing = true) {
    override val TAG: String
        get() = "SavedTypesDialog"

    protected abstract val viewModel: SavedTypesViewModel

    fun onClickDeleteType(type: String) {
        viewModel.deleteType(type)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTypesRecycler()
    }

    private fun setupTypesRecycler() {
        recycler_view_saved_types.apply {
            adapter = SavedTypeAdapter()
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private inner class SavedTypeAdapter : BaseRecyclerLiveAdapter<String>(
        layoutResId = R.layout.rec_item_saved_type,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.types,
        areItemsTheSameFun = { oldItem, newItem -> oldItem == newItem }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: String) {
            holder.bind(displayData = item, handler = this@SavedTypesDialog)
        }
    }
}