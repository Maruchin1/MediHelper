package com.maruchin.medihelper.presentation.feature.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leochuan.CarouselLayoutManager
import com.leochuan.CenterSnapHelper
import com.leochuan.ScaleLayoutManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogProfileBinding
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.presentation.dialogs.ConfirmDialog
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.dialog_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileDialog : BaseBottomDialog<DialogProfileBinding>(R.layout.dialog_profile, collapsing = true) {
    override val TAG: String
        get() = "MedicinesPlansFragment"

    private val viewModel: ProfileViewModel by viewModel()
    private val directions by lazy { CalendarFragmentDirections }

    fun onClickAddProfile() {
        requireParentFragment().findNavController().navigate(directions.toAddEditProfileFragment(editProfileId = null))
    }

    fun onClickEditProfile() {
//        findNavController().navigate(directions.toAddEditProfileFragment(editProfileId = viewModel.selectedProfileId))
    }

    fun onClickDeleteProfile() {
        ConfirmDialog().apply {
            title = "Usuń profil"
            message = "Wybrany profil zostanie usunięty, wraz z jego wpisami w kalendarzu. Czy chcesz kontynuować?"
            setOnConfirmClickListener {
                viewModel.deleteProfile()
            }
        }.show(childFragmentManager)
    }

    fun onClickMedicinePlanDetails() {
        requireParentFragment().findNavController().navigate(directions.toMedicinePlanDetailsFragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileRecyclerView()
        setupMedicinesPlansRecyclerView()
        setupToolbarMenu()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.selectedProfilePosition.observe(viewLifecycleOwner, Observer { position ->
            recycler_view_profile.scrollToPosition(position)
        })
    }

    private fun setupProfileRecyclerView() {
        recycler_view_profile.apply {
            adapter = ProfileAdapter()
            layoutManager = CarouselLayoutManager(requireContext(), 0)
            CenterSnapHelper().attachToRecyclerView(this)
        }
        setupProfileSelectedListener()
    }

    private fun setupProfileSelectedListener() {
        recycler_view_profile.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as CarouselLayoutManager
                    val selectedPosition = layoutManager.currentPosition
                    viewModel.selectProfile(selectedPosition)
                }
            }
        })
    }

    private fun setupMedicinesPlansRecyclerView() {
        recycler_view_medicine_plan.apply {
            adapter = MedicinePlanAdapter()
        }
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btn_add -> onClickAddProfile()
                R.id.btn_edit -> onClickEditProfile()
                R.id.btn_delete -> onClickDeleteProfile()
            }
            return@setOnMenuItemClickListener true
        }
    }

    private inner class ProfileAdapter : RecyclerAdapter<ProfileItem>(
        layoutResId = R.layout.rec_item_profile,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.profileItems,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.profileId == newItem.profileId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(item, this@ProfileDialog)
        }
    }

    private inner class MedicinePlanAdapter : RecyclerAdapter<MedicinePlanItem>(
        layoutResId = R.layout.recycler_item_medicine_plan,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.medicinesPlans,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicinePlanId == newItem.medicinePlanId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(item, this@ProfileDialog)
        }
    }
}