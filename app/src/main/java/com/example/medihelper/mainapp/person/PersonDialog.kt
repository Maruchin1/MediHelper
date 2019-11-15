package com.example.medihelper.mainapp.person

import android.os.Bundle
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
import com.example.medihelper.localdata.pojo.PersonItem
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

    fun onClickOpenOptions(personID: Int) = findNavController().navigate(directions.toPersonOptionsFragment(personID))

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
    }

    // Inner classes
    inner class PersonAdapter : RecyclerAdapter<PersonItem>(
        layoutResId = R.layout.recycler_item_person,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.personId == newItem.personId }
    ) {

        override fun updateItemsList(newList: List<PersonItem>?) {
            val listWithOneEmptyItem = mutableListOf<PersonItem>().apply {
                if (newList != null) {
                    addAll(newList)
                }
                add(PersonItem(personId = -1, personName = "", personColorResId = 0))
            }
            super.updateItemsList(listWithOneEmptyItem.toList())
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            val personItemDisplayData = viewModel.getPersonItemDisplayData(personItem)
            holder.bind(personItemDisplayData, this@PersonDialog)
        }
    }
}