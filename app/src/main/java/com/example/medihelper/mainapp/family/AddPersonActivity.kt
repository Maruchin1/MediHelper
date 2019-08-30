package com.example.medihelper.mainapp.family

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.medihelper.R
import com.example.medihelper.custom.DiffCallback
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.ActivityAddPersonBinding
import kotlinx.android.synthetic.main.activity_add_person.*

class AddPersonActivity : AppCompatActivity() {
    private val TAG = AddPersonActivity::class.simpleName

    private val viewModel: AddPersonViewModel by viewModels()
//    private val args: AddPersonActivityArgs by navArgs()

    fun onClickSelectColor(colorResID: Int) {
        viewModel.personColorResIDLive.value = colorResID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddPersonBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_person)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        intent?.extras?.let {
            val args = AddPersonActivityArgs.fromBundle(it)
            Log.d(TAG, "editPersonID = ${args.editPersonID}")
            viewModel.setArgs(args)
        }
        setTransparentStatusBar()
        setupToolbar()
        setupColorRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.personColorDisplayDataListLive.observe(this, Observer { personColorDisplayDataList ->
            Log.d(TAG, "personColorList change = $personColorDisplayDataList")
            val adapter = recycler_view_color.adapter as PersonColorAdapter
            adapter.updateItemsList(personColorDisplayDataList)
        })
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_save -> {
                    viewModel.saveNewPerson()
                    onBackPressed()
                }
            }
            true
        }
    }

    private fun setupColorRecyclerView() {
        recycler_view_color.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = PersonColorAdapter()
        }
    }

    private fun setTransparentStatusBar() {
        window.statusBarColor = Color.TRANSPARENT
    }

    // Inner classes
    inner class PersonColorAdapter : RecyclerAdapter<AddPersonViewModel.PersonColorDisplayData>(
        R.layout.recycler_item_person_color,
        object : DiffCallback<AddPersonViewModel.PersonColorDisplayData>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].colorResID == newList[newItemPosition].colorResID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personColorDisplayData = itemsList[position]
            holder.bind(personColorDisplayData, this@AddPersonActivity)
        }
    }
}
