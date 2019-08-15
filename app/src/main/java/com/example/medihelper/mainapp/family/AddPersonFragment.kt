package com.example.medihelper.mainapp.family

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentAddPersonBinding
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.mainapp.MainActivity
import kotlinx.android.synthetic.main.fragment_add_person.*

class AddPersonFragment : Fragment() {
    val TAG = AddPersonFragment::class.simpleName

    private lateinit var viewModel: AddPersonViewModel

    fun onClickSelectColor(colorResID: Int) {
        viewModel.personColorResIDLive.value = colorResID
    }

    fun onClickSaveNewPerson() {
        viewModel.saveNewPerson()
        findNavController().popBackStack()
    }

    fun onClickCancel() = findNavController().popBackStack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddPersonViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAddPersonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_person, container, false)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMainActivity()
        setupColorRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.personColorDisplayDataListLive.observe(viewLifecycleOwner, Observer { personColorDisplayDataList ->
            val adapter = recycler_view_color.adapter as PersonColorAdapter
            adapter.updateItemsList(personColorDisplayDataList)
        })
    }

    private fun setupMainActivity() {
        activity?.run {
            (this as MainActivity).apply {
                setTransparentStatusBar(true)
            }
        }
    }

    private fun setupColorRecyclerView() {
        recycler_view_color.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = PersonColorAdapter()
        }
    }

    // Inner classes
    inner class PersonColorAdapter : RecyclerAdapter<AddPersonViewModel.PersonColorDisplayData>(
        R.layout.recycler_item_person_color,
        object : DiffCallback<AddPersonViewModel.PersonColorDisplayData>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].colorResID == newList[newItemPosition].colorResID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personColorDisplayData = itemsList[position]
            holder.bind(personColorDisplayData, this@AddPersonFragment)
        }
    }
}