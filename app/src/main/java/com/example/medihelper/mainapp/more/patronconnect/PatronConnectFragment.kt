package com.example.medihelper.mainapp.more.patronconnect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentPatronConnectBinding
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_patron_connect.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PatronConnectFragment : AppFullScreenDialog() {

    private val viewModel: PatronConnectViewModel by viewModel()

    fun onClickScanQrCode() {
        IntentIntegrator.forSupportFragment(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            setPrompt("Zeskanuj kod z aplikacji opiekuna")
            setOrientationLocked(true)
        }.initiateScan()
    }

    fun onClickConfirm() = viewModel.loadAuthToken()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentPatronConnectBinding>(
            inflater = inflater,
            layoutResId = R.layout.fragment_patron_connect,
            container = container,
            handler = this,
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.contents?.let { personTempKey ->
            viewModel.personTempKeyLive.value = personTempKey
        }
    }

    private fun observerViewModel() {
        viewModel.errorPersonTempKey.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                showSnackbar(errorMessage)
            }
        })
    }

    private fun showSnackbar(message: String) = Snackbar.make(root_lay, message, Snackbar.LENGTH_SHORT).apply {
        animationMode = Snackbar.ANIMATION_MODE_SLIDE
    }.show()
}