package com.example.medihelper.mainapp.medickit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentMedicineDetailsBinding
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMedicineDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_details, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMainActivity()
        setupToolbar()
        observeViewModel()
        loadSelectedMedicine()
    }

    fun onClickEdit() {
        viewModel.selectedMedicineIDLive.value?.let { medicineId ->
            val action = MedicineDetailsFragmentDirections.toAddMedicineDestination(medicineId)
            findNavController().navigate(action)
        }
    }

    fun onClickDelete() {
        val dialog = ConfirmDialog().apply {
            title = "Usuń lek"
            message = "Wybrany lek zostanie usunięty. Czy chcesz kontynuować?"
            iconResId = R.drawable.round_delete_black_48
            setOnConfirmClickListener {
                viewModel.deleteMedicine()
                findNavController().popBackStack()
            }
        }
        dialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    private fun loadSelectedMedicine() {
        val medicineID = args.medicineId
        viewModel.selectedMedicineIDLive.value = medicineID
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        collapsing_toolbar.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                setTransparentStatusBar(true)
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    setIconResource(R.drawable.cross_icon)
                    text = "Zażyj lek"
                    extend()
                    setOnClickListener { takeMedicine() }
                }
            }
        }
    }

    private fun takeMedicine() {
        Toast.makeText(context!!, "Zażyj lej", Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.medicineDetailsLive.observe(viewLifecycleOwner, Observer { })
        viewModel.photoLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setMedicinePicture(it)
            } else {
                setMedicinePictureEmpty()
            }
        })
        viewModel.stateColorResIdLive.observe(viewLifecycleOwner, Observer {
            if (it != null) setStateColor(it)
        })
        viewModel.stateWeightLive.observe(viewLifecycleOwner, Observer {
            if (it != null) setViewWeight(line_state, it)
        })
        viewModel.emptyWeightLive.observe(viewLifecycleOwner, Observer {
            if (it != null) setViewWeight(line_empty, it)
        })
    }

    private fun setViewWeight(view: View, weight: Float) {
        view.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            weight
        )
    }

    private fun setMedicinePicture(photoFile: File) {
        Glide.with(this)
            .load(photoFile)
            .centerCrop()
            .into(img_medicine_picture)
    }

    private fun setMedicinePictureEmpty() {
        Glide.with(this)
            .load(R.drawable.ic_pill_white_48dp)
            .into(img_medicine_picture)
    }

    private fun setStateColor(colorResId: Int) {
        txv_curr_state_text.setTextColor(resources.getColor(colorResId))
        line_state.setBackgroundResource(colorResId)
    }
}
