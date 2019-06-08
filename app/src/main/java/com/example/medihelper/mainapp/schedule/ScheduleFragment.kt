package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentScheduleBinding
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*


class ScheduleFragment : Fragment() {
    private val TAG = ScheduleFragment::class.simpleName

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMainActivity()
        setupDatesViewPager()
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentScheduleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                setTransparentStatusBar(false)
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    show(true)
//                    if (isHidden) {
//                        show()
//                    }
                    shrink()
                    setIconResource(R.drawable.round_add_white_48)
                    text = ""
                    setOnClickListener { openAddToScheduleFragment() }
                }
            }
        }
    }

    private fun setupDatesViewPager() {
        val adapter = DaySchedulePagerAdapter(childFragmentManager)
        dates_view_pager.offscreenPageLimit = 1
        dates_view_pager.adapter = adapter
        dates_tab_lay.setupWithViewPager(dates_view_pager)
        dates_view_pager.currentItem = DaySchedulePagerAdapter.TODAY_INDEX
    }

    private fun openAddToScheduleFragment() {
        findNavController().navigate(
            ScheduleFragmentDirections.actionScheduleDestinationToAddToScheduleDestination()
        )
    }
}
