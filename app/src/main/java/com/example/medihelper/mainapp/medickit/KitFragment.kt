package com.example.medihelper.mainapp.medickit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentKitBinding
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.medickit.KitFragmentDirections
import com.example.medihelper.mainapp.menu.MenuDialogFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_kit.*


class KitFragment : Fragment() {
    private val TAG = KitFragment::class.simpleName

    private lateinit var viewModel: KitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(KitViewModel::class.java)
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
        setupRecyclerView()
        observeViewModel()
    }

    fun openMedicineDetailsFragment(medicineID: Int) {
//        val extras = FragmentNavigatorExtras(
//            imageView to getString(R.string.shared_image_photo)
//        )
        val action = KitFragmentDirections.actionKitDestinationToMedicineDetailsFragment(medicineID)
        findNavController().navigate(action)
    }

    private fun openAddMedicineFragment() {
        val action = KitFragmentDirections.actionKitDestinationToAddMedicineFragment()
        findNavController().navigate(action)
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentKitBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_kit, container, false)
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
                    setIconResource(R.drawable.round_add_white_48)
                    text = ""
                    setOnClickListener { openAddMedicineFragment() }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        context?.let { context ->
            with(recycler_view) {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = KitAdapter(context, this@KitFragment)
//                addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
            }
        }
    }

    private fun observeViewModel() {
        viewModel.medicinesListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) (recycler_view.adapter as KitAdapter).setMedicinesList(it)
        })
        viewModel.medicineTypesListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) (recycler_view.adapter as KitAdapter).setMedicineTypesList(it)
        })
    }

    private fun openMenuDialogFragment() {
        activity?.let { activity ->
            val dialog = MenuDialogFragment()
            dialog.show(activity.supportFragmentManager, MenuDialogFragment.TAG)
        }
    }
}
