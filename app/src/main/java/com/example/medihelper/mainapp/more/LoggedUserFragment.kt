package com.example.medihelper.mainapp.more

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.databinding.FragmentLoggedUserBinding
import com.example.medihelper.dialogs.ConfirmDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoggedUserFragment : AppFullScreenDialog() {

    private val viewModel: LoggedUserViewModel by viewModel()

    fun onClickLogoutUser() = ConfirmDialog().apply {
        title = "Wyloguj"
        message = "Wylogowanie sprawi, że twoje dane nie będą synchronizowane z chmurą oraz aplikacjami podopiecznych. Czy chcesz kontynuować?"
        iconResId = R.drawable.baseline_account_circle_white_36
        setOnConfirmClickListener {
            viewModel.logoutUser()
            dismiss()
        }
    }.show(childFragmentManager)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoggedUserBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_logged_user, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentStatusBar()
    }

    private fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorTransparent)
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}