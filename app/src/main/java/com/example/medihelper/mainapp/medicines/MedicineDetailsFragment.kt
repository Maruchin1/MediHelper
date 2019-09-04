package com.example.medihelper.mainapp.medicines


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.databinding.FragmentMedicineDetailsBinding
import kotlinx.android.synthetic.main.fragment_medicine_details.*
import java.io.File


class MedicineDetailsFragment : AppFullScreenDialog() {

    private val viewModel: MedicineDetailsViewModel by viewModels()
    private val args: MedicineDetailsFragmentArgs by navArgs()

    fun onClickEdit() {

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
        viewModel.setArgs(args)
        setTransparentStatusBar()
        setupToolbar()
        observeViewModel()
    }

    private fun setTransparentStatusBar() {
        context?.run {
            dialog?.window?.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    private fun setupToolbar() {
       toolbar.setNavigationOnClickListener { dismiss() }
    }

    private fun takeMedicine() {
        Toast.makeText(context!!, "Zażyj lej", Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
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