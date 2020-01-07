package com.maruchin.medihelper.presentation.feature.doctors

import android.os.Bundle
import android.view.View
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentDoctorsBinding
import com.maruchin.medihelper.presentation.framework.BaseHomeFragment

class DoctorsFragment : BaseHomeFragment<FragmentDoctorsBinding>(R.layout.fragment_doctors) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setColorPrimaryStatusBar()
    }

    private fun setColorPrimaryStatusBar() {
        super.setStatusBarColor(R.color.colorPrimary)
    }
}