package com.example.medihelper.mainapp.persons

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogPersonBinding
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_person.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonDialog : BottomSheetDialogFragment() {
    val TAG = PersonDialog::class.simpleName

    private val viewModel: PersonViewModel by viewModel()
    private val directions by lazyOf(PersonDialogDirections)

    fun onClickSelectPerson(personID: Int) {
        viewModel.selectPerson(personID)
        dismiss()
    }

    fun onClickAddNewPerson() = findNavController().navigate(directions.toAddEditPersonFragment())

    fun onClickOpenOptions(personID: Int) {
        viewModel.optionsEnabledPersonIDLive.value = personID
    }

    fun onClickCloseOptions() {
        viewModel.optionsEnabledPersonIDLive.value = null
    }

    fun onClickDeletePerson(personID: Int) {
        viewModel.optionsEnabledPersonIDLive.value = null
        viewModel.deletePerson(personID)
    }

    fun onClickEditPerson(personID: Int) = findNavController().navigate(directions.toAddEditPersonFragment(personID))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogPersonBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_person, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPersonRecyclerView()
        observeData()
    }

    private fun setupPersonRecyclerView() {
        context?.let { context ->
            recycler_view_persons.apply {
                adapter = PersonAdapter()
                GravitySnapHelper(Gravity.START).attachToRecyclerView(this)
            }
        }
    }

    private fun observeData() {
        viewModel.personItemListLive.observe(viewLifecycleOwner, Observer { personItemList ->
            val adapter = recycler_view_persons.adapter as PersonAdapter
            adapter.updateItemsList(personItemList)
        })
        viewModel.optionsEnabledPersonIDLive.observe(viewLifecycleOwner, Observer { personID ->
            val adapter = recycler_view_persons.adapter as PersonAdapter
            if (personID != null) {
                adapter.openItemOptions(personID)
            } else {
                adapter.closeItemOptions()
            }
        })
    }

    // Inner classes
    inner class PersonAdapter : RecyclerAdapter<PersonItem>(
        layoutResId = R.layout.recycler_item_person,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.personID == newItem.personID }
    ) {
        private var optionsEnabledPosition = -1

        fun openItemOptions(personID: Int) {
            val prevPosition = optionsEnabledPosition
            val newPosition = itemsList.indexOfFirst { personItem ->
                personItem.personID == personID
            }
            optionsEnabledPosition = newPosition
            notifyItemChanged(prevPosition)
            notifyItemChanged(newPosition)
        }

        fun closeItemOptions() {
            val prevPosition = optionsEnabledPosition
            optionsEnabledPosition = -1
            notifyItemChanged(prevPosition)
        }

        override fun updateItemsList(newList: List<PersonItem>?) {
            val listWithOneEmptyItem = mutableListOf<PersonItem>().apply {
                if (newList != null) {
                    addAll(newList)
                }
                add(PersonItem(personID = -1, personName = "", personColorResID = 0))
            }
            super.updateItemsList(listWithOneEmptyItem.toList())
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            val personItemDisplayData = viewModel.getPersonItemDisplayData(personItem)
            if (position == optionsEnabledPosition) {
                personItemDisplayData.optionsEnabled = true
            }
            holder.bind(personItemDisplayData, this@PersonDialog)
        }
    }
}