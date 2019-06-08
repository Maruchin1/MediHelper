package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.medihelper.R
import kotlinx.android.synthetic.main.fragment_schedule_list.*


class DayScheduleFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        arguments?.let { args ->
            args.getString(ARG_DATE)?.let { date ->
                observeViewModel(date)
            }
        }
    }

    private fun observeViewModel(date: String) {
        viewModel.getScheduledMedicinesByDateLive(date).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (recycler_view.adapter as DayScheduleAdapter).setScheduledMedicinesList(it)
            }
        })
    }

    private fun setupRecyclerView() {
        context?.run {
            recycler_view.adapter = DayScheduleAdapter(this)
            recycler_view.layoutManager = LinearLayoutManager(this)
        }
    }

    companion object {
        val ARG_DATE = "arg-date"

        fun getInstance(date: String): DayScheduleFragment {
            val instance = DayScheduleFragment()
            val args = Bundle()
            args.putString(ARG_DATE, date)
            instance.arguments = args
            return instance
        }
    }
}
