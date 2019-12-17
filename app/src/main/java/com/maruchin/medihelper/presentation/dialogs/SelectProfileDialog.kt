package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.View
import androidx.lifecycle.liveData
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogSelectProfileBinding
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.usecases.profile.GetLiveAllProfilesItemsUseCase
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import kotlinx.android.synthetic.main.dialog_select_profile.*
import org.koin.android.ext.android.inject

class SelectProfileDialog :
    BaseBottomDialog<DialogSelectProfileBinding>(R.layout.dialog_select_profile, collapsing = true) {
    override val TAG: String
        get() = "SelectProfileDialog"

    private val getLiveAllProfilesItemsUseCase: GetLiveAllProfilesItemsUseCase by inject()
    private val dataSource = liveData {
        val source = getLiveAllProfilesItemsUseCase.execute()
        emitSource(source)
    }
    private var profileSelectedListener: ((profileId: String) -> Unit)? = null

    fun onClickSelectProfile(profileId: String) {
        profileSelectedListener?.invoke(profileId)
        dismiss()
    }

    fun setProfileSelectedLsitener(listener: (profileId: String) -> Unit) {
        profileSelectedListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recycler_view_profile.adapter = ProfileAdapter()
    }

    inner class ProfileAdapter : RecyclerAdapter<ProfileItem>(
        layoutResId = R.layout.rec_item_select_profile,
        lifecycleOwner = viewLifecycleOwner,
        itemsSource = dataSource,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.profileId == newItem.profileId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(item, this@SelectProfileDialog)
        }
    }
}