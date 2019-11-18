package com.example.medihelper.mainapp.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.mainapp.authentication.LoginFragment
import com.example.medihelper.mainapp.authentication.PatronConnectFragment

class AuthenticationFragment : AppFullScreenDialog() {

    private val args: AuthenticationFragmentArgs by navArgs()

    enum class AuthenticationType {
        LOGIN, CONNECT
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.authType) {
            AuthenticationType.LOGIN -> loadLoginFragment()
            AuthenticationType.CONNECT -> loadPatronConnectFragment()
        }
    }

    private fun loadLoginFragment() {
        childFragmentManager.beginTransaction()
            .add(R.id.frame_fragments, LoginFragment())
            .commit()
    }

    private fun loadPatronConnectFragment() {
        childFragmentManager.beginTransaction()
            .add(R.id.frame_fragments, PatronConnectFragment())
            .commit()
    }
}