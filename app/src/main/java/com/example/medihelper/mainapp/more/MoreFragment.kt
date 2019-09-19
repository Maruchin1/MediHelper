package com.example.medihelper.mainapp.more


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentMoreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoreFragment : Fragment() {

    private val viewModel: MoreViewModel by viewModel()
    private val directions by lazy { MoreFragmentDirections }

    fun onClickMediHelperAccount() = findNavController().navigate(directions.toLoginRegisterFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMoreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}
