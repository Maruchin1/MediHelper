package com.example.medihelper.mainapp.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentMenuBinding
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MenuFragment : Fragment() {
    private val TAG = MenuFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMainActivity()
    }

    fun onClickMenuOption(view: View) {
        Log.d(TAG, "onClickMenuOption")
        Log.d(TAG, "view ID = ${view.id}")
        val direction = when(view.id) {
            R.id.lay_click_schedule -> MenuFragmentDirections.toScheduleDestination()
            R.id.lay_click_kit -> MenuFragmentDirections.toKitDestination()
            R.id.lay_click_family ->MenuFragmentDirections.toFamilyDestination()
            else -> null
        }
        direction?.let {
            findNavController().navigate(direction)
        }
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentMenuBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        binding.handler = this
        return binding.root
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                setTransparentStatusBar(false)
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    hide()
                }
            }
        }
    }
}