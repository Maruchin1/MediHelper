package com.maruchin.medihelper.presentation.feature.plans


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicinePlanListHostBinding
import com.maruchin.medihelper.domain.entities.MedicinePlanType
import com.maruchin.medihelper.presentation.MainActivity
import com.maruchin.medihelper.presentation.framework.bind
import kotlinx.android.synthetic.main.fragment_medicine_plan_list_host.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicinePlanListHostFragment : Fragment() {

    private val viewModel: MedicinePlanListViewModel by viewModel()
    private val directions by lazyOf(MedicinePlanListHostFragmentDirections)

    fun onClickAddMedicinePlan() = findNavController().navigate(directions.toAddEditMedicinePlanFragment())

    fun onClickSelectPerson() = findNavController().navigate(directions.toPersonDialog())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return bind<FragmentMedicinePlanListHostBinding>(
           inflater = inflater,
           container = container,
           layoutResId = R.layout.fragment_medicine_plan_list_host,
           viewModel = viewModel
       )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.colorPrimary.observe(viewLifecycleOwner, Observer { color ->
            if (!color.isNullOrEmpty()) {
                (requireActivity() as MainActivity).setMainColor(color)
            }
        })
    }

    private fun setupTabs() {
        view_pager_medicine_plan_list.adapter = MedicinePlanListPagerAdapter()
        tab_layout.setupWithViewPager(view_pager_medicine_plan_list)
    }

    // Inner classes
    inner class MedicinePlanListPagerAdapter : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val pagesList = listOf(
            MedicinePlanListFragment().apply { medicinePlanType = MedicinePlanType.ONGOING },
            MedicinePlanListFragment().apply { medicinePlanType = MedicinePlanType.ENDED }
        )

        override fun getCount(): Int {
            return pagesList.size
        }

        override fun getItem(position: Int): Fragment {
            return pagesList[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pagesList[position].medicinePlanType?.title
        }

    }
}
