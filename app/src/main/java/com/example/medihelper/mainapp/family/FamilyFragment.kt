package com.example.medihelper.mainapp.family


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentFamilyBinding
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.mainapp.MainActivity
import kotlinx.android.synthetic.main.fragment_family.*


class FamilyFragment : Fragment() {
    private val TAG = FamilyFragment::class.simpleName

    private lateinit var viewModel: FamilyViewModel

    fun onClickAddPerson() {
//        findNavController().navigate(FamilyFragmentDirections.toAddPersonDestination())
    }

    fun onClickBackToMenu() = findNavController().popBackStack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(FamilyViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFamilyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_family, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMainActivity()
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.personListItemLive.observe(viewLifecycleOwner, Observer { personListItemList ->
            val adapter = recycler_view_persons.adapter as PersonsAdapter
            adapter.updateItemsList(personListItemList)
        })
    }

    private fun setupRecyclerView() {
        recycler_view_persons.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PersonsAdapter()
        }
    }

    private fun setupMainActivity() {
        activity?.run {
            (this as MainActivity).apply {
                setTransparentStatusBar(false)
            }
        }
    }

    // Inner classes
    inner class PersonsAdapter : RecyclerAdapter<PersonItem>(
        R.layout.recycler_item_person,
        object : DiffCallback<PersonItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].personID == newList[newItemPosition].personID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            holder.bind(personItem, this@FamilyFragment)
        }
    }
}
