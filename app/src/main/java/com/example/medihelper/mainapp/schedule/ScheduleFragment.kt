package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medihelper.DateUtil
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
        observeViewModel()
        setupToolbar()
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
                    show()
                    shrink()
                    setIconResource(R.drawable.round_add_alert_white_48)
                    text = ""
                    setOnClickListener { openSelectMedicineDialog() }
                }
            }
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupDatesViewPager() {
        view_pager.adapter = ScheduleDayPagerAdapter(childFragmentManager)
        view_pager.setCurrentItem(5000, false)
        view_pager.post {
            view_pager.setCurrentItem(5000, false)
        }
        view_pager.adapter?.notifyDataSetChanged()
        view_pager.offscreenPageLimit = 0
    }

    private fun openSelectMedicineDialog() {
        val dialog = SelectMedicineDialogFragment()
        dialog.show(childFragmentManager, SelectMedicineDialogFragment.TAG)
    }

    private fun observeViewModel() {
        viewModel.medicinesListLive.observe(viewLifecycleOwner, Observer { })
        viewModel.medicinesTypesListLive.observe(viewLifecycleOwner, Observer { })
    }

    inner class ScheduleDayPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return 10000
        }

        override fun getItemPosition(`object`: Any): Int {
            return FragmentStatePagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.add(Calendar.DAY_OF_YEAR, position - 5000)
            val dateString = DateUtil.dateToString(calendar.time)
            return ScheduleDayFragment.getInstance(dateString)
        }
    }
}
