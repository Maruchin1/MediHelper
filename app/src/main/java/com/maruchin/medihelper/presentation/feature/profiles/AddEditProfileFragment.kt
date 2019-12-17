package com.maruchin.medihelper.presentation.feature.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditProfileBinding
import com.maruchin.medihelper.presentation.framework.*
import com.maruchin.medihelper.presentation.model.ProfileColorCheckbox
import kotlinx.android.synthetic.main.fragment_add_edit_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEditProfileFragment : BaseFragment<FragmentAddEditProfileBinding>(R.layout.fragment_add_edit_profile) {

    private val viewModel: AddEditProfileViewModel by viewModel()
    private val args: AddEditProfileFragmentArgs by navArgs()

    fun onClickSelectColor(color: String) {
        viewModel.selectedColor.value = color
    }

    fun onClickSaveProfile() {
        viewModel.saveProfile()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        super.setStatusBarColorLive(viewModel.selectedColor)
        super.setupToolbarNavigation()
        setupColorRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionProfileSaved.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    private fun setupColorRecyclerView() {
        recycler_view_color.apply {
            adapter = PersonColorAdapter()
        }
    }

    private fun setupFab() {
        fab_save.shrinkOnScroll(scroll_view)
    }

    // Inner classes
    inner class PersonColorAdapter : RecyclerAdapter<ProfileColorCheckbox>(
        layoutResId = R.layout.rec_item_profile_color,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.profileColorCheckboxList,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.color == newItem.color }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personColorCheckboxData = itemsList[position]
            holder.bind(personColorCheckboxData, this@AddEditProfileFragment)
        }
    }
}