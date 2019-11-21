package com.example.medihelper.presentation.feature.personsprofiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.R
import com.example.medihelper.presentation.framework.CenterLayoutManager
import com.example.medihelper.presentation.framework.RecyclerAdapter
import com.example.medihelper.presentation.framework.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogPersonBinding
import com.example.medihelper.presentation.framework.bind
import com.example.medihelper.presentation.model.PersonItem
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

    fun onClickOpenOptions(personID: Int) = findNavController().navigate(
        directions.toPersonOptionsFragment(personID)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogPersonBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_person
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPersonRecyclerView()
        observeData()
    }

    private fun setupPersonRecyclerView() = recycler_view_persons.apply {
        adapter = PersonAdapter()
        layoutManager = CenterLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun observeData() {
        viewModel.personItemList.observe(viewLifecycleOwner, Observer { personItemList ->
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
                add(PersonItem(personId = -1, name = "", colorId = 0, mainPerson = false))
            }
            super.updateItemsList(listWithOneEmptyItem.toList())
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            holder.bind(personItem, this@PersonDialog)
        }
    }
}