package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentMedicinePlanListHostBinding
import com.example.medihelper.mainapp.family.PersonDialog
import com.example.medihelper.mainapp.MainActivity
import kotlinx.android.synthetic.main.fragment_medicine_plan_list_host.*

class MedicinePlanListHostFragment : Fragment() {

    private val viewModel: ScheduleViewModel by activityViewModels()
    private val directions by lazyOf(MedicinePlanListHostFragmentDirections)

    fun onClickAddMedicinePlan() = findNavController().navigate(directions.toAddEditMedicinePlanFragment())

    fun onClickSelectPerson() = findNavController().navigate(directions.toPersonDialog())

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
        setupTabs()
        setupToolbarMenu()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.colorPrimaryLive.observe(viewLifecycleOwner, Observer { colorResID ->
            if (colorResID != null) {
                activity?.run {
                    (this as MainActivity).setStatusBarColor(colorResID)
                }
            }
        })
    }

    private fun setupToolbarMenu() {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupTabs() {
        view_pager_medicine_plan_list.adapter = MedicinePlanListPagerAdapter()
        tab_layout.setupWithViewPager(view_pager_medicine_plan_list)
    }

    // Inner classes
    inner class MedicinePlanListPagerAdapter : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val pagesList = listOf(
            MedicinePlanListFragment().apply { medicinePlanType = ScheduleViewModel.MedicinePlanType.ONGOING},
            MedicinePlanListFragment().apply { medicinePlanType = ScheduleViewModel.MedicinePlanType.ENDED }
        )

        override fun getCount(): Int {
            return pagesList.size
        }

        override fun getItem(position: Int): Fragment {
            return pagesList[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pagesList[position].medicinePlanType?.pageTitle
        }

    }
}
