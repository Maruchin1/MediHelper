package com.maruchin.medihelper.presentation.feature.profiles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.AppFullScreenDialog
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import com.maruchin.medihelper.databinding.FragmentAddEditPersonBinding
import com.maruchin.medihelper.presentation.framework.bind
import com.maruchin.medihelper.presentation.model.PersonColorCheckboxData
import kotlinx.android.synthetic.main.fragment_add_edit_person.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEditPersonFragment : AppFullScreenDialog() {
    private val TAG = "AddEditPersonFragment"

    private val viewModel: AddEditPersonViewModel by viewModel()
    private val args: AddEditPersonFragmentArgs by navArgs()

    fun onClickSelectColor(colorResID: Int) {
        viewModel.selectedColorId.value = colorResID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentAddEditPersonBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_add_edit_person,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setTransparentStatusBar()
        setupToolbar()
        setupColorRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.personColorCheckboxDataList.observe(viewLifecycleOwner, Observer { colorCheckboxList ->
            Log.i(TAG, "colorCheckboxList = $colorCheckboxList")
            val adapter = recycler_view_color.adapter as PersonColorAdapter
            adapter.updateItemsList(colorCheckboxList)
        })
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_save -> {
                    val personSaved = viewModel.savePerson()
                    if (personSaved) {
                        dismiss()
                    }
                }
            }
            true
        }
    }

    private fun setupColorRecyclerView() {
        recycler_view_color.apply {
            adapter = PersonColorAdapter()
        }
    }

    // Inner classes
    inner class PersonColorAdapter : RecyclerAdapter<PersonColorCheckboxData>(
        layoutResId = R.layout.recycler_item_person_color,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.colorId == newItem.colorId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personColorCheckboxData = itemsList[position]
            holder.bind(personColorCheckboxData, this@AddEditPersonFragment)
        }
    }
}