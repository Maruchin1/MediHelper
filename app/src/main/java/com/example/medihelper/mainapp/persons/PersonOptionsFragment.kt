package com.example.medihelper.mainapp.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.custom.setTransparentStatusBar
import com.example.medihelper.databinding.FragmentPersonOptionsBinding
import com.example.medihelper.dialogs.ConfirmDialog
import kotlinx.android.synthetic.main.fragment_person_options.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonOptionsFragment : AppFullScreenDialog() {

    private val viewModel: PersonOptionsViewModel by viewModel()
    private val directions by lazy { PersonOptionsFragmentDirections }
    private val args: PersonOptionsFragmentArgs by navArgs()

    fun onClickEdit() = viewModel.getPersonID()?.let { personID ->
        findNavController().navigate(directions.toAddEditPersonFragment(personID))
    }

    fun onClickDelete() = ConfirmDialog().apply {
        title = "Usuń osobę"
        message = "Wybrana osoba zostanie usunięta, wraz ze wszystkimi zaplanowanymi lekami. Czy chcesz kontynuować?"
        setOnConfirmClickListener {
            viewModel.deletePerson()
            this@PersonOptionsFragment.dismiss()
        }
    }.show(childFragmentManager)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentPersonOptionsBinding>(
            inflater = inflater,
            layoutResId = R.layout.fragment_person_options,
            container = container,
            handler = this,
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentStatusBar()
        viewModel.setArgs(args)

        viewModel.connectionKeyQrCodeLive.observe(viewLifecycleOwner, Observer {
            img_qr_code.setImageBitmap(it)
        })
    }
}