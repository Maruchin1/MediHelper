package com.example.medihelper.mainapp.persons

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.R
import com.example.medihelper.custom.CenterLayoutManager
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

    fun onClickConnectApps(personID: Int) {

    }

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

    private fun setupPersonRecyclerView() = recycler_view_persons.apply {
        adapter = PersonAdapter()
        layoutManager = CenterLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
            setIsScrollEnable(true)
            val prevPosition = optionsEnabledPosition
            optionsEnabledPosition = -1
            notifyItemChanged(prevPosition)
        }

        override fun updateItemsList(newList: List<PersonItem>?) {
            val listWithOneEmptyItem = mutableListOf<PersonItem>().apply {
                if (newList != null) {
                    addAll(newList)
                }
                add(PersonItem(personID = -1, personName = "", personColorResID = 0, mainPerson = false))
            }
            super.updateItemsList(listWithOneEmptyItem.toList())
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            val personItemDisplayData = viewModel.getPersonItemDisplayData(personItem)
            if (position == optionsEnabledPosition) {
                recycler_view_persons.smoothScrollToPosition(position)
                personItemDisplayData.optionsEnabled = true
                Handler().postDelayed({ setIsScrollEnable(false) }, 500)
            }
            holder.bind(personItemDisplayData, this@PersonDialog)
        }

        private fun setIsScrollEnable(enabled: Boolean) {
            (recycler_view_persons.layoutManager as CenterLayoutManager).isScrollEnabled = enabled
        }

    }
}