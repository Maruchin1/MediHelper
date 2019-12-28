package com.maruchin.medihelper.device.ringtone

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import com.maruchin.medihelper.domain.device.DeviceRingtone

class DeviceRingtoneImpl(
    private val context: Context
) : DeviceRingtone {

    private val alarm: Ringtone by lazy { getRingtone(RingtoneManager.TYPE_ALARM) }

    override fun playAlarmRingtone() {
        if (!alarm.isPlaying) {
            alarm.play()
        }
    }

    override fun stopAlarmRingtone() {
        if (alarm.isPlaying) {
            alarm.stop()
        }
    }

    private fun getRingtone(type: Int): Ringtone {
        val ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, type)
        return RingtoneManager.getRingtone(context, ringtoneUri)
    }
}