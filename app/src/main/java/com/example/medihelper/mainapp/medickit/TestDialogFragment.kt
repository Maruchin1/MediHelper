package com.example.medihelper.mainapp.medickit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TestDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_test, container, false)
    }

    companion object {
        val TAG = TestDialogFragment::class.simpleName
    }
}