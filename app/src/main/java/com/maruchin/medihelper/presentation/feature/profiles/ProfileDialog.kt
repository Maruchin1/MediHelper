package com.maruchin.medihelper.presentation.feature.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.leochuan.CarouselLayoutManager
import com.leochuan.CenterSnapHelper
import com.leochuan.ScaleLayoutManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogProfileBinding
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.presentation.dialogs.ConfirmDialog
import com.maruchin.medihelper.presentation.feature.calendar.CalendarFragmentDirections
import com.maruchin.medihelper.presentation.framework.*
import com.maruchin.medihelper.presentation.model.MedicinePlanItemData
import com.maruchin.medihelper.presentation.model.ProfileItemData
import kotlinx.android.synthetic.main.dialog_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileDialog : BaseBottomDialog<DialogProfileBinding>(R.layout.dialog_profile, collapsing = true) {
    override val TAG: String
        get() = "ProfileDialog"

    private val viewModel: ProfileViewModel by viewModel()

    fun onClickAddProfile() {
        val direction = CalendarFragmentDirections.toAddEditProfileFragment(editProfileId = null)
        requireParentFragment().findNavController().navigate(direction)
    }

    fun onClickEditProfile() {
        val profileId = viewModel.selectedProfileId
        val direction = CalendarFragmentDirections.toAddEditProfileFragment(profileId)
        requireParentFragment().findNavController().navigate(direction)
    }

    fun onClickDeleteProfile() {
        ConfirmDialog(
            title = "Usuń profil",
            message = "Wybrany profil zostanie usunięty, wraz z jego wpisami w kalendarzu. Czy chcesz kontynuować?",
            iconResId = R.drawable.round_delete_black_36
        ).apply {
            setColorPrimary(viewModel.colorPrimary.value)
            setOnConfirmClickListener {
                viewModel.deleteProfile()
            }
        }.show(childFragmentManager)
    }

    fun onClickMedicinePlanDetails(medicinePlanId: String) {
        val direction = CalendarFragmentDirections.toMedicinePlanDetailsFragment(medicinePlanId)
        requireParentFragment().findNavController().navigate(direction)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenu()
        setupProfileRecyclerView()
        setupMedicinesPlansRecyclerView()
        observeViewModel()
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
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

    private fun setupProfileRecyclerView() {
        recycler_view_profile.apply {
            adapter = ProfileAdapter()
            layoutManager = ScaleLayoutManager.Builder(requireContext(), 0)
                .setMinScale(0.75f)
                .setOrientation(CarouselLayoutManager.HORIZONTAL)
                .build()
            CenterSnapHelper().attachToRecyclerView(this)
        }
        setupProfileSelectedListener()
    }

    private fun setupProfileSelectedListener() {
        recycler_view_profile.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                updateSelectedProfileWhenScrollIdle(recyclerView, newState)
            }
        })
    }

    private fun updateSelectedProfileWhenScrollIdle(recyclerView: RecyclerView, scrollState: Int) {
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            val layoutManager = recyclerView.layoutManager as ScaleLayoutManager
            val selectedPosition = layoutManager.currentPosition
            viewModel.selectProfile(selectedPosition)
        }
    }

    private fun setupMedicinesPlansRecyclerView() {
        recycler_view_medicine_plan.apply {
            adapter = MedicinePlanAdapter()
        }
    }

    private fun observeViewModel() {
        viewModel.selectedProfilePosition.observe(viewLifecycleOwner, Observer { position ->
            recycler_view_profile.scrollToPosition(position)
        })
        viewModel.mainProfileSelected.observe(viewLifecycleOwner, Observer { mainSelected ->
            setEditDeleteProfileEnabled(!mainSelected)
        })
        viewModel.medicinesPlansAvailable.observe(viewLifecycleOwner, Observer {
            lay_medicines_plans.beginDelayedFade()
        })
    }

    private fun setEditDeleteProfileEnabled(enabled: Boolean) {
        with(toolbar.menu) {
            val btnDelete = findItem(R.id.btn_delete)
            if (btnDelete.isVisible != enabled) {
                toolbar.beginDelayedTransition()
                btnDelete.isVisible = enabled
            }
        }
    }

    private inner class ProfileAdapter : BaseRecyclerAdapter<ProfileItemData>(
        layoutResId = R.layout.rec_item_profile,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.profileItems
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(item, this@ProfileDialog)
        }
    }

    private inner class MedicinePlanAdapter : BaseRecyclerAdapter<MedicinePlanItemData>(
        layoutResId = R.layout.rec_item_medicine_plan,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = viewModel.medicinesPlans,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicinePlanId == newItem.medicinePlanId }
    ) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(item, this@ProfileDialog, viewModel = viewModel)
        }
    }
}