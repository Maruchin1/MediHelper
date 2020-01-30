package com.maruchin.medihelper.presentation.feature.options.saved_types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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

    abstract override val viewModel: SavedTypesViewModel

    fun onClickDeleteType(type: String) {
        viewModel.deleteType(type)
        showTypeDeletedSnackbar(type)
        dismiss()
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

    private fun showTypeDeletedSnackbar(type: String) {
        val message = "Usunięto typ $type"
        val view = mainActivity.rootLayout
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setAction("Cofnij") {
            viewModel.addType(type)
            snackbar.dismiss()
        }
        snackbar.show()
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