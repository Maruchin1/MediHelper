package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentScheduleBinding
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_schedule.*


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
        observeViewModel()
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
                    shrink()
                    setIconResource(R.drawable.baseline_add_alert_black_48)
                    text = ""
                    setOnClickListener { openAddToScheduleFragment() }
                }
            }
        }
    }

    private fun setupDatesViewPager() {
        val adapter = ScheduleDayAdapter(childFragmentManager, viewModel)
        view_pager.offscreenPageLimit = 1
        view_pager.adapter = adapter
        view_pager.currentItem = ScheduleDayAdapter.TODAY_INDEX
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                (view_pager.adapter as ScheduleDayAdapter).getItem(position).let {

                }
            }
        })
        tab_lay_dates.setupWithViewPager(view_pager)
    }

    private fun openAddToScheduleFragment() {
        findNavController().navigate(
            ScheduleFragmentDirections.actionScheduleDestinationToAddToScheduleDestination()
        )
    }

    private fun observeViewModel() {
        viewModel.medicinesListLive.observe(viewLifecycleOwner, Observer { })
        viewModel.medicinesTypesListLive.observe(viewLifecycleOwner, Observer { })
    }
}
