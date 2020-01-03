package com.maruchin.medihelper.presentation.feature.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditProfileBinding
import com.maruchin.medihelper.presentation.framework.*
import com.maruchin.medihelper.presentation.model.ColorCheckboxData
import kotlinx.android.synthetic.main.fragment_add_edit_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEditProfileFragment : BaseMainFragment<FragmentAddEditProfileBinding>(R.layout.fragment_add_edit_profile) {

    private val viewModel: AddEditProfileViewModel by viewModel()
    private val args: AddEditProfileFragmentArgs by navArgs()

    fun onClickSelectColor(color: String) {
        viewModel.selectedColor.value = color
    }

    fun onClickSaveProfile() {
        viewModel.saveProfile()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setStatusBarLiveColor()
        setupToolbarNavigation()
        setupToolbarMenu()
        setupColorRecyclerView()
        observeViewModel()
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun initViewModel() {
        viewModel.initViewModel(args.editProfileId)
    }

    private fun setStatusBarLiveColor() {
        super.setStatusBarColorLive(viewModel.selectedColor)
    }

    private fun setupToolbarNavigation() {
        val navController = findNavController()
        toolbar.setupWithNavController(navController)
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btn_save -> onClickSaveProfile()
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupColorRecyclerView() {
        recycler_view_color.apply {
            adapter = PersonColorAdapter()
        }
    }

    private fun observeViewModel() {
        viewModel.actionProfileSaved.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    // Inner classes
    inner class PersonColorAdapter : BaseRecyclerAdapter<ColorCheckboxData>(
        layoutResId = R.layout.rec_item_profile_color,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.colorCheckboxDataList,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.color == newItem.color }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val personColorCheckboxData = itemsList[position]
            holder.bind(personColorCheckboxData, this@AddEditProfileFragment)
        }
    }
}