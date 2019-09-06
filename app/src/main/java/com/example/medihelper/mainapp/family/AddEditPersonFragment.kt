package com.example.medihelper.mainapp.family

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentAddEditPersonBinding
import kotlinx.android.synthetic.main.fragment_add_edit_person.*

class AddEditPersonFragment : AppFullScreenDialog() {

    private val viewModel: AddEditPersonViewModel by viewModels()
    private val args: AddEditPersonFragmentArgs by navArgs()

    fun onClickSelectColor(colorResID: Int) {
        viewModel.personColorResIDLive.value = colorResID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAddEditPersonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_person, container, false)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setTransparentStatusBar()
        setupToolbar()
        setupColorRecyclerView()
        observeViewModel()
    }

    private fun setTransparentStatusBar() {
        context?.run {
            dialog?.window?.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    private fun observeViewModel() {
        viewModel.personColorDisplayDataListLive.observe(viewLifecycleOwner, Observer { personColorDisplayDataList ->
            val adapter = recycler_view_color.adapter as PersonColorAdapter
            adapter.updateItemsList(personColorDisplayDataList)
        })
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_save -> {
                    val personSaved = viewModel.saveNewPerson()
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
    inner class PersonColorAdapter : RecyclerAdapter<AddEditPersonViewModel.PersonColorDisplayData>(
        R.layout.recycler_item_person_color,
        object : DiffCallback<AddEditPersonViewModel.PersonColorDisplayData>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].colorResID == newList[newItemPosition].colorResID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personColorDisplayData = itemsList[position]
            holder.bind(personColorDisplayData, this@AddEditPersonFragment)
        }
    }
}