package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medihelper.DateUtil

import com.example.medihelper.R
import kotlinx.android.synthetic.main.fragment_schedule_day.*
import java.util.*


class ScheduleDayFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_schedule_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { args ->
            val date = args.getString(ARG_DATE, "Brak")
            txv_date.text = date
        }

//        setupRecyclerView()
//        arguments?.let { args ->
//            args.getString(ARG_DATE)?.let { dateString ->
//                observeViewModel(DateTime(dateString))
//            }
//        }
    }

//    private fun observeViewModel(date: Date) {
//        viewModel.getScheduledMedicinesByDateLive(date).observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                (recycler_view.adapter as ScheduleMedicineAdapter).setScheduledMedicinesList(it)
//            }
//        })
//    }
//
//    private fun setupRecyclerView() {
//        context?.run {
//            recycler_view.adapter = ScheduleMedicineAdapter(this, viewModel)
//            recycler_view.layoutManager = LinearLayoutManager(this)
//            recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        }
//    }

    companion object {
        val ARG_DATE = "arg-date"

        fun getInstance(date: String): ScheduleDayFragment {
            val instance = ScheduleDayFragment()
            val args = Bundle()
            args.putString(ARG_DATE, date)
            instance.arguments = args
            return instance
        }
    }
}
