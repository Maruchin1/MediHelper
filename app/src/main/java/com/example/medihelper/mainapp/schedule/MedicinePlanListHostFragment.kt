package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentMedicinePlanListHostBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_medicine_plan_list_host.*

class MedicinePlanListHostFragment : Fragment() {

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMedicinePlanListHostBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_plan_list_host, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupTabs()
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupTabs() {
        val fragOngoing = MedicinePlanListFragment().apply { medicinePlanType = ScheduleViewModel.MedicinePlanType.ONGOING }
        val fragEnded = MedicinePlanListFragment().apply { medicinePlanType = ScheduleViewModel.MedicinePlanType.ENDED }
        tab_layout.selectTab(null)
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedFrag = when (tab?.position) {
                    0 -> fragOngoing
                    1 -> fragEnded
                    else -> null
                }
                if (selectedFrag != null) {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.frame_medicine_plan_list, selectedFrag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
            }

        })
        tab_layout.getTabAt(0)?.select()
    }
}
