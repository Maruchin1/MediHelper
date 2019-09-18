package com.example.medihelper.mainapp.loginregister


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Slide
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentLoginInputBinding
import com.example.medihelper.databinding.FragmentRegisterInputBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class LoginRegisterInputFragment : Fragment() {
    private val TAG = "LoginRegisterInputFragment"

    var fragmentMode = Mode.LOGIN
    private val viewModel: LoginRegisterViewModel by sharedViewModel(from = { requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAnimations()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (fragmentMode) {
            Mode.LOGIN -> {
                val binding: FragmentLoginInputBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_input, container, false)
                binding.viewModel = viewModel
                binding.handler = requireParentFragment() as LoginRegisterFragment
                binding.lifecycleOwner = viewLifecycleOwner
                binding.root
            }
            Mode.REGISTER -> {
                val binding: FragmentRegisterInputBinding =
                    DataBindingUtil.inflate(inflater, R.layout.fragment_register_input, container, false)
                binding.viewModel = viewModel
                binding.handler = requireParentFragment() as LoginRegisterFragment
                binding.lifecycleOwner = viewLifecycleOwner
                binding.root
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnimations()
    }

    private fun setupAnimations() {
        exitTransition = Slide(Gravity.BOTTOM).apply {
            duration = 500
        }
        enterTransition = Slide(Gravity.BOTTOM).apply {
            duration = 500
            startDelay = 800
        }
    }

    enum class Mode {
        LOGIN, REGISTER
    }
}
