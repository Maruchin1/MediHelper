package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.DialogSelectPersonBinding
import com.example.medihelper.localdatabase.pojos.PersonSimpleItem
import com.example.medihelper.mainapp.schedule.AddMedicinePlanFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_person.*

class SelectPersonDialog : BottomSheetDialogFragment() {
    val TAG = SelectPersonDialog::class.simpleName

    private var personSelectedListener: ((personID: Int) -> Unit)? = null

    fun setPersonSelectedListener(listener: (personID: Int) -> Unit) {
        personSelectedListener = listener
    }

    fun onClickSelectPerson(personID: Int) {
        personSelectedListener?.invoke(personID)
        dismiss()
    }

    fun onClickAddNewPerson() = findNavController().navigate(R.id.add_person_destination)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogSelectPersonBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_person, container, false)
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
                layoutManager = LinearLayoutManager(context)
                adapter = PersonAdapter()
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun observeData() {
        AppRepository.getPersonSimpleItemListLive().observe(viewLifecycleOwner, Observer { personListItemList ->
            val adapter = recycler_view_persons.adapter as PersonAdapter
            adapter.updateItemsList(personListItemList)
        })
    }

    // Inner classes
    inner class PersonAdapter : RecyclerAdapter<PersonSimpleItem>(
        R.layout.recycler_item_select_person,
        object : DiffCallback<PersonSimpleItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].personID == newList[newItemPosition].personID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personSimpleItem = itemsList[position]
            holder.bind(personSimpleItem, this@SelectPersonDialog)
        }
    }
}