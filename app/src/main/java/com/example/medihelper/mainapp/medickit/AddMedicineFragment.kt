package com.example.medihelper.mainapp.medickit


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.medihelper.AppDateTime
import com.example.medihelper.R
import com.example.medihelper.dialogs.SelectDateDialog
import com.example.medihelper.databinding.FragmentAddMedicineBinding
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_medicine.*
import java.io.File


class AddMedicineFragment : Fragment() {
    private val TAG = AddMedicineFragment::class.simpleName
    private val REQUEST_IMAGE_CAPTURE = 1

    private val args: MedicineDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: AddMedicineViewModel
    private lateinit var medicineTypeAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddMedicineViewModel::class.java)
            medicineTypeAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddMedicineBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_medicine, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetViewModel()
        setupMainActivity()
        setupToolbar()
        observeViewModel()
        setupSpinMedicineType()
        loadSelectedMedicineData()
    }

    fun onClickTakePhoto() {
        activity?.let {
            val intent = viewModel.takePhotoIntent(it)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun onClickExpireDate(view: View) {
        val dialog = SelectDateDialog()
        dialog.defaultDate = viewModel.expireDateLive.value
        dialog.setDateSelectedListener { date ->
            viewModel.expireDateLive.value = date
        }
        dialog.show(childFragmentManager, SelectDateDialog.TAG)
    }

    private fun observeViewModel() {
        viewModel.run {
            photoFileLive.observe(viewLifecycleOwner, Observer { photoFile ->
                Log.d(TAG, "photoFile change = $photoFile")
                setPhotoImage(photoFile)
            })
            medicineUnitListLive.observe(viewLifecycleOwner, Observer { medicineUnitList ->
                if (medicineUnitList != null) {
                    setMedicineTypeSpinnerItems(medicineUnitList)
                }
            })
        }
    }

    private fun setPhotoImage(photoFile: File?) {
        if (photoFile != null) {
            Glide.with(this)
                .load(photoFile)
                .centerCrop()
                .into(img_photo)
        }
    }

    private fun loadSelectedMedicineData() {
        val medicineId = args.medicineId
        if (medicineId != -1) {
            viewModel.setSelectedMedicineID(medicineId)
            txv_title.text = "Edytuj lek"
        } else {
            txv_title.text = "Dodaj lek"
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                setTransparentStatusBar(false)
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    setIconResource(R.drawable.round_save_white_48)
                    text = "Zapisz"
                    extend()
                    setOnClickListener { saveNewMedicine() }
                }
            }
        }
    }

    private fun saveNewMedicine() {
        val medicineSaved = viewModel.saveMedicine()
        if (medicineSaved) {
            findNavController().popBackStack()
            viewModel.resetViewModel()
        } else {
            val errorMessage = "Podanie nazwy leku jest wymagane"
            etx_medicine_name.error = errorMessage
            activity?.run {
                (this as MainActivity).showSnackBar(errorMessage)
            }
        }
    }

    private fun setupSpinMedicineType() {
        spin_medicine_type.adapter = medicineTypeAdapter
        spin_medicine_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setMedicineType(position)
            }
        }
    }

    private fun setMedicineTypeSpinnerItems(list: List<String>) {
        val medicineTypesNamesList = ArrayList<String>()
        list.forEach { medicineUnit ->
            medicineTypesNamesList.add(medicineUnit)
        }
        medicineTypeAdapter.apply {
            clear()
            addAll(medicineTypesNamesList)
            notifyDataSetChanged()
        }
    }
}
