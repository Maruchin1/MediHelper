package com.example.medihelper.mainapp.medickit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Fade
import com.bumptech.glide.Glide
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentMedicineDetailsBinding
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.TransitionHelper
import kotlinx.android.synthetic.main.fragment_medicine_details.*
import java.io.File


class MedicineDetailsFragment : Fragment() {
    private val TAG = MedicineDetailsFragment::class.simpleName

    private val args: MedicineDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: MedicineDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(MedicineDetailsViewModel::class.java)
            (this as MainActivity).hideBottomNav()
        }
//        setupTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            (it as MainActivity).hideBottomNav()
        }
        setupStatusBar()
        setupToolbar()
        observeViewModel()
        loadSelectedMedicine()
    }

    fun onClickEdit(view: View) {
//        Toast.makeText(context!!, "Edytuj", Toast.LENGTH_SHORT).show()
        val dialog = TestDialogFragment()
        dialog.show(childFragmentManager, TestDialogFragment.TAG)
    }

    fun onClickDelete(view: View) {
        Toast.makeText(context!!, "Usuń", Toast.LENGTH_SHORT).show()
    }

    fun onClickTake(view: View) {
        Toast.makeText(context!!, "Weź", Toast.LENGTH_SHORT).show()
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentMedicineDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_details, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun loadSelectedMedicine() {
        val medicineID = args.medicineId
        viewModel.selectedMedicineID.value = medicineID
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration =  AppBarConfiguration(navController.graph)
        collapsing_toolbar.setupWithNavController(toolbar, navController, appBarConfiguration)
    }


    private fun setupStatusBar() {
        activity?.let {
            (it as MainActivity).setTransparentStatusBar(true)
        }
    }

    private fun observeViewModel() {
        viewModel.selectedMedicineID.observe(viewLifecycleOwner, Observer {  })
        viewModel.selectedMedicine.observe(viewLifecycleOwner, Observer {  })
        viewModel.photoLive.observe(viewLifecycleOwner, Observer {
            if (it != null) setMedicinePicture(it)
        })
        viewModel.stateColorResIdLive.observe(viewLifecycleOwner, Observer {
            if (it != null) setStateColor(it)
        })
    }

    private fun setMedicinePicture(photoFile: File) {
        Glide.with(this)
            .load(photoFile)
            .centerCrop()
            .into(img_medicine_picture)
    }

    private fun setStateColor(colorResId: Int) {
        txv_curr_state_text.setTextColor(resources.getColor(colorResId))
        line_state.setBackgroundResource(colorResId)
    }

    private fun setupTransition() {
        context?.let {
            val transHelper = TransitionHelper()
            sharedElementEnterTransition = transHelper.sharedElementEnterTransition(it)
        }
        enterTransition = Fade().apply {
            startDelay = 300L
        }
        returnTransition = Fade().apply {
            duration = 0L
        }
    }
}
