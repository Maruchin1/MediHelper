package com.maruchin.medihelper.presentation.feature.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maruchin.medihelper.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maruchin.medihelper.databinding.DialogSelectProfileBinding
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.dialog_select_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectProfileDialog : BaseDialog<DialogSelectProfileBinding>(R.layout.dialog_select_profile){
    override val TAG: String
        get() = "SelectProfileDialog"

    private val viewModel: SelectProfileViewModel by viewModel()
    private val directions by lazyOf(SelectProfileDialogDirections)

    fun onClickSelectPerson(profileId: String) {
        viewModel.selectProfile(profileId)
        dismiss()
    }

    fun onClickAddNewPerson() = findNavController().navigate(directions.toAddEditPersonFragment())

    fun onClickOpenOptions(profileId: String) {
//        findNavController().navigate(directions.toPersonOptionsFragment(profileId))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
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
        viewModel.profileItemList.observe(viewLifecycleOwner, Observer { personItemList ->
            val adapter = recycler_view_persons.adapter as PersonAdapter
            adapter.updateItemsList(personItemList)
        })
    }

    // Inner classes
    inner class PersonAdapter : RecyclerAdapter<ProfileItem>(
        layoutResId = R.layout.recycler_item_profile,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.profileId == newItem.profileId }
    ) {

        override fun updateItemsList(newList: List<ProfileItem>?) {
            val listWithOneEmptyItem = mutableListOf<ProfileItem>().apply {
                if (newList != null) {
                    addAll(newList)
                }
                add(ProfileItem(profileId = "", name = "", color = "", mainPerson = false))
            }
            super.updateItemsList(listWithOneEmptyItem.toList())
        }

        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            holder.bind(personItem, this@SelectProfileDialog)
        }
    }
}