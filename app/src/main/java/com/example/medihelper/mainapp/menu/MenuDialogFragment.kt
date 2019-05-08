package com.example.medihelper.mainapp.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.example.medihelper.mainapp.FullScreenDialogFragment

class MenuDialogFragment : FullScreenDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        super.setAnimations(R.style.AppTheme_SlideHorizontal)
    }

    companion object {
        val TAG = FullScreenDialogFragment::class.simpleName
    }
}