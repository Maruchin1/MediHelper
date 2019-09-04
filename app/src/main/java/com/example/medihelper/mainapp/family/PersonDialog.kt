package com.example.medihelper.mainapp.family

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogPersonBinding
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_person.*

class PersonDialog : BottomSheetDialogFragment() {
    val TAG = PersonDialog::class.simpleName

    private val viewModel: PersonViewModel by viewModels()
    private val directions by lazyOf(PersonDialogDirections)

    fun onClickSelectPerson(personID: Int) {
        viewModel.selectPerson(personID)
        dismiss()
    }

    fun onClickAddNewPerson() = findNavController().navigate(directions.toAddPersonFragment())

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

    fun onClickEditPerson(personID: Int) = findNavController().navigate(directions.toAddPersonFragment(personID))

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
        R.layout.recycler_item_person,
        object : DiffCallback<PersonItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].personID == newList[newItemPosition].personID
            }
        }
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