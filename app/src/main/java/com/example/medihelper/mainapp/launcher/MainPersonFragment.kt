package com.example.medihelper.mainapp.launcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentMainPersonBinding
import com.example.medihelper.mainapp.LauncherActivity
import org.koin.android.ext.android.inject

class MainPersonFragment : LauncherOptionFragment() {

    private val viewModel: MainPersonViewModel by inject()

    fun onClickConfirm() = viewModel.saveMainProfile()

    fun onClickBack() = findNavController().popBackStack()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentMainPersonBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_main_person,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.setupEndAction.observe(viewLifecycleOwner, Observer {
            (requireActivity() as LauncherActivity).startMainActivity()
        })
    }
}