package com.maruchin.medihelper.device.reminder.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.ActivityAlarmBinding
import com.maruchin.medihelper.domain.device.DeviceRingtone
import kotlinx.android.synthetic.main.activity_alarm.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.hypot


class AlarmActivity : AppCompatActivity() {
    private val TAG = "AlarmActivity"

    companion object {
        const val EXTRA_DATA = "extra-data"
        private const val REVEAL_ANIM_TIME = 500L
        private const val FINISH_ACTIVITY_DELAY = 1000L
    }

    private val viewModel: AlarmViewModel by viewModel()
    private val deviceRingtone: DeviceRingtone by inject()

    fun onClickMedicineTaken(plannedMedicineId: String) {
        viewModel.setPlannedMedicineTaken(plannedMedicineId)
        circularRevealView(txv_medicine_taken)
        delayedActivityFinish()
        deviceRingtone.stopAlarmRingtone()
    }

    fun onClickMedicineNotTaken() {
        circularRevealView(txv_medicine_not_taken)
        delayedActivityFinish()
        deviceRingtone.stopAlarmRingtone()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindLayout()
        setupAlarmActivityFlags()
        loadInputData()
        deviceRingtone.playAlarmRingtone()
    }

    override fun onDestroy() {
        deviceRingtone.stopAlarmRingtone()
        super.onDestroy()
    }

    private fun bindLayout() {
        val binding = DataBindingUtil.setContentView<ActivityAlarmBinding>(this, R.layout.activity_alarm)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupAlarmActivityFlags() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorTransparent)
    }

    private fun loadInputData() {
        val data = intent.extras?.getString(EXTRA_DATA)
        if (data != null) {
            viewModel.initData(data)
        }
    }

    private fun circularRevealView(revealView: View) {
        val startX = revealView.width / 2
        val startY = revealView.height

        val endRadius = hypot(startX.toDouble(), startY.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(revealView, startX, startY, 0f, endRadius).apply {
            duration =
                REVEAL_ANIM_TIME
            interpolator = FastOutSlowInInterpolator()
        }
        revealView.visibility = View.VISIBLE
        anim.start()
    }

    private fun delayedActivityFinish() {
        lifecycleScope.launch {
            delay(FINISH_ACTIVITY_DELAY)
            finish()
        }
    }
}
