package com.maruchin.medihelper.presentation.feature.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentOptionsBinding
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment : BaseMainFragment<FragmentOptionsBinding>(R.layout.fragment_options) {

    private val viewModel: OptionsViewModel by viewModel()
    private val loadingScreen: LoadingScreen by inject()

    fun onClickSignOut() {
        viewModel.signOutUser()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingScreen.bind(this, viewModel.loadingInProgress)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionSignOutSuccessful.observe(viewLifecycleOwner, Observer {
            super.mainActivity.restartApp()
        })
    }
}